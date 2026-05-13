# Redis 六大功能实现方案

---

## 📋 概览

| 功能          | Redis 数据结构  | 实现难度 | 建议顺序 |
|:----------- |:----------- |:----:|:----:|
| 用户签到        | Bitmap      | ⭐    | ①    |
| 帖子点赞        | Set         | ⭐    | ②    |
| UV 统计       | HyperLogLog | ⭐    | ③    |
| 帖子排行榜       | ZSet        | ⭐⭐   | ④    |
| 点赞排行榜       | ZSet        | ⭐⭐   | ⑤    |
| 好友关注/Feed推送 | Set + List  | ⭐⭐⭐  | ⑥    |

---

## 一、用户签到

### 技术选型：Bitmap

Bitmap 的每个 bit 位代表一天的签到状态（1=已签，0=未签），一个用户一年只需要 **365 bit = 46 字节**，非常省空间。

### Key 设计

```
KEY: sign:userId:yyyyMM     VALUE: Bitmap
举例: sign:1:202604          → 用户1在2026年4月的签到记录
```

### 数据结构

```
bit 0  → 4月1日   (1=已签)
bit 1  → 4月2日   (0=未签)
bit 2  → 4月3日   (1=已签)
...
```

### 接口设计

```
签到：          POST   /api/sign/in                 → 当天签到（幂等，重复签到不报错）
获取当月签到：    GET    /api/sign?year=2026&month=4  → 返回当月签到日期列表
获取签到统计：    GET    /api/sign/stats              → 当月连续签到天数、总天数
```

### 核心代码示例

```java
// 签到
public boolean sign(Long userId) {
    LocalDate today = LocalDate.now();
    String key = "sign:" + userId + ":" + today.format(DateTimeFormatter.ofPattern("yyyyMM"));
    int offset = today.getDayOfMonth() - 1;
    Boolean signed = redisTemplate.opsForValue().setBit(key, offset, true);
    return Boolean.TRUE.equals(signed); // true=已签过, false=首次签到
}

// 获取当月签到列表
public List<Integer> getSignList(Long userId, Integer year, Integer month) {
    String key = "sign:" + userId + ":" + String.format("%04d%02d", year, month);
    int days = YearMonth.of(year, month).lengthOfMonth();
    List<Integer> signedDays = new ArrayList<>();
    for (int i = 0; i < days; i++) {
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(key, i))) {
            signedDays.add(i + 1);
        }
    }
    return signedDays;
}

// 获取签到统计（总天数 + 连续天数）
public Map<String, Long> getSignStats(Long userId) {
    // 1. 用 BITCOUNT 统计当月总签到天数
    // 2. 从今天往前遍历，统计连续签到天数
}
```

---

## 二、帖子点赞

### 技术选型：Set

Set 天然支持去重，每个元素是用户 ID，可以判断用户是否点过赞。

### Key 设计

```
KEY: post:liked:postId      VALUE: Set<userId>
举例: post:liked:1          → 赞了帖子1的所有用户ID
```

### 数据流

```
用户点赞   → SADD post:liked:1 1001       → 返回1（新增成功）
用户取消赞 → SREM post:liked:1 1001       → 返回1（移除成功）
查是否点赞 → SISMEMBER post:liked:1 1001  → 返回true/false
查点赞数   → SCARD post:liked:1           → 返回数量
```

### 接口设计

```
点赞帖子：   POST   /api/likes/post/{postId}         → 调用 SADD
取消赞帖子：  DELETE /api/likes/post/{postId}         → 调用 SREM
点赞评论：   POST   /api/likes/comment/{commentId}   → 同上
取消赞评论：  DELETE /api/likes/comment/{commentId}   → 同上
点赞状态：   GET    /api/likes/post/{postId}/check   → 调用 SISMEMBER
```

### 与数据库同步策略

Redis 的热数据会因宕机丢失，需要用定时任务同步到数据库：

```
用户点赞 → 写 Redis + 写 MySQL like 表（事务）
         或：先写 Redis → 定时任务批量刷到 MySQL
推荐方案：强一致性用前者，高性能用后者
```

---

## 三、UV 统计

### 技术选型：HyperLogLog

UV 统计的特点是数据量大、允许微小误差（~0.81%）。HyperLogLog 每个 key 只占 **12KB**，可以统计海量独立访客。

### Key 设计

```
KEY: uv:post:postId:yyyyMMdd     VALUE: HyperLogLog
举例: uv:post:1:20260421          → 帖子1在2026-04-21的访客
```

### 接口设计

```
浏览帖子时记录 UV：  GET /api/posts/{id} 时自动记录
获取帖子 UV：         GET /api/posts/{id}/uv → 返回今日UV + 总UV
```

### 核心代码

