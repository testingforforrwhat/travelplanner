package com.test.travelplanner.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * MVCC 允许多个事务同时读取数据库中的“同一行”而不会互相阻塞。
 * 每个事务看到的都是自己操作时刻的数据快照（Snapshot），
 * 通过版本号或时间戳区分不同数据版本，从而避免锁表、阻塞等影响系统性能的问题。
 *
 * Hibernate 实现 MVCC 主要依托两个机制：
 * （1）乐观锁（Optimistic Locking）
 * 最常见的是在实体类加一个 版本号字段（@Version 注解）
 * 每次更新数据时，Hibernate 会检查数据库当前的版本号与自己的版本号是否一致。
 * 如果版本不一致，说明有其他事务修改过，更新失败，抛出 OptimisticLockException。
 * （2）快照机制（Snapshot）
 * 每次查询时，Hibernate 会在 Session 一级缓存中保存一份数据快照。
 * 提交事务时，Hibernate 比较快照和数据库最新数据的版本号，如果快照版本号落后于数据库，即认为出现了并发更新。
 *
 * MVCC 优点
 * 高并发性能：允许更多事务同时操作，提高系统吞吐量。
 * 解决幻读、脏读问题：结合数据库本身的隔离级别，能较好避免脏读、不可重复读等问题。
 * 减少数据库锁竞争：无须长时间持有数据库锁，避免死锁、性能瓶颈。
 *
 * 注意事项
 * MVCC 主要应用于支持版本/时间戳的数据库，如 MySQL（InnoDB）、PostgreSQL 等。
 * 乐观锁适合“读多写少”场景，不适合高并发写入。
 * 乐观锁冲突时，需要业务层做好异常处理和回滚重试机制。
 *
 */
@Entity
@Data
@Table(name = "product")
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


    // 旅行行程相关字段

    @Column(name = "destination", length = 100)
    private String destination; // 目的地

    @Column(name = "departure_location", length = 100)
    private String departureLocation; // 出发地

    /**
     * LocalDate 和 LocalDateTime 类型处理日期和时间
     */
    @Column(name = "start_date")
    private LocalDate startDate; // 行程开始日期

    @Column(name = "end_date")
    private LocalDate endDate; // 行程结束日期

    @Column(name = "duration")
    private Integer duration; // 行程天数

    @Column(name = "itinerary", columnDefinition = "TEXT")
    private String itinerary; // 详细行程安排，可存储JSON或文本描述

    @Column(name = "includes", columnDefinition = "TEXT")
    private String includes; // 包含的服务（如酒店、餐饮、导游等）

    @Column(name = "excludes", columnDefinition = "TEXT")
    private String excludes; // 不包含的服务

    @Column(name = "transportation_type", length = 50)
    private String transportationType; // 交通方式（飞机、火车、巴士等）

    @Column(name = "accommodation_level", length = 50)
    private String accommodationLevel; // 住宿等级

    @Column(name = "max_participants")
    private Integer maxParticipants; // 最大参与人数

    @Column(name = "min_participants")
    private Integer minParticipants; // 最小成团人数

    /**
     * LocalDate 和 LocalDateTime 类型处理日期和时间
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 产品创建时间

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 产品更新时间

    @Column(name = "product_code", length = 50, unique = true)
    private String productCode; // 产品编码，唯一

    @Column(name = "status", length = 20)
    private String status; // 产品状态（上架、下架、售罄等）

}