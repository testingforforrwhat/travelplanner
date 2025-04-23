package com.test.travelplanner.repository;


import com.test.travelplanner.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {


   UserEntity findByUsername(String username);


   boolean existsByUsername(String username);

   @Modifying
   @Transactional
   @Query("UPDATE UserEntity u SET u.email = :email WHERE u.username = :username")
   int updateUserProfile(String username,String email);

   UserEntity findAllById(Long id);
}
