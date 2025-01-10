package com.test.travelplanner.service;

import com.test.travelplanner.model.entity.CityPoint;
import com.test.travelplanner.repository.CityPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service  // 标识该类是一个Spring服务组件
@RequiredArgsConstructor  // Lombok注解，自动生成构造函数，注入所有标记为final的字段
@Transactional(readOnly = true)  // 默认将所有方法设置为只读事务，以提高性能
public class CityPointService {

    private final CityPointRepository cityPointRepository;  // 注入CityPointRepository，用于数据库操作

    @Transactional  // 标识该方法是一个事务方法，需要写操作
    public CityPoint create(CityPoint cityPoint) {
        // 如果没有指定访问顺序，则自动设置为当前行程的最大顺序加1
        if (cityPoint.getVisitOrder() == null) {
            Integer maxOrder = cityPointRepository.findMaxVisitOrderByTripId(cityPoint.getTripId());
            cityPoint.setVisitOrder(maxOrder == null ? 1 : maxOrder + 1);
        }
        // 保存CityPoint实体到数据库
        return cityPointRepository.save(cityPoint);
    }

    // 根据ID查找CityPoint，返回Optional包装的结果
    public Optional<CityPoint> findById(Long id) {
        return cityPointRepository.findById(id);
    }

    // 根据tripId查找所有CityPoint，按访问顺序排序
    public List<CityPoint> findByTripId(Long tripId) {
        return cityPointRepository.findByTripIdOrderByVisitOrder(tripId);
    }

    // 根据userId查找所有CityPoint，按创建时间降序排序
    public List<CityPoint> findByUserId(Long userId) {
        return cityPointRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional  // 标识该方法是一个事务方法，需要写操作
    public CityPoint update(Long id, CityPoint cityPoint) {
        // 查找CityPoint并更新其属性，如果不存在则抛出异常
        return cityPointRepository.findById(id)
                .map(existing -> {
                    existing.setCityName(cityPoint.getCityName());
                    existing.setPoiName(cityPoint.getPoiName());
                    existing.setPoiDescription(cityPoint.getPoiDescription());
                    existing.setCityDescription(cityPoint.getCityDescription());
                    existing.setVisitOrder(cityPoint.getVisitOrder());
                    // 保存更新后的CityPoint实体
                    return cityPointRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("CityPoint not found: " + id));
    }

    @Transactional  // 标识该方法是一个事务方法，需要写操作
    public void delete(Long id) {
        // 删除指定ID的CityPoint
        cityPointRepository.deleteById(id);
    }
}