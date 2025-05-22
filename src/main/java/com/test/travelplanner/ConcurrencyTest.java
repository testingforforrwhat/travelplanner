package com.test.travelplanner;

import com.test.travelplanner.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConcurrencyTest implements CommandLineRunner {

    private final ProductService productService;
    
    @Override
    public void run(String... args) throws Exception {
        // 等待应用启动完成再执行测试
        Thread.sleep(2000);
        
        log.info("开始并发测试...");
        Long productId = 1L;
        int threadCount = 5;
        
        // 并发控制
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                try {
                    log.info("线程 {} 准备减库存", threadNum);
                    // 模拟所有线程同时运行
                    latch.countDown();
                    latch.await();
                    
                    try {
                        productService.decreaseStockWithRetry(productId, 1);
                        log.info("线程 {} 减库存成功", threadNum);
                    } catch (Exception e) {
                        log.error("线程 {} 减库存失败: {}", threadNum, e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
    }
}