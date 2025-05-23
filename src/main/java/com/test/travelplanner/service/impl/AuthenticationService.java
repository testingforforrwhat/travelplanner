package com.test.travelplanner.service.impl;


import com.alibaba.fastjson2.JSON;
import com.test.travelplanner.authentication.UserAlreadyExistException;
import com.test.travelplanner.model.dto.user.LoginAuthResponse;
import com.test.travelplanner.model.entity.UserEntity;
import com.test.travelplanner.model.entity.UserRole;
import com.test.travelplanner.redis.RedisUtil;
import com.test.travelplanner.repository.UserRepository;
import com.test.travelplanner.security.JwtHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {


   private final AuthenticationManager authenticationManager;
   private final JwtHandler jwtHandler;
   private final PasswordEncoder passwordEncoder;
   private final UserRepository userRepository;
    private final RedisUtil redisUtil;


    public AuthenticationService(
           AuthenticationManager authenticationManager,
           JwtHandler jwtHandler,
           PasswordEncoder passwordEncoder,
           UserRepository userRepository,
           RedisUtil redisUtil) {
       this.authenticationManager = authenticationManager;
       this.jwtHandler = jwtHandler;
       this.passwordEncoder = passwordEncoder;
       this.userRepository = userRepository;
        this.redisUtil = redisUtil;
    }


   public UserEntity register(String username, String password, UserRole role, String email) throws UserAlreadyExistException {
       if (userRepository.existsByUsername(username)) {
           throw new UserAlreadyExistException();
       }


       UserEntity userEntity = new UserEntity(
               null,
               username,
               passwordEncoder.encode(password),
               role,
               email);
       return userRepository.save(userEntity);
   }


   public LoginAuthResponse login(String username, String password) {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
       LoginAuthResponse authResponse = new LoginAuthResponse();
       String token = jwtHandler.generateToken(username);
       authResponse.setToken(jwtHandler.generateToken(username));

       // ( spring cloud ) 签发的令牌，存入Redis中。拼接上Authorization的策略（Bearer Token）前缀。
       redisUtil.set( "Bearer " + token , username , 60 * 24 );

       authResponse.setUser(username);

       return authResponse;
   }
}
