# 🌱 spring-boot4-demo

[![Java](https://img.shields.io/badge/java-21%2B-orange)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)](https://spring.io/projects/spring-boot)
[![dotenv-java](https://img.shields.io/badge/dotenv--java-3.2.0-blue)](https://github.com/cdimascio/dotenv-java)
[![Docker](https://img.shields.io/badge/Docker-Dragonwell%2021-blue)](https://github.com/dragonwell-project/dragonwell21)

> Spring Boot 4 示例项目，基于 **Spring Boot 4.1.1** + **Java 21** 构建。

演示基础 Web 开发、dotenv 环境变量加载、Server-Sent Events (SSE) 实时推送，以及基于 `@HttpExchange` 的声明式 HTTP 客户端等核心能力。

## ✨ 特性

- 🌐 基础 REST 接口
- 🔐 dotenv-java 环境变量加载与读取
- 📡 Server-Sent Events 实时数据流推送
- 🔌 声明式 HTTP Interface Client（`@HttpExchange` + WebClient）
- 🕒 Java 时间类型（`LocalTime` / `LocalDate` / `LocalDateTime`）序列化演示
- 🧩 `SmartInitializingSingleton` 启动时收集 `PayHandler` 建渠道分发表
- 🏥 Spring Boot Actuator 健康检查

## 🚀 快速开始

### 📥 下载构建产物

前往 [GitHub Releases](../../releases) 页面下载最新的 `spring-boot4-demo.jar`。

### ▶️ 直接运行

```bash
java -jar spring-boot4-demo.jar
```

本地默认端口见 `application.yaml` 中的 **18297**；Docker 示例使用 **8080**。均可通过环境变量 `SERVER_PORT` 覆盖。

### 🐳 Docker 运行

先构建 jar 包，再构建镜像：

```bash
mvn clean package -DskipTests
docker build -t spring-boot4-demo .
```

```bash
docker run -it --rm -p 8080:8080 spring-boot4-demo
```

自定义 JVM 参数和时区：

```bash
docker run -it --rm -p 8080:8080 \
  -e TZ=Asia/Shanghai \
  -e JAVA_OPTS="-XX:+PrintCommandLineFlags" \
  spring-boot4-demo
```

创建 `v*` 版本标签后，GitHub Actions 还会自动构建并推送镜像到 GitHub Container Registry：

```bash
# GitHub Container Registry（匿名可拉取）
docker pull ghcr.io/netbuffer/spring-boot4-demo:latest
```

```bash
docker run -it --rm -p 8080:8080 ghcr.io/netbuffer/spring-boot4-demo:latest
```

### 🐙 Docker Compose

```bash
mvn clean package -DskipTests
docker compose up -d
```

## 📡 API 接口

| 接口 | 说明 |
|------|------|
| `GET /hello` | 👋 返回问候消息 |
| `GET /dotenv/list` | 📋 列出 `sb4d.env` 中的所有环境变量 |
| `GET /dotenv/myEnvVar1` | 🔍 返回配置项 `sb4d.my_env_var1` 的值 |
| `GET /sse/data` | 📡 SSE 流，每秒推送当前时间戳 |
| `GET /sse/data/retry` | 🔄 带重试配置的 SSE（重试间隔 5 秒） |
| `GET /api/greeting` | 🔌 HttpExchange 问候 |
| `GET /api/greeting/{name}` | 🔌 带路径参数的问候 |
| `POST /api/greeting` | 🔌 JSON 问候（body: `{ "name": "..." }`） |
| `GET /api/greeting/search?keyword=` | 🔌 关键字搜索 |
| `GET /demo/http-exchange/local/hello` | 🔌 通过 GreetingClient 调用本地 API |
| `GET /demo/http-exchange/local/hello/{name}` | 🔌 本地 Client 路径参数演示 |
| `GET /demo/http-exchange/local/greet/{name}` | 🔌 本地 Client POST 问候演示 |
| `GET /demo/http-exchange/local/search?keyword=` | 🔌 本地 Client 搜索演示 |
| `GET /demo/http-exchange/public/ip` | 🔌 外部 IP/区域查询 Client 演示 |
| `GET /datetime/test/localtime` | 🕒 返回 `LocalTime` |
| `GET /datetime/test/localdate` | 🕒 返回 `LocalDate` |
| `GET /datetime/test/localdatetime` | 🕒 返回 `LocalDateTime` |
| `GET /demo/virtual-thread/info` | 🧵 查看当前请求是否运行在虚拟线程上 |
| `GET /demo/virtual-thread/parallel?tasks=&sleepMs=` | 🧵 用虚拟线程并行执行阻塞任务 |
| `GET /demo/text-block/basic` | 📄 文本块 vs `\n` 经典写法 |
| `GET /demo/text-block/html?title=` | 📄 文本块拼 HTML |
| `GET /demo/text-block/json?name=&age=` | 📄 文本块拼 JSON |
| `GET /demo/text-block/sql?keyword=` | 📄 文本块拼 SQL |
| `GET /demo/text-block/escape` | 📄 文本块转义（`\"` / `\s`） |
| `GET /demo/pay/{channel}?amount=` | 💳 走启动时建好的渠道表（`alipay` / `wechat`） |
| `GET /demo/pay/registry` | 🧩 渠道表 + 三个时机的执行记录 |
| `GET /actuator/health` | 💚 健康检查 |

更多可直接运行的请求样本见 [`http/spring-boot4-demo.http`](http/spring-boot4-demo.http)。

### 🧩 SmartInitializingSingleton

一句话：**该建的对象全建完了，而网站还没开门接单**——就在这个窗口里做一次性的全局动作。

`lifecycle/PayDispatcher` 就在这一步把所有 `PayHandler` 收进 `channel -> handler` 表，顺手校验渠道不重复、不为空——有问题进程直接启动失败，而不是等第一笔支付才报错。以后加新渠道只多写一个 `@Component` 类，`PayDispatcher` 一行不改。

启动日志会直接把三个时机打出来。下面是本机一次真实启动（端口 18300）的节选，可以看到端口就开在 2/3 与 3/3 之间：

```text
[lifecycle] 1/3 @PostConstruct              (JVM +1961ms) -> 我刚建好，支付渠道只出生了 0 个（一共 2 个）—— 别的对象还在后面排队，这时候清点必然数漏
[lifecycle] 2/3 afterSingletonsInstantiated (JVM +2423ms) -> 所有对象都建完了，而网站还没开门—— 2 个渠道一次登记齐 [alipay, wechat]，就该在这步干活
Tomcat started on port 18300 (http) with context path '/'        <- 端口在这里开
Started SpringBoot4DemoApplication in 2.142 seconds (process running for 2.476)
[lifecycle] 3/3 ApplicationRunner           (JVM +2533ms) -> 网站已经开门迎客，可能早就有请求打进来—— 这时才登记就晚了，本步只是回头核对一下（表里已有 2 个渠道）
```

不想翻控制台就访问 `GET /demo/pay/registry`，字段含义：

| 字段 | 含义 |
|------|------|
| `seenAtPostConstruct` | `@PostConstruct` 时**已创建**的 Handler 数；对象还在后面排队，**不能在这建表**（具体值取决于创建顺序，只当观察值） |
| `knownHandlerBeans` | 同一时刻能看到多少个 Handler 的 **Bean 定义**（定义全了，实例还没全） |
| `registeredAtCallback` | `afterSingletonsInstantiated()` 里实际建表的数量；全量 ✅ |
| `readyAtRunner` | `ApplicationRunner` 才确认就绪；已经晚了，门开着、可能已有流量 |
| `phases` | 上面那三行日志原文，按执行顺序排列 |

其中 1/3 与 3/3 只是对照组，删掉不影响功能；真业务代码里 `PayDispatcher` 只需要实现 `SmartInitializingSingleton` 一个接口。

## 🔨 构建

项目使用 [Dragonwell JDK 21](https://github.com/dragonwell-project/dragonwell21) 构建，推荐通过 GitHub Actions 自动完成。

```bash
mvn clean package -DskipTests
```

构建产物位于 `target/spring-boot4-demo.jar`。

本地开发运行：

```bash
mvn spring-boot:run
```

## 🔄 CI/CD

推送至 `master` / `main` 分支或创建 `v*` 标签时，GitHub Actions 将自动：

1. 🐉 使用 Dragonwell JDK 21 编译打包
2. 📦 上传构建产物（可在 Actions 页面登录下载）
3. 🏷️ 创建 `v*` 标签时，自动发布 jar 包到 GitHub Releases
4. 🐳 创建 `v*` 标签时，自动构建镜像并推送到 ghcr.io

手动触发 Release：

```bash
git tag v1.0.0 && git push origin v1.0.0
```

## ⚙️ 环境变量

在项目根目录创建 `sb4d.env` 文件，应用启动时自动加载其中的键值对：

```properties
MY_ENV_VAR1=spring-boot4-demo-value
```

并通过 `application.yaml` 映射为 Spring 配置：

```yaml
sb4d:
  my_env_var1: ${MY_ENV_VAR1}

app:
  ip-query:
    base-url: ${APP_IP_QUERY_BASE_URL:https://api.bilibili.com}
```

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `SERVER_PORT` | `18297`（本地 yaml）/ `8080`（Docker） | 服务监听端口 |
| `MY_ENV_VAR1` | - | 必填；映射到 `sb4d.my_env_var1`（可由 `sb4d.env` 提供） |
| `APP_IP_QUERY_BASE_URL` | `https://api.bilibili.com` | `IpQueryClient` 请求基地址（对应 `app.ip-query.base-url`） |
| `JAVA_OPTS` | - | JVM 启动参数（Docker 运行时） |
| `SPRING_PROFILES_ACTIVE` | - | 激活的 Spring Profile |

## 🧰 技术栈

- ☕ Java 21 (Dragonwell)
- 🍃 Spring Boot 4.1.1
- 🌐 Spring Web MVC
- ⚡ Spring WebFlux / WebClient
- 🏥 Spring Boot Actuator
- 📦 dotenv-java 3.2.0
- 🧅 Lombok
