package com.test.travelplanner.model.dto.user;


import com.test.travelplanner.model.dto.valid.StrongPassword;
import com.test.travelplanner.model.dto.valid.ValidPhone;
import com.test.travelplanner.model.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data  
public class UserRegistrationRequest {

    @NotNull(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3到20个字符之间")
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^[0-9]+$", message = "电话号码只能包含数字")
    @ValidPhone
    private String phone;

    @StrongPassword
    private String password;

    private UserRole role;
}