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
- 🏥 Spring Boot Actuator 健康检查

## 🚀 快速开始

### 📥 下载构建产物

前往 [GitHub Releases](../../releases) 页面下载最新的 `spring-boot4-demo.jar`。

### ▶️ 直接运行

```bash
java -jar spring-boot4-demo.jar
```

应用默认监听 **8080** 端口，可通过环境变量 `SERVER_PORT` 修改。

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
| `GET /actuator/health` | 💚 健康检查 |

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
```

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `SERVER_PORT` | `8080` | 服务监听端口 |
| `MY_ENV_VAR1` | - | dotenv 配置项，映射到 `sb4d.my_env_var1` |
| `APP_IP_QUERY_BASE_URL` | `https://api.bilibili.com` | HTTP Interface Client 请求基地址 |
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
