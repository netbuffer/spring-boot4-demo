package cn.netbuffer.spring.boot4.demo.config;

import cn.netbuffer.spring.boot4.demo.client.GreetingClient;
import cn.netbuffer.spring.boot4.demo.client.IpQueryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    public IpQueryClient ipQueryClient(
            @Value("${app.ip-query.base-url:https://api.bilibili.com}") String baseUrl) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .defaultHeader("Referer", "https://www.bilibili.com")
                .build();
        return createClient(webClient, IpQueryClient.class);
    }

    @Bean
    @Lazy
    public GreetingClient greetingClient(Environment environment) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:" + resolveServerPort(environment))
                .build();
        return createClient(webClient, GreetingClient.class);
    }

    private static int resolveServerPort(Environment environment) {
        Integer localPort = environment.getProperty("local.server.port", Integer.class);
        if (localPort != null && localPort > 0) {
            return localPort;
        }
        return environment.getProperty("server.port", Integer.class, 8080);
    }

    private <T> T createClient(WebClient webClient, Class<T> clientType) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(clientType);
    }
}
