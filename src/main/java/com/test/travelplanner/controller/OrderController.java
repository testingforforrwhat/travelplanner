package com.test.travelplanner.controller;


import com.test.travelplanner.model.entity.order.Order;
import com.test.travelplanner.redis.RedisUtil;
import com.test.travelplanner.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final RedisUtil redisUtil;

    @Autowired
    public OrderController(OrderService orderService, RedisUtil redisUtil) {
        this.orderService = orderService;
        this.redisUtil = redisUtil;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getOrderByNumber(@PathVariable String orderNumber) {
        try {
            Order order = orderService.getOrderByNumber(orderNumber);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestHeader String authorization,
            @RequestBody Map<String, Object> request) {
        if( redisUtil.hashKey( authorization ) ){
        Long userId = Long.valueOf(request.get("userId").toString());
        BigDecimal totalAmount = new BigDecimal(request.get("totalAmount").toString());
        
        Order createdOrder = orderService.createOrder(userId, totalAmount);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}
    }

    @PutMapping("/{orderNumber}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderNumber,
            @RequestBody Map<String, String> request) {
        
        String status = request.get("status");
        if (status == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderNumber, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}