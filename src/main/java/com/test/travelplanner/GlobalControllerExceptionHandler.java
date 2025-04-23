package com.test.travelplanner;


import com.test.travelplanner.authentication.UserAlreadyExistException;
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

    // 使用 @ExceptionHandler 注解，指定该方法用于处理 UserAlreadyExistException 异常
    @ExceptionHandler(UserAlreadyExistException.class)
    public final ResponseEntity<ErrorResponse> handleException(UserAlreadyExistException e) {
        // 创建并返回一个 ResponseEntity 对象，包含自定义的错误响应和 HTTP 状态码
        return new ResponseEntity<>(
                // 构造自定义的错误响应对象 ErrorResponse
                new ErrorResponse(
                        "User exist", // 错误信息，描述资源未找到
                        "user_exist, " + e.getMessage()  // 错误代码，用于标识错误类型
                ),
                HttpStatus.NOT_FOUND // 设置 HTTP 状态码为 404，表示资源未找到
        );
    }

}
