# 校园图书借阅管理系统

> 本文档为 `sysbook` 项目的结构化代码知识库，涵盖项目整体架构、模块职责、关键类与函数说明、依赖关系及运行方式等关键信息。

---

## 一、项目概览

| 项目属性 | 说明 |
| --- | --- |
| 项目名称 | sysbook（校园图书借阅管理系统） |
| 项目定位 | SpringBoot 实训项目 · 前后端完整可运行案例 |
| 构建工具 | Maven |
| 打包方式 | `war`（实际以 SpringBoot 内嵌 Tomcat 启动） |
| SpringBoot 版本 | 2.5.2 |
| 基础包名 | `com.org.example` |
| 启动入口 | [AssetsManagerApplication.java](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/AssetsManagerApplication.java) |

### 核心业务能力

1. **用户管理**：注册、登录、用户列表、逻辑删除。
2. **图书管理**：图书列表（按名称模糊查询）、详情、新增、修改、逻辑删除、库存更新。
3. **借阅管理**：借书（含库存校验与扣减）、还书（含库存回补）、借阅记录查询、当前用户借阅记录过滤。

---

## 二、技术栈与依赖关系

### 后端依赖（[pom.xml](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/pom.xml)）

| 依赖 | 版本 | 作用 |
| --- | --- | --- |
| `spring-boot-starter-parent` | 2.5.2 | SpringBoot 父工程，统一版本管理 |
| `spring-boot-starter-web` | 继承父工程 | Web MVC、内嵌 Tomcat、REST 支持 |
| `mybatis-spring-boot-starter` | 2.1.3 | MyBatis 整合 SpringBoot，提供 Mapper 扫描与 SQL 会话 |
| `druid` | 1.1.19 | 阿里巴巴数据库连接池 |
| `mysql-connector-java` | 8.0.31 | MySQL JDBC 驱动 |
| `lombok` | 1.18.12 | 通过注解生成 getter/setter/构造器，简化 POJO |
| `jquery`（webjars） | 3.6.0 | 前端通过 `webjars/jquery/3.6.0/jquery.js` 引入 |

### 前端技术

- 纯静态 HTML + 原生 CSS + jQuery 3.6.0（webjars 引入）
- 通过 `$.ajax` 调用后端 REST 接口，统一解析 `Result` 返回结构
- 登录态使用浏览器 `localStorage` 存储用户信息（`userInfo`）

### 依赖层次关系

```
前端 HTML (jQuery ajax)
        │  HTTP / JSON
        ▼
Controller 层  ──依赖──▶  Service 层(接口)  ──依赖──▶  ServiceImpl  ──依赖──▶  Mapper 接口
        │                                                                            │
        └────────────────── 统一返回 Result<T> ◀──────────────────────────────────┘
                                                                                   │
                                                                            MyBatis XML (SQL)
                                                                                   │
                                                                            MySQL (book_db)
```

---

## 三、项目目录结构

```
sysbook/
├── pom.xml                                  # Maven 构建配置
├── src/main/
│   ├── java/com/org/example/
│   │   ├── AssetsManagerApplication.java    # 启动类
│   │   ├── common/
│   │   │   └── Result.java                  # 统一响应封装
│   │   ├── controller/                      # 控制层
│   │   │   ├── BookInfoController.java
│   │   │   ├── BookBorrowController.java
│   │   │   └── SysUserController.java
│   │   ├── service/                         # 业务接口层
│   │   │   ├── BookInfoService.java
│   │   │   ├── BookBorrowService.java
│   │   │   ├── SysUserService.java
│   │   │   └── Impl/                        # 业务实现层
│   │   │       ├── BookInfoServiceImpl.java
│   │   │       ├── BookBorrowServiceImpl.java
│   │   │       └── SysUserServiceImpl.java
│   │   ├── mapper/                          # 数据访问层（MyBatis Mapper 接口）
│   │   │   ├── BookInfoMapper.java
│   │   │   ├── BookBorrowMapper.java
│   │   │   └── SysUserMapper.java
│   │   └── pojo/                            # 实体类
│   │       ├── BookInfo.java
│   │       ├── BookBorrow.java
│   │       └── SysUser.java
│   ├── resources/
│   │   ├── application.yml                  # SpringBoot 配置
│   │   ├── mapper/                          # MyBatis SQL 映射文件
│   │   │   ├── BookInfoMapperImpl.xml
│   │   │   ├── BookBorrowServiceMapperImpl.xml
│   │   │   └── SysUserMapperImpl.xml
│   │   └── static/                          # 前端静态页面
│   │       ├── index.html                   # 系统首页
│   │       ├── login.html                   # 登录页
│   │       ├── register.html                # 注册页
│   │       ├── bookList.html                # 图书列表（搜索+前端分页）
│   │       ├── bookedit.html                # 图书新增/编辑
│   │       ├── borrowlist.html              # 全部借阅记录
│   │       └── myborrow.html                # 我的借阅记录
│   └── webapp/                              # 遗留 Web 资源（基本未使用）
│       ├── WEB-INF/web.xml
│       └── index.jsp
└── target/                                  # 编译输出（构建产物）
```

