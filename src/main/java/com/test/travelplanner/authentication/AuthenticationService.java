package com.test.travelplanner.authentication;


import com.test.travelplanner.authentication.model.UserEntity;
import com.test.travelplanner.authentication.model.UserRole;
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


   public AuthenticationService(
           AuthenticationManager authenticationManager,
           JwtHandler jwtHandler,
           PasswordEncoder passwordEncoder,
           UserRepository userRepository
   ) {
       this.authenticationManager = authenticationManager;
       this.jwtHandler = jwtHandler;
       this.passwordEncoder = passwordEncoder;
       this.userRepository = userRepository;
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


   public String login(String username, String password) {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
       return jwtHandler.generateToken(username);
   }
}
