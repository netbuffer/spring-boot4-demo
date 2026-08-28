package cn.netbuffer.spring.boot4.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

import static cn.netbuffer.spring.boot4.demo.SpringBoot4DemoApplication.DOTENV;

@RestController
@RequestMapping("/dotenv")
public class DotEnvController {

    @Value(value = "${sb4d.my_env_var1}")
    private String myEnvVar1;

    @GetMapping("/list")
    public List<String> list() {
        List<String> list = new ArrayList<>();
        DOTENV.entries().forEach(e -> {
            String temp = e.getKey() + "=" + e.getValue();
            System.out.println(temp);
            list.add(temp);
        });
        return list;
    }

    @GetMapping("/myEnvVar1")
    public String getMyEnvVar1() {
        return myEnvVar1;
    }

}