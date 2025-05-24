package com.test.travelplanner.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.travelplanner.model.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrderCreatedEvent(Order order) {
        sendOrderEvent(KafkaConfig.ORDER_CREATED_TOPIC, order);
    }

    public void sendOrderUpdatedEvent(Order order) {
        sendOrderEvent(KafkaConfig.ORDER_UPDATED_TOPIC, order);
    }

    public void sendOrderCancelledEvent(Order order) {
        sendOrderEvent(KafkaConfig.ORDER_CANCELLED_TOPIC, order);
    }

    private void sendOrderEvent(String topic, Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(topic, order.getOrderNumber(), orderJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing order", e);
        }
    }
}