```java
// 记录UV（在 PostService.getPostById 中调用）
public void recordUV(Long postId, Long userId) {
    String todayKey = "uv:post:" + postId + ":" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    redisTemplate.opsForHyperLogLog().add(todayKey, userId.toString());
}

// 获取UV
public Long getUV(Long postId) {
    // 今日 UV
    String todayKey = "uv:post:" + postId + ":" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    Long todayUV = redisTemplate.opsForHyperLogLog().size(todayKey);

    // 合并所有日期的 UV 得到总 UV
    // 用 PFMERGE 合并后统计
    return todayUV;
}
```

---

## 四、帖子排行榜

### 技术选型：ZSet

ZSet 的 score 就是排序分数，天然支持按热度排序。

### Key 设计

```
KEY: post:rank:period         VALUE: ZSet<postId, score>
举例:
  post:rank:daily    → 日榜（按当日点赞数）
  post:rank:weekly   → 周榜
  post:rank:monthly  → 月榜
  post:rank:alltime  → 总榜
```

### 分数计算

```
score = likeCount * 10 + commentCount * 5 + viewCount * 1
```

### 更新策略

用户点赞/评论时同时更新 ZSet 的 score：

```java
// 点赞时同步更新排行榜
public void likePost(Long userId, Long postId) {
    // 1. 写入点赞 Set
    redisTemplate.opsForSet().add("post:liked:" + postId, userId.toString());

    // 2. 更新各排行榜的 score（+10 分）
    double increment = 10.0;
    redisTemplate.opsForZSet().incrementScore("post:rank:daily", postId.toString(), increment);
    redisTemplate.opsForZSet().incrementScore("post:rank:weekly", postId.toString(), increment);
    redisTemplate.opsForZSet().incrementScore("post:rank:monthly", postId.toString(), increment);
}
```

### 获取排行榜

```java
// 获取日榜 TOP N
public List<Long> getDailyRanking(int topN) {
    Set<String> postIds = redisTemplate.opsForZSet()
        .reverseRange("post:rank:daily", 0, topN - 1);
    return postIds.stream().map(Long::valueOf).collect(Collectors.toList());
}
```

### 定时任务：清理过期排行榜

```java
@Scheduled(cron = "0 0 0 * * ?")  // 每天0点
public void resetDailyRank() {
    redisTemplate.delete("post:rank:daily");
}

@Scheduled(cron = "0 0 0 * * MON")  // 每周一0点
public void resetWeeklyRank() {
    redisTemplate.delete("post:rank:weekly");
}
```

---

## 五、点赞排行榜

### 技术选型：ZSet

谁的帖子收到的点赞总数多，谁就排在前面。

### Key 设计

```
KEY: like:rank:period      VALUE: ZSet<userId, totalLikes>
举例: like:rank:weekly      → 本周获赞最多的用户
```

### 更新策略

每次有点赞行为时更新：

```java
// 点赞时更新用户的获赞数
public void onLike(Long postUserId, Long postId) {
    double increment = 1.0;
    redisTemplate.opsForZSet().incrementScore("like:rank:daily", postUserId.toString(), increment);
    redisTemplate.opsForZSet().incrementScore("like:rank:weekly", postUserId.toString(), increment);
}
```

### 接口设计

```
GET /api/ranking/like?period=daily&limit=10   → 获赞最多的用户排行
```

---

## 六、好友关注 / Feed 推送

### 技术选型：Set + List

这是最复杂的一个功能。采用 **推模式（Fanout）**：博主发帖时，把帖子 ID 推送到所有粉丝的收件箱。

### 数据结构

```
粉丝关系（Set）：
  KEY: user:followers:userId     → Set<followerId>    关注者列表
  KEY: user:following:userId     → Set<followUserId>   关注列表

用户收件箱（List，ZSet也可以）：
  KEY: user:inbox:userId         → List<postId>        按时间倒序
```

### 发帖推送到粉丝（推模式）

```java
@Transactional
public PostVO createPost(Long userId, PostCreateDTO dto) {
    // 1. 写数据库
    Post post = new Post();
    // ...
    postMapper.insert(post);

    // 2. 查询所有粉丝
    Set<String> followers = redisTemplate.opsForSet()
        .members("user:followers:" + userId);

    // 3. 推送到每个粉丝的收件箱（最多保留200条）
    for (String followerId : followers) {
        String inboxKey = "user:inbox:" + followerId;
        redisTemplate.opsForList().leftPush(inboxKey, post.getId().toString());
        redisTemplate.opsForList().trim(inboxKey, 0, 199); // 只保留最近200条
    }

    return convertToVO(post);
}
```

### 粉丝拉取 Feed

