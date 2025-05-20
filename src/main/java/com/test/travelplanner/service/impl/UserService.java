package com.test.travelplanner.service.impl;


import com.test.travelplanner.model.entity.UserEntity;
import com.test.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserProfile(Long id) {
        return userRepository.findAllById(id);
    }

    public int updateUserProfile(String username, String email) {
        return userRepository.updateUserProfile(username,email);
    }
}
