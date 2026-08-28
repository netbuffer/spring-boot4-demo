package cn.netbuffer.spring.boot4.demo.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IpQueryClientTest {

    @Autowired
    private IpQueryClient ipQueryClient;

    @Test
    void getPublicIpViaDeclarativeClient() {
        Mono<IpQueryResult> result = ipQueryClient.getPublicIp();

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.code()).isZero();
                    assertThat(response.data()).isNotNull();
                    assertThat(response.data().addr()).matches("^\\d{1,3}(\\.\\d{1,3}){3}$");
                })
                .verifyComplete();
    }
}
