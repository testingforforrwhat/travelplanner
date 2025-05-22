package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // 可选：使用 OPTIMISTIC 显式声明锁模式
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findById(Long id);
}