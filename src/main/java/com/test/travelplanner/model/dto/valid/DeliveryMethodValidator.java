package com.test.travelplanner.model.dto.valid;

import com.test.travelplanner.model.DeliveryMethod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;  

public class DeliveryMethodValidator implements ConstraintValidator<ValidDeliveryMethod, DeliveryMethod> {

    @Override  
    public boolean isValid(DeliveryMethod value, ConstraintValidatorContext context) {  
        if (value == null) {  
            return false; // 空值视为不合法（可根据需求改为 true）  
        }  

        // 验证是否在合法的枚举值范围内  
        for (DeliveryMethod method : DeliveryMethod.values()) {  
            if (method.equals(value)) {  
                return true;  
            }  
        }  

        // 如果没有匹配到合法值，返回 false  
        return false;  
    }  
}  