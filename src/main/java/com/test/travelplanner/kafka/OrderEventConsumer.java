package com.test.travelplanner.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = KafkaConfig.ORDER_CREATED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCreatedEvent(String orderJson) {
        logger.info("Received order created event: {}", orderJson);
        // 实际的订单创建逻辑处理
    }

    @KafkaListener(topics = KafkaConfig.ORDER_UPDATED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderUpdatedEvent(String orderJson) {
        logger.info("Received order updated event: {}", orderJson);
        // 实际的订单更新逻辑处理
    }

    @KafkaListener(topics = KafkaConfig.ORDER_CANCELLED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCancelledEvent(String orderJson) {
        logger.info("Received order cancelled event: {}", orderJson);
        // 实际的订单取消逻辑处理
    }
}