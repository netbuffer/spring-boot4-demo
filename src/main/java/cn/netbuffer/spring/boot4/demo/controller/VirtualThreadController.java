package cn.netbuffer.spring.boot4.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo/virtual-thread")
public class VirtualThreadController {

    @GetMapping("/info")
    public Map<String, Object> info() {
        Thread thread = Thread.currentThread();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("thread", thread.toString());
        result.put("name", thread.getName());
        result.put("threadId", thread.threadId());
        result.put("isVirtual", thread.isVirtual());
        return result;
    }

    @GetMapping("/parallel")
    public Map<String, Object> parallel(
            @RequestParam(defaultValue = "20") int tasks,
            @RequestParam(defaultValue = "200") long sleepMs) throws Exception {
        int taskCount = Math.clamp(tasks, 1, 200);
        long sleepMillis = Math.clamp(sleepMs, 1, 2000);
        long startedAt = System.nanoTime();

        List<Map<String, Object>> taskResults = new ArrayList<>(taskCount);
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<Map<String, Object>>> callables = new ArrayList<>(taskCount);
            for (int i = 0; i < taskCount; i++) {
                int taskId = i + 1;
                callables.add(() -> runTask(taskId, sleepMillis));
            }
            for (Future<Map<String, Object>> future : executor.invokeAll(callables)) {
                taskResults.add(future.get());
            }
        }

        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tasks", taskCount);
        result.put("sleepMs", sleepMillis);
        result.put("elapsedMs", elapsedMs);
        result.put("requestThreadVirtual", Thread.currentThread().isVirtual());
        result.put("sample", taskResults.subList(0, Math.min(3, taskResults.size())));
        return result;
    }

    private static Map<String, Object> runTask(int taskId, long sleepMillis) throws InterruptedException {
        Thread.sleep(sleepMillis);
        Thread thread = Thread.currentThread();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("taskId", taskId);
        result.put("thread", thread.toString());
        result.put("isVirtual", thread.isVirtual());
        return result;
    }

}
