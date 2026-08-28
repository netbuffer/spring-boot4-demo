package cn.netbuffer.spring.boot4.demo.api;

import cn.netbuffer.spring.boot4.demo.api.dto.GreetingRequest;
import cn.netbuffer.spring.boot4.demo.api.dto.GreetingResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/api/greeting")
public interface GreetingApi {

    @GetExchange
    String hello();

    @GetExchange("/{name}")
    String hello(@PathVariable String name);

    @PostExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
    GreetingResponse greet(@RequestBody GreetingRequest request);

    @GetExchange("/search")
    List<String> search(@RequestParam String keyword);
}
