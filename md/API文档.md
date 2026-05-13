# 社区论坛项目 - 后端接口文档

## 基础信息

- **基础URL**: `http://localhost:9000`
- **响应格式**: JSON
- **认证方式**: JWT Token (Header: `Authorization: Bearer <token>`)
- **用户ID获取**: Header: `userId`

## 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 分页响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "records": [],
    "current": 1,
    "size": 10,
    "pages": 10
  }
}
```

---

## 用户模块

### 1. 用户注册

**接口地址**: `POST /api/users/register`

**请求参数**:
```json
{
  "username": "用户名",
  "password": "密码",
  "phone": "手机号"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "用户名",
    "avatar": null,
    "bio": null,
    "followCount": 0,
    "fansCount": 0,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 2. 用户登录

**接口地址**: `POST /api/users/login`

**请求参数**:
```json
{
  "username": "用户名",
  "password": "密码"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": 1,
    "username": "用户名",
    "avatar": null,
    "bio": null,
    "followCount": 0,
    "fansCount": 0,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 3. 获取用户信息

**接口地址**: `GET /api/users/{id}`

**路径参数**:
- `id`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "用户名",
    "avatar": null,
    "bio": null,
    "followCount": 0,
    "fansCount": 0,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 4. 获取当前用户信息

**接口地址**: `GET /api/users/current`

**请求头**:
- `userId`: 当前用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "用户名",
    "avatar": null,
    "bio": null,
    "followCount": 0,
    "fansCount": 0,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 5. 更新用户信息

**接口地址**: `PUT /api/users/current`

**请求头**:
- `userId`: 当前用户ID

**请求参数**:
```json
{
  "avatar": "头像URL",
  "bio": "个人简介",
  "email": "邮箱"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "username": "用户名",
    "avatar": "头像URL",
    "bio": "个人简介",
    "followCount": 0,
    "fansCount": 0,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

## 帖子模块

### 6. 发布帖子

**接口地址**: `POST /api/posts`

**请求头**:
- `userId`: 当前用户ID

**请求参数**:
```json
{
  "title": "帖子标题",
  "content": "帖子内容"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 1,
    "title": "帖子标题",
    "content": "帖子内容",
    "userId": 1,
    "username": "用户名",
    "userAvatar": null,
    "likeCount": 0,
    "commentCount": 0,
    "viewCount": 0,
    "isLiked": false,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00",
    "updateTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 7. 更新帖子

**接口地址**: `PUT /api/posts/{id}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `id`: 帖子ID

**请求参数**:
```json
{
  "title": "帖子标题",
  "content": "帖子内容"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "title": "帖子标题",
    "content": "帖子内容",
    "userId": 1,
    "username": "用户名",
    "userAvatar": null,
    "likeCount": 0,
    "commentCount": 0,
    "viewCount": 0,
    "isLiked": false,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00",
    "updateTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 8. 删除帖子

**接口地址**: `DELETE /api/posts/{id}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `id`: 帖子ID

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 9. 获取帖子详情

**接口地址**: `GET /api/posts/{id}`

**路径参数**:
- `id`: 帖子ID

**请求头** (可选):
- `userId`: 当前用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "帖子标题",
    "content": "帖子内容",
    "userId": 1,
    "username": "用户名",
    "userAvatar": null,
    "likeCount": 0,
    "commentCount": 0,
    "viewCount": 1,
    "isLiked": false,
    "isFollowing": false,
    "createTime": "2024-01-01T00:00:00",
    "updateTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 10. 获取帖子列表

**接口地址**: `GET /api/posts`

**查询参数**:
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**请求头** (可选):
- `userId`: 当前用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "title": "帖子标题",
        "content": "帖子内容",
        "userId": 1,
        "username": "用户名",
        "userAvatar": null,
        "likeCount": 0,
        "commentCount": 0,
        "viewCount": 0,
        "isLiked": false,
        "isFollowing": false,
        "createTime": "2024-01-01T00:00:00",
        "updateTime": "2024-01-01T00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 10
  }
}
```

**状态**: ✅ 已完成

---

### 11. 搜索帖子

**接口地址**: `GET /api/posts/search`

**查询参数**:
- `keyword`: 搜索关键词
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**请求头** (可选):
- `userId`: 当前用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "title": "帖子标题",
        "content": "帖子内容",
        "userId": 1,
        "username": "用户名",
        "userAvatar": null,
        "likeCount": 0,
        "commentCount": 0,
        "viewCount": 0,
        "isLiked": false,
        "isFollowing": false,
        "createTime": "2024-01-01T00:00:00",
        "updateTime": "2024-01-01T00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

**状态**: ✅ 已完成

---

## 评论模块

### 12. 发表评论

**接口地址**: `POST /api/comments`

**请求头**:
- `userId`: 当前用户ID

**查询参数**:
- `postId`: 帖子ID

**请求参数**:
```json
{
  "content": "评论内容"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "id": 1,
    "postId": 1,
    "userId": 1,
    "username": "用户名",
    "userAvatar": null,
    "content": "评论内容",
    "likeCount": 0,
    "isLiked": false,
    "createTime": "2024-01-01T00:00:00"
  }
}
```

**状态**: ✅ 已完成

---

### 13. 删除评论

**接口地址**: `DELETE /api/comments/{id}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `id`: 评论ID

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 14. 获取评论列表

