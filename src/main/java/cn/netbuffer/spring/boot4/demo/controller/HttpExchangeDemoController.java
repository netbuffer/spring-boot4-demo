package cn.netbuffer.spring.boot4.demo.controller;

import cn.netbuffer.spring.boot4.demo.api.dto.GreetingRequest;
import cn.netbuffer.spring.boot4.demo.api.dto.GreetingResponse;
import cn.netbuffer.spring.boot4.demo.client.GreetingClient;
import cn.netbuffer.spring.boot4.demo.client.IpQueryClient;
import cn.netbuffer.spring.boot4.demo.client.IpQueryResult;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/demo/http-exchange")
public class HttpExchangeDemoController {

    private final GreetingClient greetingClient;
    private final IpQueryClient ipQueryClient;

    public HttpExchangeDemoController(@Lazy GreetingClient greetingClient, IpQueryClient ipQueryClient) {
        this.greetingClient = greetingClient;
        this.ipQueryClient = ipQueryClient;
    }

    @GetMapping("/local/hello")
    public Mono<String> localHello() {
        return greetingClient.hello();
    }

    @GetMapping("/local/hello/{name}")
    public Mono<String> localHello(@PathVariable String name) {
        return greetingClient.hello(name);
    }

    @GetMapping("/local/greet/{name}")
    public Mono<GreetingResponse> localGreet(@PathVariable String name) {
        return greetingClient.greet(new GreetingRequest(name));
    }

    @GetMapping("/local/search")
    public Mono<List<String>> localSearch(@org.springframework.web.bind.annotation.RequestParam String keyword) {
        return greetingClient.search(keyword);
    }

    @GetMapping("/public/ip")
    public Mono<IpQueryResult> publicIp() {
        return ipQueryClient.getPublicIp();
    }
}
