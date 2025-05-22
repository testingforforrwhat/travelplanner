package com.test.travelplanner.service.impl;

import com.test.travelplanner.model.entity.Product;
import com.test.travelplanner.repository.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
    }
    
    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        Product product = getProduct(productId);
        log.info("当前版本: {}, 当前库存: {}", product.getVersion(), product.getStock());
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        // 减少库存
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        
        log.info("减库存成功，新版本: {}, 新库存: {}", product.getVersion(), product.getStock());
    }
    
    // 带重试机制的库存减少
    @Transactional
    public void decreaseStockWithRetry(Long productId, int quantity) {
        int maxRetries = 3;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                decreaseStock(productId, quantity);
                return; // 成功则返回
            } catch (OptimisticLockException e) {
                retryCount++;
                log.warn("乐观锁冲突，重试 {}/{}", retryCount, maxRetries);
                
                if (retryCount >= maxRetries) {
                    log.error("达到最大重试次数，操作失败");
                    throw e;
                }
                
                try {
                    // 随机等待一小段时间，避免并发冲突
                    Thread.sleep((long) (Math.random() * 200));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}