**接口地址**: `GET /api/comments`

**查询参数**:
- `postId`: 帖子ID
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**请求头** (可选):
- `userId`: 当前用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "postId": 1,
        "userId": 1,
        "username": "用户名",
        "userAvatar": null,
        "content": "评论内容",
        "likeCount": 0,
        "isLiked": false,
        "createTime": "2024-01-01T00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

**状态**: ✅ 已完成

---

## 点赞模块

### 15. 点赞帖子

**接口地址**: `POST /api/likes/post/{postId}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 16. 取消点赞帖子

**接口地址**: `DELETE /api/likes/post/{postId}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
  "code": 200,
  "message": "取消点赞成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 17. 点赞评论

**接口地址**: `POST /api/likes/comment/{commentId}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `commentId`: 评论ID

**响应示例**:
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 18. 取消点赞评论

**接口地址**: `DELETE /api/likes/comment/{commentId}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `commentId`: 评论ID

**响应示例**:
```json
{
  "code": 200,
  "message": "取消点赞成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 19. 检查帖子点赞状态

**接口地址**: `GET /api/likes/post/{postId}/check`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": true
}
```

**状态**: ✅ 已完成

---

### 20. 检查评论点赞状态

**接口地址**: `GET /api/likes/comment/{commentId}/check`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `commentId`: 评论ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": true
}
```

**状态**: ✅ 已完成

---

## 关注模块

### 21. 关注用户

**接口地址**: `POST /api/follows/{targetUserId}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `targetUserId`: 目标用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "关注成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 22. 取消关注用户

**接口地址**: `DELETE /api/follows/{targetUserId}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `targetUserId`: 目标用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "取消关注成功",
  "data": null
}
```

**状态**: ✅ 已完成

---

### 23. 检查关注状态

**接口地址**: `GET /api/follows/check/{targetUserId}`

**请求头**:
- `userId`: 当前用户ID

**路径参数**:
- `targetUserId`: 目标用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": true
}
```

**状态**: ✅ 已完成

---

### 24. 获取关注列表

**接口地址**: `GET /api/follows/following`

**请求头**:
- `userId`: 当前用户ID

**查询参数**:
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 2,
        "username": "用户名",
        "avatar": null,
        "bio": null,
        "followCount": 0,
        "fansCount": 0,
        "isFollowing": false,
        "createTime": "2024-01-01T00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

**状态**: ✅ 已完成

---

### 25. 获取粉丝列表

**接口地址**: `GET /api/follows/followers`

**请求头**:
- `userId`: 当前用户ID

**查询参数**:
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 2,
        "username": "用户名",
        "avatar": null,
        "bio": null,
        "followCount": 0,
        "fansCount": 0,
        "isFollowing": false,
        "createTime": "2024-01-01T00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

**状态**: ✅ 已完成

---

## 排行榜模块

### 26. 获取帖子排行榜

**接口地址**: `GET /api/rankings/posts`

**查询参数**:
- `type`: 排行榜类型 (all: 总榜, week: 周榜, 默认: all)
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "title": "帖子标题",
        "userId": 1,
        "username": "用户名",
        "userAvatar": null,
        "likeCount": 100,
        "commentCount": 10,
        "viewCount": 1000,
        "createTime": "2024-01-01T00:00:00",
        "rank": 1
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

**状态**: ✅ 已完成

---

## 个人中心模块

### 27. 获取我的帖子列表

**接口地址**: `GET /api/personal/posts`

**请求头**:
- `userId`: 当前用户ID

**查询参数**:
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "title": "帖子标题",
        "content": "帖子内容",
        "userId": 1,
        "username": "用户名",
        "userAvatar": null,
        "likeCount": 0,
        "commentCount": 0,
        "viewCount": 0,
        "isLiked": false,
        "isFollowing": false,
        "createTime": "2024-01-01T00:00:00",
        "updateTime": "2024-01-01T00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

**状态**: ✅ 已完成

---

### 28. 获取我的点赞记录

**接口地址**: `GET /api/personal/liked-posts`

**请求头**:
- `userId`: 当前用户ID

**查询参数**:
- `page`: 页码 (默认: 1)
- `size`: 每页条数 (默认: 10)

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "title": "帖子标题",
        "content": "帖子内容",
        "userId": 1,
        "username": "用户名",
        "userAvatar": null,
        "likeCount": 0,
        "commentCount": 0,
        "viewCount": 0,
        "isLiked": true,
        "isFollowing": false,
        "createTime": "2024-01-01T00:00:00",
        "updateTime": "2024-01-01T00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```

**状态**: ✅ 已完成

---

## 接口完成状态总结

