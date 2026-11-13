package cn.netbuffer.spring.boot4.demo.lifecycle;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WechatHandler implements PayHandler {

    @Override
    public String channel() {
        // 故意把它改成 "alipay" 再启动试试：进程直接起不来，而不是等第一笔支付才报错
        return "wechat";
    }

    @Override
    public String pay(BigDecimal amount) {
        return "wechat paid " + amount;
    }
}
