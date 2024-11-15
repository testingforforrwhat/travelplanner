package com.test.travelplanner.repository;


import com.test.travelplanner.authentication.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Long> {


   UserEntity findByUsername(String username);


   boolean existsByUsername(String username);
}
