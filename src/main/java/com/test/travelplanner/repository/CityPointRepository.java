package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.CityPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // 标识该接口是一个Spring Data JPA的仓库接口
public interface CityPointRepository extends JpaRepository<CityPoint, Long> {
    // 继承JpaRepository以获得CRUD操作和分页支持

    // 根据tripId查找CityPoint列表，并按visitOrder排序
    List<CityPoint> findByTripIdOrderByVisitOrder(Long tripId);

    // 根据userId查找CityPoint列表，并按创建时间降序排序
    List<CityPoint> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 使用JPQL查询获取特定tripId的最大visitOrder
    @Query("SELECT MAX(cp.visitOrder) FROM CityPoint cp WHERE cp.tripId = ?1")
    Integer findMaxVisitOrderByTripId(Long tripId);

    // 检查特定tripId和visitOrder的CityPoint是否存在
    boolean existsByTripIdAndVisitOrder(Long tripId, Integer visitOrder);
}