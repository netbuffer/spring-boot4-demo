package cn.netbuffer.spring.boot4.demo.client;

import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface IpQueryClient {

    @GetExchange("/x/web-interface/zone")
    Mono<IpQueryResult> getPublicIp();
}
