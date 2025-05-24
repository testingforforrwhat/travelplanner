package com.test.travelplanner.redis;

import com.test.travelplanner.model.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OrderCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ORDER_KEY_PREFIX = "order:";
    private static final long CACHE_DURATION = 30; // 缓存时间，单位分钟

    @Autowired
    public OrderCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheOrder(Order order) {
        String key = ORDER_KEY_PREFIX + order.getOrderNumber();
        redisTemplate.opsForValue().set(key, order, CACHE_DURATION, TimeUnit.MINUTES);
    }

    public Order getOrder(String orderNumber) {
        String key = ORDER_KEY_PREFIX + orderNumber;
        return (Order) redisTemplate.opsForValue().get(key);
    }

    public void deleteOrderCache(String orderNumber) {
        String key = ORDER_KEY_PREFIX + orderNumber;
        redisTemplate.delete(key);
    }
}