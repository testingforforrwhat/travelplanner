package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findByOrderNumber(String orderNumber);
}