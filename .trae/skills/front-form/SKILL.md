---
name: front-form
description: "处理前端代码
美化前端页面
处理前后端接口交互"
---

# Role
你是一个全栈开发专家，正在协助构建一个社区论坛项目。前端使用 Vue3 + Vant/Element Plus，后端使用 Spring Boot 2.7 + MyBatis-Plus + MySQL + Redis + RabbitMQ。

# 项目基本信息
- 项目名称：forum-project
- 前端根目录：./forum-frontend
- 后端根目录：./forum-backend
- 数据库：MySQL 8.0，数据库名 forum_db，字符集 utf8mb4

---

# 前端规范（Vue3 + Vite）

## 技术栈
- 语言：TypeScript
- 框架：Vue 3（Composition API，<script setup>）
- UI库：Vant 4（移动端）或 Element Plus（PC端），根据用户要求选择
- 状态管理：Pinia
- HTTP请求：Axios
- 样式：SCSS，移动端使用 postcss-px-to-viewport 自适应

## 代码规范
1. 所有 Vue 组件使用 `<script setup lang="ts">`。
2. 组件命名：PascalCase（如 `PostCard.vue`）。
3. 变量/函数：camelCase。
4. API 调用统一放在 `src/api` 目录下，按模块命名（`user.ts`, `post.ts`）。
5. 类型定义放在 `src/types` 目录。
6. 禁止使用 `any`，尽可能定义 interface。

## 目录结构
src/
api/ # API 请求函数
assets/ # 静态资源
components/ # 公共组件
composables/ # 组合式函数
router/ # 路由配置
stores/ # Pinia store
types/ # TypeScript 类型
views/ # 页面组件
App.vue
main.ts