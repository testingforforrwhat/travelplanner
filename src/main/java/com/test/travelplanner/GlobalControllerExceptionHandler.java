package com.test.travelplanner;


import com.test.travelplanner.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalControllerExceptionHandler {


   @ExceptionHandler(EntityNotFoundException.class)
   public final ResponseEntity<ErrorResponse> handleException(EntityNotFoundException e) {
       return new ResponseEntity<>(new ErrorResponse(
               "Resource not found",
               "resource_not_found"),
               HttpStatus.NOT_FOUND);
   }

}