---

## 四、整体架构

### 4.1 分层架构

项目采用经典的 **四层架构**，各层职责清晰、单向依赖：

| 层级 | 包路径 | 职责 | 关键技术 |
| --- | --- | --- | --- |
| 表现层（Controller） | `com.org.example.controller` | 接收 HTTP 请求、参数校验、调用 Service、封装 `Result` 返回 | `@RestController`、`@RequestMapping` |
| 业务层（Service） | `com.org.example.service` + `service.Impl` | 业务逻辑编排（如借阅库存校验）、事务边界 | `@Service` |
| 持久层（Mapper） | `com.org.example.mapper` | 数据库 CRUD，与 SQL XML 映射 | MyBatis `@Mapper` |
| 模型层（POJO） | `com.org.example.pojo` | 与数据库表对应的实体对象 | Lombok `@Data` |

### 4.2 请求处理流程（以借书为例）

```
bookList.html 点击「借阅」
   │  POST /borrow/borrow  body: {userId, bookId}
   ▼
BookBorrowController.borrow()
   │  1. bookInfoService.getById(bookId)         查图书
   │  2. 校验 stock <= 0  →  返回「库存不足」
   │  3. stock - 1，bookInfoService.updateStock()  扣库存
   │  4. bookBorrowService.add()                  写借阅记录
   ▼
Result.success("借阅成功")  →  前端 alert + 刷新列表
```

### 4.3 配置说明（[application.yml](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/application.yml)）

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource      # 使用 Druid 连接池
    url: jdbc:mysql://localhost:3306/book_db?useSSL=false&serverTimezone=UTC
    username: root
    password: root