| 模块 | 接口数量 | 完成状态 |
|------|---------|---------|
| 用户模块 | 5 | ✅ 全部完成 |
| 帖子模块 | 6 | ✅ 全部完成 |
| 评论模块 | 3 | ✅ 全部完成 |
| 点赞模块 | 6 | ✅ 全部完成 |
| 关注模块 | 5 | ✅ 全部完成 |
| 排行榜模块 | 1 | ✅ 全部完成 |
| 个人中心模块 | 2 | ✅ 全部完成 |
| **总计** | **28** | **✅ 全部完成** |

---

## 注意事项

1. **JWT认证**: 用户登录后，前端需要将JWT Token存储在本地，并在每次请求时通过Header传递
2. **用户ID**: 当前用户ID通过Header `userId` 传递，用于权限验证和个性化数据返回
3. **分页参数**: 所有分页接口默认 `page=1`, `size=10`
4. **时间格式**: 所有时间字段使用ISO 8601格式 (YYYY-MM-DDTHH:mm:ss)
5. **错误处理**: 所有接口返回统一的错误格式，错误码为500时表示业务异常
6. **Redis集成**: 点赞和关注功能预留了Redis接口，后续可优化性能
7. **文件上传**: 文件上传接口需要您自行实现
8. **通知功能**: 通知模块的数据库表已创建，但接口未实现，可后续迭代

---

## 技术栈说明

- **后端框架**: Spring Boot 2.7
- **ORM框架**: MyBatis-Plus 3.5
- **数据库**: MySQL 8.0
- **缓存**: Redis (预留)
- **消息队列**: RabbitMQ (预留)
- **认证**: JWT (预留)
- **工具库**: Lombok

---

## 项目结构

```
forum-backend/
├── src/main/java/com/forum/
│   ├── common/          # 通用类
│   │   ├── Result.java
│   │   └── PageResult.java
│   ├── controller/      # 控制器
│   │   ├── UserController.java
│   │   ├── PostController.java
│   │   ├── CommentController.java
│   │   ├── LikeController.java
│   │   ├── FollowController.java
│   │   ├── RankingController.java
│   │   └── PersonalController.java
│   ├── dto/            # 数据传输对象
│   │   ├── UserLoginDTO.java
│   │   ├── UserRegisterDTO.java
│   │   ├── UserUpdateDTO.java
│   │   ├── PostCreateDTO.java
│   │   ├── PostUpdateDTO.java
│   │   └── CommentCreateDTO.java
│   ├── entity/         # 实体类
│   │   ├── User.java
│   │   ├── Post.java
│   │   ├── Comment.java
│   │   ├── Like.java
│   │   ├── Follow.java
│   │   └── Notification.java
│   ├── mapper/         # 数据访问层
│   │   ├── UserMapper.java
│   │   ├── PostMapper.java
│   │   ├── CommentMapper.java
│   │   ├── LikeMapper.java
│   │   ├── FollowMapper.java
│   │   └── NotificationMapper.java
│   ├── service/        # 业务逻辑层
│   │   ├── UserService.java
│   │   ├── PostService.java
│   │   ├── CommentService.java
│   │   ├── LikeService.java
│   │   ├── FollowService.java
│   │   ├── RankingService.java
│   │   └── PersonalService.java
│   └── service/impl/   # 业务逻辑实现
│       ├── UserServiceImpl.java
│       ├── PostServiceImpl.java
│       ├── CommentServiceImpl.java
│       ├── LikeServiceImpl.java
│       ├── FollowServiceImpl.java
│       ├── RankingServiceImpl.java
│       └── PersonalServiceImpl.java
└── src/main/resources/
    └── application.yml  # 配置文件
```

---

## 数据库表结构

### user (用户表)
- id: 用户ID
- username: 用户名
- password: 密码
- phone: 手机号
- email: 邮箱
- avatar: 头像URL
- bio: 个人简介
- follow_count: 关注数
- fans_count: 粉丝数
- status: 状态
- create_time: 创建时间
- update_time: 更新时间

### post (帖子表)
- id: 帖子ID
- user_id: 发布者ID
- title: 标题
- content: 内容
- like_count: 点赞数
- comment_count: 评论数
- view_count: 浏览数
- status: 状态
- create_time: 创建时间
- update_time: 更新时间

### comment (评论表)
- id: 评论ID
- post_id: 帖子ID
- user_id: 评论者ID
- parent_id: 父评论ID
- content: 内容
- like_count: 点赞数
- status: 状态
- create_time: 创建时间

### like_record (点赞记录表)
- id: 记录ID
- user_id: 用户ID
- target_type: 目标类型 (1-帖子 2-评论)
- target_id: 目标ID
- create_time: 创建时间

### follow (关注表)
- id: 记录ID
- user_id: 关注者ID
- follow_user_id: 被关注者ID
- create_time: 创建时间

### notification (通知表)
- id: 通知ID
- user_id: 接收通知的用户ID
- from_user_id: 触发通知的用户ID
- type: 类型 (1-点赞 2-评论 3-关注)
- target_id: 目标ID
- content: 内容
- is_read: 是否已读
- create_time: 创建时间
