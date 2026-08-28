package cn.netbuffer.spring.boot4.demo.client;

import cn.netbuffer.spring.boot4.demo.api.dto.GreetingRequest;
import cn.netbuffer.spring.boot4.demo.api.dto.GreetingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingClientTest {

    @Autowired
    private GreetingClient greetingClient;

    @Test
    void helloViaDeclarativeClient() {
        StepVerifier.create(greetingClient.hello())
                .assertNext(body -> assertThat(body).isEqualTo("Hello, HttpExchange!"))
                .verifyComplete();
    }

    @Test
    void helloWithNameViaDeclarativeClient() {
        StepVerifier.create(greetingClient.hello("Spring"))
                .assertNext(body -> assertThat(body).isEqualTo("Hello, Spring!"))
                .verifyComplete();
    }

    @Test
    void postGreetViaDeclarativeClient() {
        StepVerifier.create(greetingClient.greet(new GreetingRequest("World")))
                .assertNext(response -> {
                    assertThat(response.message()).isEqualTo("Hello, World!");
                    assertThat(response.timestamp()).isNotBlank();
                })
                .verifyComplete();
    }

    @Test
    void searchViaDeclarativeClient() {
        StepVerifier.create(greetingClient.search("spring"))
                .assertNext(results -> assertThat(results).containsExactly("Spring"))
                .verifyComplete();
    }
}
