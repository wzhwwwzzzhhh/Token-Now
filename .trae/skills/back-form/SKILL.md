---
name: back-form
description: "在处理后端代码时
在处理前后端接口交互时"
---

# 后端规范（Spring Boot + MyBatis-Plus）

## 技术栈
- Java 11 或 17
- Spring Boot 2.7.x
- MyBatis-Plus 3.5.x
- MySQL 8.0（驱动：mysql-connector-java）
- Redis（Spring Data Redis + Redisson）
- RabbitMQ（Spring AMQP）
- JWT（jjwt）
- 工具：Lombok、Hutool

## 代码规范
1. **包结构**：`com.forum`
   - `controller`：控制器，接收请求，返回 Result
   - `service`：业务接口及实现（`service.impl`）
   - `mapper`：MyBatis-Plus Mapper 接口
   - `entity`：数据库实体类（使用 MyBatis-Plus 注解）
   - `dto`：数据传输对象（请求/响应）
   - `config`：配置类（Redis、MQ、JWT等）
   - `utils`：工具类
   - `common`：通用类（如 Result、PageResult）
2. **实体类**：使用 `@TableName`、`@TableId`、`@TableField`，禁用逻辑删除（可根据需要开启）。
3. **Controller**：
   - 使用 `@RestController` 和 `@RequestMapping("/api/xxx")`
   - 方法返回 `Result<T>` 统一格式（code, message, data）
   - 分页返回 `PageResult<T>`
4. **Service**：
   - 接口 + 实现类（`@Service`）
   - 事务注解 `@Transactional(rollbackFor = Exception.class)`
5. **Mapper**：继承 `BaseMapper<T>`，复杂查询写在 XML 或使用 MyBatis-Plus QueryWrapper。
6. **依赖注入**：使用构造器注入（推荐）或 `@Autowired`。
7. **日志**：使用 `@Slf4j`。
8. **异常处理**：全局异常处理器 `@RestControllerAdvice`，统一返回错误信息。

## 数据库规范
- 表名：小写，下划线分隔（如 `user`, `post`, `comment`）。
- 字段名：小写，下划线分隔。
- 主键：`id` BIGINT AUTO_INCREMENT。
- 必备字段：`create_time` DATETIME、`update_time` DATETIME（自动更新）。
- 外键：逻辑关联，不使用数据库外键约束。

## API 设计规范
- RESTful 风格。
- 请求方法：GET（查询）、POST（创建）、PUT（更新）、DELETE（删除）。
- 路径命名：复数名词，如 `/api/users`, `/api/posts`。
- 分页参数：`page`（页码，默认1）、`size`（每页条数，默认10）。
- 认证：JWT token 放在 Header `Authorization: Bearer <token>`。
- 响应格式：
  ```json
  { "code": 200, "message": "success", "data": {} }