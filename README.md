# Token-Now 社区论坛

一个基于 Spring Boot + Vue3 的全栈社区论坛项目，支持帖子发布、评论互动、点赞关注、排行榜、签到等功能。

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen)
![Vue](https://img.shields.io/badge/Vue-3.5-42b883)
![TypeScript](https://img.shields.io/badge/TypeScript-5.6-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479a1)
![Redis](https://img.shields.io/badge/Redis-7.0-dc382d)

---

## 项目简介

Token-Now 是一个现代化的移动端社区论坛应用，采用前后端分离架构。后端使用 Spring Boot 2.7 + MyBatis-Plus 构建 RESTful API，前端使用 Vue3 + Vant4 打造流畅的移动端体验。

### 核心特性

- **用户系统**：手机号注册/登录，JWT Token 认证
- **帖子管理**：发布、编辑、删除帖子，支持搜索和分页浏览
- **评论互动**：发表评论、删除评论，实时统计评论数
- **点赞系统**：帖子/评论点赞，基于 Redis ZSet 实现高性能计数
- **关注系统**：关注/取消关注用户，查看关注列表和粉丝列表
- **排行榜**：帖子排行榜（周榜/总榜）+ 用户点赞排行
- **签到功能**：基于 Redis Bitmap 的用户签到，连续签到统计
- **文件上传**：阿里云 OSS 头像上传
- **通知系统**：点赞、评论、关注实时通知（RabbitMQ 异步）

---

## 技术栈

### 后端 (forum-backend)

| 技术 | 版本 | 用途 |
|:---|:---|:---|
| Spring Boot | 2.7.18 | 核心框架 |
| MyBatis-Plus | 3.5.3.2 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7.0 | 缓存/排行榜/签到 |
| RabbitMQ | 3.x | 消息队列（通知） |
| JWT (jjwt) | 0.11.5 | Token 认证 |
| 阿里云 OSS | 3.15.1 | 文件存储 |
| Lombok | - | 代码简化 |

### 前端 (forum-frontend)

| 技术 | 版本 | 用途 |
|:---|:---|:---|
| Vue | 3.5 | 核心框架 |
| TypeScript | 5.6 | 类型安全 |
| Vant | 4.9 | 移动端 UI 组件库 |
| Pinia | 2.2 | 状态管理 |
| Axios | 1.7 | HTTP 请求 |
| Vite | 6.0 | 构建工具 |
| dayjs | 1.11 | 时间处理 |
| postcss-px-to-viewport | 1.1 | 移动端适配 |

---

## 项目结构

```
Token-Now/
├── forum-backend/              # 后端项目
│   ├── src/main/java/com/forum/
│   │   ├── controller/         # REST API 控制器
│   │   ├── service/            # 业务逻辑层
│   │   ├── mapper/             # MyBatis-Plus Mapper
│   │   ├── entity/             # 数据库实体
│   │   ├── dto/                # 请求数据传输对象
│   │   ├── vo/                 # 响应视图对象
│   │   ├── config/             # 配置类
│   │   ├── interceptor/        # JWT 拦截器
│   │   ├── common/             # 通用响应类
│   │   └── utils/              # 工具类
│   └── src/main/resources/
│       ├── application.yml     # 主配置
│       └── application-dev.yml.template  # 开发环境配置模板
│
├── forum-frontend/             # 前端项目
│   ├── src/
│   │   ├── api/                # API 请求模块
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 公共组件
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── router/             # 路由配置
│   │   ├── types/              # TypeScript 类型定义
│   │   └── utils/              # 工具函数
│   ├── package.json
│   └── vite.config.ts
│
├── mysql/                      # 数据库脚本
│   └── forum_db.sql            # 建表语句
│
└── md/                         # 项目文档
```

---

## 快速开始

### 环境要求

- JDK 1.8+
- Node.js 18+
- MySQL 8.0
- Redis 7.0
- RabbitMQ 3.x（可选，通知功能需要）

### 后端启动

1. **创建数据库**

```bash
mysql -u root -p < mysql/forum_db.sql
```

2. **配置开发环境**

```bash
cd forum-backend/src/main/resources
cp application-dev.yml.template application-dev.yml
# 编辑 application-dev.yml，填入数据库、Redis、OSS 等真实配置
```

3. **启动后端**

```bash
cd forum-backend
mvn spring-boot:run
```

后端默认运行在 `http://localhost:9000`

### 前端启动

```bash
cd forum-frontend
npm install
npm run dev
```

前端默认运行在 `http://localhost:15173`

---

## API 文档

后端 API 统一使用 `/api` 前缀，主要接口如下：

| 模块 | 接口 | 方法 | 描述 |
|:---|:---|:---:|:---|
| 用户 | `/api/users/register` | POST | 用户注册 |
| 用户 | `/api/users/login` | POST | 用户登录 |
| 用户 | `/api/users/current` | GET | 获取当前用户信息 |
| 用户 | `/api/users/current` | PUT | 更新用户信息 |
| 帖子 | `/api/posts` | POST | 发布帖子 |
| 帖子 | `/api/posts/{id}` | GET | 帖子详情 |
| 帖子 | `/api/posts` | GET | 帖子列表（分页） |
| 帖子 | `/api/posts/search` | GET | 搜索帖子 |
| 评论 | `/api/comments` | POST | 发表评论 |
| 评论 | `/api/comments/{id}` | DELETE | 删除评论 |
| 点赞 | `/api/likes/post/{id}` | PUT | 帖子点赞/取消 |
| 点赞 | `/api/likes/comment/{id}` | PUT | 评论点赞/取消 |
| 关注 | `/api/follows/{id}` | POST | 关注用户 |
| 关注 | `/api/follows/{id}` | DELETE | 取消关注 |
| 排行 | `/api/rankings/posts` | GET | 帖子排行榜 |
| 排行 | `/api/rankings/like` | GET | 点赞排行榜 |
| 签到 | `/api/sign/in` | POST | 用户签到 |
| 上传 | `/api/upload` | POST | 文件上传 |

---

## 数据库设计

### 核心表结构

| 表名 | 描述 | 主要字段 |
|:---|:---|:---|
| `user` | 用户表 | id, username, phone, password, avatar, bio |
| `post` | 帖子表 | id, user_id, title, content, like_count, comment_count |
| `comment` | 评论表 | id, post_id, user_id, content, like_count |
| `like_record` | 点赞记录表 | id, user_id, target_type, target_id |
| `follow` | 关注关系表 | id, user_id, follow_user_id |
| `notification` | 通知表 | id, user_id, type, content, is_read |

---

## 功能截图

> 待添加

---

## 开发计划

- [x] 用户注册/登录
- [x] 帖子 CRUD
- [x] 评论功能
- [x] 点赞功能（Redis）
- [x] 关注功能
- [x] 排行榜
- [x] 签到功能
- [x] 文件上传
- [ ] 通知系统（RabbitMQ）
- [ ] 密码 BCrypt 加密
- [ ] 全局异常处理
- [ ] 单元测试
- [ ] Docker 部署

---

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

---

## 许可证

本项目仅供学习交流使用。

---

## 联系方式

- GitHub: [@wzhwwwzzzhhh](https://github.com/wzhwwwzzzhhh)
