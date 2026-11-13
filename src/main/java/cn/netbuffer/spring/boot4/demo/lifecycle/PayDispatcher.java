package cn.netbuffer.spring.boot4.demo.lifecycle;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SmartInitializingSingleton是Spring 在预实例化所有（非 lazy 的）单例 Bean 并跑完各自初始化回调之后、上下文 refresh 收尾之前，对每个实现了该接口的单例回调一次的钩子。
 */
@Component
public class PayDispatcher implements SmartInitializingSingleton, ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PayDispatcher.class);

    private final ConfigurableListableBeanFactory beanFactory;
    private final Map<String, PayHandler> table = new ConcurrentHashMap<>();
    private final List<String> phases = Collections.synchronizedList(new ArrayList<>());

    private int seenAtPostConstruct;
    private int knownHandlerBeans;
    private int registeredAtCallback;
    private volatile boolean readyAtRunner;

    public PayDispatcher(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 时机一：只保证"我自己"初始化完了，别的 Bean 建没建完全看创建顺序，
     */
    @PostConstruct
    public void lookAroundAtMyOwnInit() {
        String[] beanNames = beanFactory.getBeanNamesForType(PayHandler.class, true, false);
        knownHandlerBeans = beanNames.length;
        seenAtPostConstruct = countInstantiated(beanNames);
        logPhase(1, "@PostConstruct", "@PostConstruct invoked，但支付渠道只出生了 " + seenAtPostConstruct + " 个（一共 " + knownHandlerBeans
                + " 个）—— 别的对象还在后面排队，这时候清点必然数漏");
    }

    /**
     * 时机二 ：所有单例都创建完了，而 Web 端口还没开始接流量。
     */
    @Override
    public void afterSingletonsInstantiated() {
        beanFactory.getBeansOfType(PayHandler.class).forEach((beanName, handler) -> {
            if (table.put(handler.channel(), handler) != null) {
                throw new IllegalStateException("支付渠道重复注册: " + handler.channel());
            }
        });
        if (table.isEmpty()) {
            throw new IllegalStateException("容器里没有任何 PayHandler，支付功能不可用");
        }
        registeredAtCallback = table.size();
        logPhase(2, "afterSingletonsInstantiated", "afterSingletonsInstantiated invoked 所有对象都建完了，而网站还没开门—— " + registeredAtCallback
                + " 个渠道一次登记齐 " + table.keySet());
    }

    /**
     * 时机三：Boot 的 Runner，此时 Tomcat 已经监听端口，能做的只有"检查自己是不是准备好了"。
     */
    @Override
    public void run(ApplicationArguments args) {
        readyAtRunner = registeredAtCallback > 0;
        logPhase(3, "ApplicationRunner", "ApplicationRunner invoked 网站已接入流量 " + registeredAtCallback + " 个渠道）");
    }

    public String pay(String channel, BigDecimal amount) {
        PayHandler handler = table.get(channel);
        if (handler == null) {
            throw new IllegalArgumentException("不支持的支付渠道: " + channel);
        }
        return handler.pay(amount);
    }

    public List<String> phases() {
        return List.copyOf(phases);
    }

    public Map<String, Object> report() {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("channels", new TreeSet<>(table.keySet()));
        report.put("seenAtPostConstruct", seenAtPostConstruct);
        report.put("knownHandlerBeans", knownHandlerBeans);
        report.put("registeredAtCallback", registeredAtCallback);
        report.put("readyAtRunner", readyAtRunner);
        report.put("phases", List.copyOf(phases));
        return report;
    }

    /**
     * allowEagerInit=false：只数已经创建出来的，不为了统计去把别的 Bean 提前拽起来。
     */
    private int countInstantiated(String[] beanNames) {
        int count = 0;
        for (String beanName : beanNames) {
            if (beanFactory.containsSingleton(beanName)) {
                count++;
            }
        }
        return count;
    }

    private void logPhase(int index, String phase, String detail) {
        long jvmUptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        String line = String.format("[lifecycle] %d/3 %-28s (JVM +%dms) -> %s", index, phase, jvmUptimeMs, detail);
        log.info(line);
        phases.add(line);
    }

}
