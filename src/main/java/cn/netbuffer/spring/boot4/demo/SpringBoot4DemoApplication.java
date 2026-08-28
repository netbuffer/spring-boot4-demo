package cn.netbuffer.spring.boot4.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBoot4DemoApplication {

    public static final Dotenv DOTENV;

    static {
        DOTENV = Dotenv.configure().filename("sb4d.env").ignoreIfMissing().load();
        DOTENV.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot4DemoApplication.class, args);
    }

}