mybatis:
  type-aliases-package: com.org.example.pojo          # 实体类别名包
  mapper-locations: classpath:mapper/*.xml            # SQL 映射文件位置
  configuration:
    map-underscore-to-camel-case: true                # 下划线转驼峰

debug: true
```

> 默认服务端口为 `8080`（未在 yml 中显式配置，采用 SpringBoot 默认值）。前端 ajax 中硬编码了 `http://localhost:8080`。

---

## 五、数据库设计

数据库名：`book_db`，共 3 张业务表。

### 5.1 `book_info`（图书信息表）

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | int | 主键，自增 |
| book_name | varchar | 书名 |
| author | varchar | 作者 |
| category | varchar | 分类 |
| publish_house | varchar | 出版社 |
| stock | int | 库存数量 |
| book_desc | varchar | 简介 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | int | 逻辑删除标识（0 未删除 / 1 已删除） |

### 5.2 `book_borrow`（借阅记录表）

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | int | 主键，自增 |
| user_id | int | 借阅用户 ID |
| book_id | int | 图书 ID |
| borrow_time | datetime | 借阅时间（建议数据库默认 `now()`） |
| return_time | datetime | 归还时间（还书时由 SQL 写入 `now()`） |
| status | varchar | 借阅状态（`已归还` / 未归还） |

### 5.3 `sys_user`（系统用户表）

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | int | 主键，自增 |
| username | varchar | 账号 |
| password | varchar | 密码（明文存储） |
| nickname | varchar | 昵称 |
| phone | varchar | 手机号 |
| head_img | varchar | 头像 |
| role | varchar | 角色（注册时默认 `student`） |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | int | 逻辑删除标识（0 未删除 / 1 已删除） |

---

## 六、关键类与函数说明

### 6.1 启动类

#### `AssetsManagerApplication`（[源码](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/AssetsManagerApplication.java)）

```java
@SpringBootApplication
@EnableCaching
public class AssetsManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssetsManagerApplication.class, args);
    }
}
```

- `@SpringBootApplication`：开启自动装配与组件扫描（扫描范围：`com.org.example` 及其子包）。
- `@EnableCaching`：开启缓存支持（当前业务未实际使用缓存注解，属预留配置）。
- 说明：类名 `AssetsManager` 与项目名 `sysbook` 不一致，疑似从模板复用，但功能正确。

### 6.2 通用层

#### `Result<T>`（[源码](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/common/Result.java)）

统一响应封装类，所有接口返回该结构。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| code | int | 状态码（200 成功 / 500 失败） |
| msg | String | 提示信息 |
| data | T | 业务数据 |

| 静态方法 | 说明 |
| --- | --- |
| `Result.success(T data)` | 返回 `code=200, msg="操作成功", data` |
| `Result.error(String msg)` | 返回 `code=500, msg, data=null` |

### 6.3 实体类（POJO）

均使用 Lombok `@Data` 自动生成 getter/setter/toString 等。

| 类 | 对应表 | 关键字段 |
| --- | --- | --- |
| [BookInfo](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/pojo/BookInfo.java) | book_info | id, bookName, author, category, publishHouse, stock, bookDesc, deleted |
| [BookBorrow](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/pojo/BookBorrow.java) | book_borrow | id, userId, bookId, borrowTime, returnTime, status |
| [SysUser](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/pojo/SysUser.java) | sys_user | id, username, password, nickname, phone, headImg, role, deleted |

### 6.4 控制层（Controller）

#### `SysUserController`（`/user`）（[源码](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/controller/SysUserController.java)）

| 方法 | HTTP | 路径 | 说明 |
| --- | --- | --- | --- |
| `login` | POST | `/user/login` | 登录，成功返回用户对象（存入前端 localStorage） |
| `register` | POST | `/user/register` | 注册 |
| `list` | GET | `/user/list` | 查询所有用户 |
| `delete` | DELETE | `/user/delete/{id}` | 逻辑删除用户 |

#### `BookInfoController`（`/book`）（[源码](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/controller/BookInfoController.java)）

| 方法 | HTTP | 路径 | 说明 |
| --- | --- | --- | --- |
| `list` | GET | `/book/list?bookName=` | 图书列表，支持书名模糊查询 |
| `getById` | GET | `/book/get/{id}` | 根据 ID 查图书 |
| `add` | POST | `/book/add` | 新增图书 |
| `update` | PUT | `/book/update` | 修改图书 |
| `delete` | DELETE | `/book/delete/{id}` | 逻辑删除图书 |

#### `BookBorrowController`（`/borrow`）（[源码](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/controller/BookBorrowController.java)）

| 方法 | HTTP | 路径 | 说明 |
| --- | --- | --- | --- |
| `list` | GET | `/borrow/list` | 查询全部借阅记录 |
| `borrow` | POST | `/borrow/borrow` | 借书：校验库存→扣减库存→写借阅记录 |
| `back` | POST | `/borrow/back/{id}` | 还书：回补库存→更新借阅状态为「已归还」 |

> `borrow` 与 `back` 方法是**跨表业务**的典型，同时依赖 `BookInfoService` 与 `BookBorrowService`，体现了 Controller 层对多 Service 的编排职责。

### 6.5 业务层（Service）

Service 接口与实现一一对应，Impl 通过 `@Service("/beanName")` 注册，`@Autowired` 注入 Mapper，方法体直接委托 Mapper 执行。

| 接口 | 实现 | 主要方法 |
| --- | --- | --- |
| [BookInfoService](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/service/BookInfoService.java) | [BookInfoServiceImpl](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/service/Impl/BookInfoServiceImpl.java) | list, getById, add, update, delete, updateStock |
| [BookBorrowService](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/service/BookBorrowService.java) | [BookBorrowServiceImpl](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/service/Impl/BookBorrowServiceImpl.java) | list, add, returnBook, getById |
| [SysUserService](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/service/SysUserService.java) | [SysUserServiceImpl](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/service/Impl/SysUserServiceImpl.java) | login, register, list, getById, delete, update |

> 业务逻辑较薄，Service 层主要承担「接口隔离 + 未来扩展点」的作用；真正业务编排集中在 `BookBorrowController`（库存校验与扣减/回补）。

### 6.6 持久层（Mapper 接口 + XML）

三个 Mapper 接口均标注 `@Mapper` + `@Repository`，SQL 写在 `resources/mapper/*.xml` 中。

#### `BookInfoMapper`（[XML](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/mapper/BookInfoMapperImpl.xml)）

| SQL id | 类型 | 说明 |
| --- | --- | --- |
| `list` | select | 带 `<where>`+`<if>` 动态条件，过滤 `deleted=0`，支持书名 LIKE |
| `getById` | select | 按 id 查询（且 deleted=0） |
| `add` | insert | 新增，`useGeneratedKeys=true` 回填主键 |
| `update` | update | `<set>` 动态更新全字段 |
| `delete` | update | 逻辑删除：`set deleted=1` |
| `updateStock` | update | 仅更新库存字段 |

#### `BookBorrowMapper`（[XML](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/mapper/BookBorrowServiceMapperImpl.xml)）

| SQL id | 类型 | 说明 |
| --- | --- | --- |
| `list` | select | 查全部借阅记录 |
| `add` | insert | 插入 `user_id, book_id`（borrow_time/status 依赖数据库默认值） |
| `returnBook` | update | 归还：`set return_time=now(), status='已归还'` |
| `getById` | select | 按借阅记录 id 查询 |

#### `SysUserMapper`（[XML](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/mapper/SysUserMapperImpl.xml)）

| SQL id | 类型 | 说明 |
| --- | --- | --- |
| `login` | select | 按账号+密码查询（明文比对） |
| `register` | insert | 插入用户，角色硬编码为 `student` |
| `list` | select | 查全部用户 |
| `getById` | select | 按 id 查询 |
| `delete` | update | 逻辑删除：`set deleted=1` |
| `updateUser` | update | 修改用户信息 |

> ⚠️ **已知缺陷**：`SysUserMapper` 接口方法名为 `update`，但 XML 中 `<update id="updateUser">`，二者 id 不一致，调用 `SysUserService.update()` 会因找不到对应语句而抛出 MyBatis 异常。修复方式：将 XML 的 `id="updateUser"` 改为 `id="update"`。

---

## 七、前端模块说明

所有页面位于 [src/main/resources/static/](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/)，统一引入 `webjars/jquery/3.6.0/jquery.js`。

| 页面 | 功能 | 调用接口 |
| --- | --- | --- |
| [login.html](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/login.html) | 登录，成功后 `localStorage` 存 `userInfo` 并跳首页 | `POST /user/login` |
| [register.html](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/register.html) | 注册（账号/密码/昵称/手机号） | `POST /user/register` |
| [index.html](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/index.html) | 系统首页，含导航与登录态校验 | 无（仅读 localStorage） |
| [bookList.html](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/bookList.html) | 图书列表，搜索 + **前端切片分页**（每页 10 条） | `GET /book/list`、`DELETE /book/delete/{id}`、`POST /borrow/borrow` |
| [bookedit.html](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/bookedit.html) | 图书新增/编辑（根据 URL `?id=` 区分） | `GET /book/get/{id}`、`POST /book/add`、`PUT /book/update` |
| [borrowlist.html](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/borrowlist.html) | 全部借阅记录，支持归还操作 | `GET /borrow/list`、`POST /borrow/back/{id}` |
| [myborrow.html](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/static/myborrow.html) | 当前登录用户的借阅记录（前端 `filter` 过滤） | `GET /borrow/list`、`POST /borrow/back/{id}` |

**前端登录态机制**：进入受保护页面时读取 `localStorage.getItem("userInfo")`，为空则强制跳转 `login.html`；退出登录时清除该键。

---

## 八、REST API 接口总览

| 模块 | 方法 | 路径 | 请求体/参数 | 返回 data |
| --- | --- | --- | --- | --- |
| 用户 | POST | `/user/login` | `{username, password}` | `SysUser` |
| 用户 | POST | `/user/register` | `{username, password, nickname, phone}` | `"注册成功"` |
| 用户 | GET | `/user/list` | - | `List<SysUser>` |
| 用户 | DELETE | `/user/delete/{id}` | path: id | `"逻辑删除成功"` |
| 图书 | GET | `/book/list?bookName=` | query: bookName（可选） | `List<BookInfo>` |
| 图书 | GET | `/book/get/{id}` | path: id | `BookInfo` |
| 图书 | POST | `/book/add` | `BookInfo` | `"添加成功"` |
| 图书 | PUT | `/book/update` | `BookInfo` | `"修改成功"` |
| 图书 | DELETE | `/book/delete/{id}` | path: id | `"逻辑删除成功"` |
| 借阅 | GET | `/borrow/list` | - | `List<BookBorrow>` |
| 借阅 | POST | `/borrow/borrow` | `{userId, bookId}` | `"借阅成功"` / 错误信息 |
| 借阅 | POST | `/borrow/back/{id}` | path: id | `"归还成功"` |

所有响应统一为：`{ "code": 200|500, "msg": "...", "data": ... }`

---

## 九、项目运行方式

### 9.1 环境要求

- JDK 8+（SpringBoot 2.5.2 推荐 JDK 8/11）
- Maven 3.6+
- MySQL 8.x（驱动为 `mysql-connector-java 8.0.31`）

### 9.2 数据库准备

1. 创建数据库 `book_db`。
2. 根据第五章字段定义建表：`book_info`、`book_borrow`、`sys_user`。
   - `book_borrow` 的 `borrow_time` 建议设默认值 `now()`，`status` 建议默认值如 `借阅中`（与还书 SQL 的 `已归还` 区分）。
3. 建议预置一条管理员账号以便登录测试：
   ```sql
   INSERT INTO sys_user(username, password, role, deleted) VALUES ('admin', 'admin', 'admin', 0);
   ```

### 9.3 配置数据库连接

编辑 [application.yml](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/application.yml)，按实际环境修改：

```yaml
spring.datasource.url: jdbc:mysql://localhost:3306/book_db?...
spring.datasource.username: <你的用户名>
spring.datasource.password: <你的密码>
```

### 9.4 启动方式

**方式一：IDEA 直接运行**
- 打开 [AssetsManagerApplication.java](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/java/com/org/example/AssetsManagerApplication.java)，运行 `main` 方法。

**方式二：Maven 命令**
```bash
mvn spring-boot:run
```

**方式三：打包运行**
```bash
mvn clean package
java -jar target/sysbook.war
```

### 9.5 访问系统

启动成功后，浏览器访问：

```
http://localhost:8080/login.html
```

默认账号（需按 9.2 预置）：`admin / admin`。

---

## 十、已知问题与改进建议

| 序号 | 问题 | 位置 | 建议 |
| --- | --- | --- | --- |
| 1 | Mapper 方法名与 XML id 不一致，`SysUserService.update()` 无法执行 | [SysUserMapperImpl.xml](file:///c:/Users/zhoutao/Desktop/项目/sysbook/sysbook/src/main/resources/mapper/SysUserMapperImpl.xml#L33) `id="updateUser"` | 改为 `id="update"` |
| 2 | 密码明文存储与比对 | login / register SQL | 引入 MD5/BCrypt 加密 |
| 3 | 登录态仅靠 localStorage，无服务端会话/Token | 全局前端 | 引入 JWT 或 Session 鉴权 |
| 4 | 借阅记录插入未显式写入 `borrow_time`、`status` | `BookBorrowMapper.add` | 数据库设默认值或在 SQL 中显式写入 |
| 5 | 前端 ajax 硬编码 `http://localhost:8080` | bookList/login/register 等 | 改用相对路径，便于部署 |
| 6 | 分页为前端切片（一次性拉全量） | bookList.html | 改为后端分页（PageHelper） |
| 7 | 缺少全局异常处理与参数校验 | Controller 层 | 增加 `@RestControllerAdvice` + `@Valid` |
| 8 | 启动类名 `AssetsManagerApplication` 与项目语义不符 | 启动类 | 可重命名为 `SysBookApplication` |
| 9 | `debug: true` 生产环境不宜开启 | application.yml | 按环境拆分 profile |

---

## 十一、术语速查

| 术语 | 含义 |
| --- | --- |
| 逻辑删除 | 通过 `deleted` 字段标记删除，不真正删除数据行（查询时 `deleted=0`） |
| 动态 SQL | MyBatis 的 `<where>`/`<if>`/`<set>` 标签，按参数动态拼接 SQL |
| webjars | 以 jar 形式打包前端依赖（如 jQuery），通过 `/webjars/...` 路径访问 |
| `Result<T>` | 本项目统一的 HTTP 响应封装结构 |
| `@MapperScan` | 本项目未使用，改为在每个 Mapper 接口上标注 `@Mapper` 实现扫描 |
