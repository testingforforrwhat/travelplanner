package com.test.travelplanner.authentication;


import com.test.travelplanner.authentication.model.UserEntity;
import com.test.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserProfile(String username) {
        return userRepository.findByUsername(username);
    }

    public int updateUserProfile(String username, String email) {
        return userRepository.updateUserProfile(username,email);
    }
}
