package cn.netbuffer.spring.boot4.demo.lifecycle;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AlipayHandler implements PayHandler {

    @Override
    public String channel() {
        return "alipay";
    }

    @Override
    public String pay(BigDecimal amount) {
        return "alipay paid " + amount;
    }
}
