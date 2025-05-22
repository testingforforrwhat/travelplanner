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
     *
     * 假设有 5 个线程同时尝试减少同一个商品的库存：
     *
     * 每个线程读取当前商品状态（包括库存和版本号）
     * 线程 A 先完成事务，成功更新库存并增加版本号
     * 线程 B 提交事务时，发现版本号已变，抛出 OptimisticLockException
     * 线程 B 根据重试逻辑，再次查询最新商品状态（新版本号）
     * 线程 B 使用新状态重新计算并提交
     * 别的线程类似处理
     * 这样就确保了在高并发环境中的数据一致性，避免了"丢失更新"问题。
     *
     */
    @Version  // 关键：版本控制字段
    private Integer version;
}