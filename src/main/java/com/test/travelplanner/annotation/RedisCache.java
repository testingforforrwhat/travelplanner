package com.test.travelplanner.annotation;

import java.lang.annotation.*;

/**
 * Redis缓存策略注解
 * 方法Method承载该注解
 * 承载该注解的方法，需要进行RedisAspect缓存切入
 * */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    long duration() default 0;
}
