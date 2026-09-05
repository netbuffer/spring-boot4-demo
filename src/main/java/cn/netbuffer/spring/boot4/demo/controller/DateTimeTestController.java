package cn.netbuffer.spring.boot4.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/datetime/test")
public class DateTimeTestController {

    @GetMapping("localtime")
    public LocalTime localTime() {
        return LocalTime.now();
    }

    @GetMapping("localdate")
    public LocalDate localDate() {
        return LocalDate.now();
    }

    @GetMapping("localdatetime")
    public LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }

}