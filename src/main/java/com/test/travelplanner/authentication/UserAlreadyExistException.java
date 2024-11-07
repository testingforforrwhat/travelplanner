package com.test.travelplanner.authentication;


public class UserAlreadyExistException extends RuntimeException {


   public UserAlreadyExistException() {
       super("Username already exists");
   }
}
