package cn.netbuffer.spring.boot4.demo.lifecycle;

import java.math.BigDecimal;

/**
 * 支付渠道处理器：一个渠道一个实现，注册成 Bean 就会被 {@link PayDispatcher} 自动收集。
 */
public interface PayHandler {

    String channel();

    String pay(BigDecimal amount);
}
