package cn.netbuffer.spring.boot4.demo.controller;

import cn.netbuffer.spring.boot4.demo.lifecycle.PayDispatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/demo/pay")
public class PayController {

    private final PayDispatcher dispatcher;

    public PayController(PayDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @GetMapping("/{channel}")
    public String pay(@PathVariable String channel,
                      @RequestParam(defaultValue = "1.00") BigDecimal amount) {
        return dispatcher.pay(channel, amount);
    }

    @GetMapping("/registry")
    public Map<String, Object> registry() {
        return dispatcher.report();
    }
}
