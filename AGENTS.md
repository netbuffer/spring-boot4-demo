# 开发规范

本文档供 **AI 编码助手** 阅读。

## 环境

- Java 21、Maven 3.9.6、Spring Boot 4.1.1
- 本地端口：`18297`（`http/http-client.env.json` 的 `dev`）
- Docker：`SERVER_PORT=8080`
- 可选根目录 `sb4d.env`（已 gitignore）。启动必须能解析到 `MY_ENV_VAR1`

## 命令

```bash
mvn spring-boot:run
mvn test
mvn clean package -DskipTests
```

产物：`target/spring-boot4-demo.jar`。CI 使用上面的 package 命令（Dragonwell 21）。

## 目录

```
src/main/java/cn/netbuffer/spring/boot4/demo/
├── SpringBoot4DemoApplication.java   # 加载 sb4d.env
├── api/ + api/dto/                   # @HttpExchange 契约
├── client/                           # 出站客户端（多为 Mono）
├── config/                           # HttpClientConfig 等
└── controller/
src/test/java/…                       # SpringBootTest + WebTestClient
http/                                 # IntelliJ HTTP Client 示例
```

## 约定

- 命名：`*Controller`、`*Client`、`*Api`、`*Config`
- 新 HttpExchange API → 在 `controller/` 实现；新 Client → 在 `HttpClientConfig` 注册
- 测试：`@SpringBootTest(webEnvironment = RANDOM_PORT)` + `WebTestClient`；客户端单测放 `client/`
- 保持 demo 规模；非用户要求不上安全体系/复杂持久化
- 不要随意升 Spring Boot / Java 大版本；无必要不改包结构

## 联改

| 改动 | 同步更新 |
|------|----------|
| 对外 HTTP 接口 | `http/spring-boot4-demo.http`、README API 表 |
| 端口 | `http/http-client.env.json`、Dockerfile、`docker-compose.yml`、README |
| dotenv / `MY_ENV_VAR1` | `application.yaml`、README、测试 |
| `app.ip-query.base-url` | `APP_IP_QUERY_BASE_URL`、`HttpClientConfig`、README |

## 提交

Conventional Commits：`<type>: <说明>`（英文祈使句、小写开头、无句末句号）。

类型：`feat` `fix` `docs` `refactor` `test` `chore` `perf`

- 一事一提交；相关 README/HTTP 改动可同提交
- 用户未要求时禁止 `git commit` / `git push`
- 禁止提交 `*.env` 与密钥

## CI / 发布

`.github/workflows/build.yml`：推送/PR 到 `master`/`main` → 构建并上传 jar；标签 `v*` → GitHub Release + `ghcr.io`。未经要求勿改 workflow 权限或触发条件。

## 安全与边界

- 不把密钥写入仓库或提交
- 不把 Agent 专用规则写进 `README.md`
- 改动保持聚焦，禁止顺手大重构