```java
// 获取关注动态（首页「关注」Tab）
public PageResult<PostVO> getFeedPosts(Long userId, Integer page, Integer size) {
    String inboxKey = "user:inbox:" + userId;
    int start = (page - 1) * size;
    int end = start + size - 1;

    List<String> postIds = redisTemplate.opsForList()
        .range(inboxKey, start, end);

    if (postIds == null || postIds.isEmpty()) {
        return PageResult.empty();
    }

    // 根据 postId 批量查询帖子详情（从数据库或缓存）
    List<PostVO> posts = postIds.stream()
        .map(id -> postService.getPostById(Long.valueOf(id), userId))
        .collect(Collectors.toList());

    return new PageResult<>(posts, page, size);
}
```

### 粉丝关系同步策略

```
关注用户   → SADD user:followers:2 1 + SADD user:following:1 2
取关用户   → SREM user:followers:2 1 + SREM user:following:1 2

小技巧：新用户关注后，可以把被关注者最近20条帖子拉到收件箱
```

---

## 🗓️ 实施顺序建议

```
第一天：Redis 基础配置
├── 1. 配置 Redis 连接（连接池、序列化）
├── 2. 创建 RedisConfig.java（配置 RedisTemplate 序列化方式）
└── 3. 测试连通性

第二天：签到 + UV
├── 1. 用户签到功能（SignController + SignService）
├── 2. UV 统计（在 PostService 中植入）
└── 3. 前端对接

第三天：点赞 + 排行榜
├── 1. 用 Set 重构点赞服务
├── 2. 帖子排行榜（ZSet）
├── 3. 点赞排行榜（ZSet）
└── 4. 前端对接

第四天：Feed 推送
├── 1. 粉丝关系 Redis 化
├── 2. 发帖推送（Fanout）
├── 3. 收件箱拉取
├── 4. 首页「关注」Tab 对接
└── 5. 新用户关注时初始化收件箱
```

---

## 🔧 RedisConfig 配置参考

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Key 使用 String 序列化
        template.setKeySerializer(new StringRedisSerializer());
        // Value 使用 Jackson 序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Hash 的 key/value 也设置
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }
}
```

---

## 📊 数据流总图

```
┌──────────────────────────────────────────────────┐
│                   用户操作                        │
└──────────┬──────────┬──────────┬─────────────────┘
           │          │          │
     ┌─────▼──┐  ┌───▼───┐  ┌──▼────────┐
     │ 点赞    │  │ 签到   │  │ 发帖      │
     │ POST   │  │ POST   │  │ POST      │
     └───┬────┘  └───┬───┘  └───┬───────┘
         │           │          │
         ▼           ▼          ▼
   ┌─────────┐ ┌─────────┐ ┌─────────────────┐
   │ SADD    │ │ SETBIT  │ │ 数据库 INSERT    │
   │ Set     │ │ Bitmap  │ │ + Fanout 推送    │
   │ post:   │ │ sign:1: │ │ 到粉丝收件箱     │
   │ liked:1 │ │ 202604  │ │ (List)          │
   └──┬──────┘ └─────────┘ └────────┬────────┘
      │                              │
      ▼                              ▼
  ┌──────────┐              ┌──────────────┐
  │ ZINCRBY  │              │ LEFT PUSH    │
  │ 排行榜    │              │ 粉丝收件箱    │
  │ ZSet     │              │ List         │
  └──────────┘              └──────────────┘


┌──────────────────────────────────────────────────┐
│                   查看操作                        │
└──────────┬──────────┬──────────┬─────────────────┘
           │          │          │
     ┌─────▼──┐  ┌───▼───┐  ┌──▼────────┐
     │ 首页    │  │ 排行榜 │  │ 签到      │
     │ 关注Tab │  │ GET   │  │ GET       │
     └───┬────┘  └───┬───┘  └───┬───────┘
         │           │          │
         ▼           ▼          ▼
   ┌─────────┐ ┌──────────┐ ┌──────────┐
   │ LRANGE  │ │ ZREVRANGE│ │ GETBIT   │
   │ 收件箱  │ │ 排行榜   │ │ Bitmap   │
   │ List    │ │ ZSet     │ │ 遍历     │
   └─────────┘ └──────────┘ └──────────┘
```

---

## ⚠️ 注意事项

1. **点赞数据持久化**：Redis 重启会丢失数据，建议每天凌晨定时把 Redis 点赞数同步到 MySQL
2. **Feed 推送量**：大 V（百万粉丝）不能用推模式，要改成拉模式或推拉结合
3. **排行榜过期**：日榜/周榜设置 TTL 自动过期，避免无用数据堆积
4. **Bitmap 偏移**：签到用 `dayOfMonth - 1` 作为 offset，跨月会换 key
5. **HyperLogLog 误差**：~0.81% 的误差对 UV 统计可接受，但不能用于精确计数
6. **Redis Key 统一前缀**：建议加项目名前缀，如 `forum:post:liked:1`
