package cn.netbuffer.spring.boot4.demo.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PayDispatcherTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PayDispatcher dispatcher;

    private WebTestClient createClient() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void callbackCollectedEveryHandlerBean() {
        // registeredAtCallback 是全量；seenAtPostConstruct 取决于 Bean 创建顺序，只当观察值，不做断言
        assertThat(dispatcher.report())
                .containsEntry("registeredAtCallback", 2)
                .containsEntry("readyAtRunner", true)
                .hasEntrySatisfying("channels", channels -> assertThat(channels.toString()).contains("alipay", "wechat"));
    }

    @Test
    void lifecyclePhasesAreLoggedInOrder() {
        List<String> phases = dispatcher.phases();
        assertThat(phases).hasSize(3);
        assertThat(phases.get(0)).contains("1/3").contains("@PostConstruct");
        assertThat(phases.get(1)).contains("2/3").contains("afterSingletonsInstantiated");
        assertThat(phases.get(2)).contains("3/3").contains("ApplicationRunner");
    }

    @Test
    void requestIsRoutedThroughTheTable() {
        createClient().get()
                .uri("/demo/pay/alipay?amount=12.50")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isEqualTo("alipay paid 12.50"));
    }

    @Test
    void registryEndpointListsChannels() {
        createClient().get()
                .uri("/demo/pay/registry")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.channels[0]").isEqualTo("alipay")
                .jsonPath("$.channels[1]").isEqualTo("wechat")
                .jsonPath("$.registeredAtCallback").isEqualTo(2)
                .jsonPath("$.phases.length()").isEqualTo(3);
    }
}
