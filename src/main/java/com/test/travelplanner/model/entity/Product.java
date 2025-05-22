package com.test.travelplanner.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    private int stock;

    /**
     *
     * @Version 注解：Hibernate 的 MVCC 实现关键，每次更新数据时会检查并自增版本号。
     *
     * OptimisticLockException：当版本号不匹配时（有并发更新），Hibernate 会抛出此异常。
     *
     * 重试机制：在并发冲突时，添加了重试逻辑，避免因乐观锁冲突导致的操作失败。
     * 
     */
    @Version  // 关键：版本控制字段
    private Integer version;
}