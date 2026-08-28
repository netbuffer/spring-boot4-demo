package cn.netbuffer.spring.boot4.demo.controller;

import cn.netbuffer.spring.boot4.demo.api.GreetingApi;
import cn.netbuffer.spring.boot4.demo.api.dto.GreetingRequest;
import cn.netbuffer.spring.boot4.demo.api.dto.GreetingResponse;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class GreetingController implements GreetingApi {

    private static final List<String> KEYWORDS = List.of("Spring", "HttpExchange", "WebClient", "Mono");

    @Override
    public String hello() {
        return "Hello, HttpExchange!";
    }

    @Override
    public String hello(String name) {
        return "Hello, " + name + "!";
    }

    @Override
    public GreetingResponse greet(GreetingRequest request) {
        return new GreetingResponse("Hello, " + request.name() + "!", Instant.now().toString());
    }

    @Override
    public List<String> search(String keyword) {
        return KEYWORDS.stream()
                .filter(item -> item.toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}
