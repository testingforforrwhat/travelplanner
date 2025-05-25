package com.test.travelplanner.service.impl;


import com.test.travelplanner.kafka.OrderEventProducer;
import com.test.travelplanner.model.entity.order.Order;
import com.test.travelplanner.redis.OrderCacheService;
import com.test.travelplanner.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCacheService orderCacheService;
    private final OrderEventProducer orderEventProducer;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderCacheService orderCacheService,
                        OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderCacheService = orderCacheService;
        this.orderEventProducer = orderEventProducer;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order getOrderByNumber(String orderNumber) {
        // 先从缓存获取
        Order cachedOrder = orderCacheService.getOrder(orderNumber);
        if (cachedOrder != null) {
            return cachedOrder;
        }
        
        // 缓存未命中，从数据库查询
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));
        
        // 查到后放入缓存
        orderCacheService.cacheOrder(order);
        return order;
    }

    /**
     *
     * 建议同时使用限流和断路器：
     * - 限流防止流量冲击 ( 控制请求速率(控制本方法调用次数)，防止本系统过载 )
     * - 断路器防止故障扩散 ( 本方法调用依赖系统(检测本方法调用 success 次数),错误率达到阈值（如50%）或响应超时,快速失败，防止级联故障，防止依赖系统过载 )
     * - 两者互补，提供更全面的系统保护
     *
     *
     * @param userId
     * @param totalAmount
     * @return
     */
    @CircuitBreaker(name = "orderService", fallbackMethod = "createOrderFallback")
    @RateLimiter(name = "orderServiceLimiter", fallbackMethod = "createOrderRateLimitFallback")
    @Transactional
    public Order createOrder(Long userId, BigDecimal totalAmount) {
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // 保存订单
        Order savedOrder = orderRepository.save(order);
        
        // 放入缓存
        orderCacheService.cacheOrder(savedOrder);
        
        // 发送Kafka消息
        orderEventProducer.sendOrderCreatedEvent(savedOrder);
        
        return savedOrder;
    }

    /**
     *
     * 参数列表 = 主方法所有参数 + Throwable ex
     * 返回类型 = 主方法返回类型完全一致
     * 方法名 = 注解中 fallbackMethod 值完全一致
     *
     * @param userId
     * @param totalAmount
     * @param ex
     * @return
     */
    public Order createOrderFallback(Long userId, BigDecimal totalAmount, Throwable ex) {
        Order fallbackOrder = new Order();
        fallbackOrder.setId(-1L);
        fallbackOrder.setStatus("订单服务异常，已记录您的请求，稍后处理");
        return fallbackOrder;
    }

    public Order createOrderRateLimitFallback(Long userId, BigDecimal totalAmount, Exception ex) {
        Order fallbackOrder = new Order();
        fallbackOrder.setId(-1L);
        fallbackOrder.setStatus("系统繁忙，请稍后重试创建订单");
        return fallbackOrder;
    }

    public String createOrderFallback(String orderData, Exception ex) {
        return "订单服务异常，已记录您的请求，稍后处理";
    }

    public String createOrderRateLimitFallback(String orderData, Exception ex) {
        return "系统繁忙，请稍后重试创建订单";
    }

    @Transactional
    public Order updateOrderStatus(String orderNumber, String status) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        // 更新缓存
        orderCacheService.cacheOrder(updatedOrder);
        
        // 发送Kafka消息
        if ("CANCELLED".equals(status)) {
            orderEventProducer.sendOrderCancelledEvent(updatedOrder);
        } else {
            orderEventProducer.sendOrderUpdatedEvent(updatedOrder);
        }
        
        return updatedOrder;
    }

    private String generateOrderNumber() {
        return "ORD" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10).toUpperCase();
    }
}