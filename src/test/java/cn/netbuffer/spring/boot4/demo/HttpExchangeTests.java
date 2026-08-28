package cn.netbuffer.spring.boot4.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpExchangeTests {

    @LocalServerPort
    private int port;

    private WebTestClient createClient() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void helloEndpointReturnsGreeting() {
        WebTestClient client = createClient();

        client.get()
                .uri("/hello")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isEqualTo("Hello, Spring Boot 4!"));
    }

    @Test
    void dotenvListEndpointReturnsArray() {
        WebTestClient client = createClient();

        client.get()
                .uri("/dotenv/list")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0]").exists();
    }

    @Test
    void dotenvMyEnvVar1ReturnsValue() {
        WebTestClient client = createClient();

        client.get()
                .uri("/dotenv/myEnvVar1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isEqualTo("spring-boot4-demo-value"));
    }

    @Test
    void sseDataRetryReturnsRetryHeader() {
        WebTestClient client = createClient();

        client.get()
                .uri("/sse/data/retry")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/event-stream;charset=UTF-8")
                .expectBody(String.class)
                .value(body -> {
                    assertThat(body).contains("retry: 5000");
                    assertThat(body).contains("data:");
                });
    }

    @Test
    void greetingApiHelloEndpoint() {
        WebTestClient client = createClient();

        client.get()
                .uri("/api/greeting")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isEqualTo("Hello, HttpExchange!"));
    }

    @Test
    void greetingApiHelloWithNameEndpoint() {
        WebTestClient client = createClient();

        client.get()
                .uri("/api/greeting/Spring")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isEqualTo("Hello, Spring!"));
    }

    @Test
    void greetingApiSearchEndpoint() {
        WebTestClient client = createClient();

        client.get()
                .uri("/api/greeting/search?keyword=mono")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0]").isEqualTo("Mono");
    }

    @Test
    void demoControllerLocalHelloEndpoint() {
        WebTestClient client = createClient();

        client.get()
                .uri("/demo/http-exchange/local/hello")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isEqualTo("Hello, HttpExchange!"));
    }

    @Test
    void actuatorHealthEndpoint() {
        WebTestClient client = createClient();

        client.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }
}