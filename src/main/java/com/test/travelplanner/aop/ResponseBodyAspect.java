package com.test.travelplanner.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Aspect
@Component
public class ResponseBodyAspect {
    
    // Before advice: called before the execution of any method annotated with @ResponseBody
    @Before("@annotation(responseBody)")
    public void beforeMethodWithResponseBody(JoinPoint joinPoint, ResponseBody responseBody) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        System.out.println("============> Invoking method " + className + "." + methodName + " which has @ResponseBody annotation.");
    }

    // AfterReturning advice: called after the execution of any method annotated with @ResponseBody
    @AfterReturning(pointcut = "@annotation(responseBody)", returning = "result")
    public void afterReturningMethodWithResponseBody(JoinPoint joinPoint, ResponseBody responseBody, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        System.out.println("============> Method " + className + "." + methodName + " executed and returned: " + result);
    }
}