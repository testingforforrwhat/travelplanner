package com.test.travelplanner.repository;

import java.util.List;
import com.test.travelplanner.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByUserId(Long userId);
}
