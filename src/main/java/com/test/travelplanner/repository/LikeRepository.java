package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>{
    Optional<Like> findByUserIdAndTripId(Long userId, Long tripId);
    int countByTripId(Long tripId);
}
