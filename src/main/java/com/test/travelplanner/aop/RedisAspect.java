package com.test.travelplanner.aop;


import com.alibaba.fastjson2.JSON;
import com.test.travelplanner.RedisCache;
import com.test.travelplanner.redis.DestinationsUtils;
import com.test.travelplanner.redis.RedisUtil;
import com.test.travelplanner.repository.DestinationRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存策略 切面类
 * */
@Component
@Aspect
@Slf4j
public class RedisAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisAspect.class);

    // 依赖项
    @Resource
    private DestinationRepository destinationRepository;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private DestinationsUtils destinationsUtils;


    /**
     *
     * 常见的切点表达式包括：
     *
     * "execution(* com.example.service.*.*(..))"：匹配 com.example.service 包下的所有方法。
     *
     * "within(com.example.service..*)"：匹配 com.example.service 包及其子包下的所有类的所有方法。
     *
     * "@annotation(org.springframework.transaction.annotation.Transactional)"：匹配所有带有 @Transactional 注解的方法。
     *
     *
     */

    //  Pointcut切入点。描述execution织入表达式。描述代理的目标方法。
    @Pointcut("execution(* com.test.travelplanner.controller.DestinationController.getAllDestinations(..))")
    public void TopTenCachepointCut(){}

    // 环绕增强方法 = 前置增强 + 后置增强 + 返回值增强 + 异常增强
    @Around( value = "@annotation(redisCache)" )
    public Object around(ProceedingJoinPoint joinPoint , RedisCache redisCache) throws Throwable {

        System.out.println( "Redis ==> 开启缓存策略！ " );
        log.info( "Redis ==> 开启缓存策略！ " );

        // 步骤一：去Redis中读取缓存数据
        // Redis Key 生成规则：方法签名+实参数据
        Map<String,Object> keyMap = new HashMap<>();
        keyMap.put( "signature" , joinPoint.getSignature().toString() );
        keyMap.put( "arguments" , joinPoint.getArgs() );
        String key = JSON.toJSONString( keyMap );
        System.out.println("Redis Key 生成: " + key);

        String nameTopTen = destinationRepository.findById(
                Long.valueOf(
                        Arrays.stream(joinPoint.getArgs())
                                .iterator()
                                .next()
                                .toString()
                )).get().getName();
        
        System.out.println("filenameTopTen: " + nameTopTen);
        String NameTopTen = nameTopTen;
        String keyTopTen = "destination:name:clickCountByWeekByDestinationId:" + NameTopTen;

        while( true ) {

            // 去Redis获取缓存数据
            System.out.println("Redis ==> 去Redis中查询缓存数据！ ");
            logger.info("Redis ==> 去Redis中查询缓存数据！ ");
            Object cacheData = redisUtil.get(key);

            // 如果第一个键为空，再尝试使用备用键
            if (cacheData == null) {
                System.out.println("第一个键为空，尝试使用备用键");
                cacheData = redisUtil.get(keyTopTen);
            }

            // 步骤二：判断缓存数据是否存在
            if (cacheData != null) {
                // 2.1 缓存命中，直接返回缓存数据
                System.out.println("Redis ==> 缓存命中，直接返回缓存数据！ ");
                logger.info("Redis ==> 缓存命中，直接返回缓存数据！ ");

                destinationsUtils.incrementClickCount(Arrays.stream(joinPoint.getArgs()).iterator().next().toString());

                String name = destinationRepository.findById(Long.valueOf(Arrays.stream(joinPoint.getArgs()).iterator().next().toString())).get().getName();
                System.out.println("name: " + name);
                logger.info("name ==> {}", name);
                ZSetOperations<String, Object> zSetOps = redisUtil.zSet();
                zSetOps.add("destination:topDestinationsByClickCount", name, (Integer) redisUtil.get("destination:clickCountByWeekByDestinationId:" + Arrays.stream(joinPoint.getArgs()).iterator().next().toString()));

                // ==> 缓存穿透 => 判断Redis中查询到的缓存数据是否是 "null"
                return "null".equals(cacheData) ? null : cacheData;
            }

            // 2.2 缓存未命中
            // ==> 缓存击穿 => 争夺分布式锁
            if ( redisUtil.setnx("Mutex-" + key, 5000) ) {
                // ==> 缓存击穿 => 争夺分布式锁 成功

                // 步骤三：去MySQL查询数据
                System.out.println("Redis ==> 缓存未命中，去MySQL查询数据！ ");
                logger.info("Redis ==> 缓存未命中，去MySQL查询数据！ ");
                // 通过joinPoint连接点，调用代理的目标方法（业务逻辑层中的核心业务方法）
                Object returnValue = joinPoint.proceed();

                // 步骤四：将MySQL中查询到的数据，生成缓存到Redis中
                System.out.println("Redis ==> 生成缓存到Redis中！ ");
                logger.info("Redis ==> 生成缓存到Redis中！ ");
                // ==> 缓存穿透 => 判断 将MySQL中查询到的数据是否为null
                if (returnValue == null) {
                    // ==> 缓存穿透 => 对null空值，依然生成缓存，生命周期较短。为了避免缓存穿透。
                    logger.info("MySQL中查询到的数据为null 对null空值，依然生成缓存，生命周期较短。为了避免缓存穿透。");
                    redisUtil.set(key, "null", 50);
                } else {
                    // ==> 缓存穿透 => 正常生成缓存
                    redisUtil.set(
                            key,                      // redis存储的key
                            returnValue,              // redis存储的value
                            // redis数据的生命周期（单位：秒） ==> 缓存雪崩 => 生命周期中增加随机部分，避免缓存在同一时间同时失效
                            redisCache.duration() + (int)( Math.random() * redisCache.duration() / 10 )
                    );
                }

                // 返回 代理的目标方法的返回值
                return returnValue;

            } else {
                // ==> 缓存击穿 => 争夺分布式锁 失败
                // ==> 缓存击穿 => 延时 500毫秒 => 重新判断缓存数据是否存在
                Thread.currentThread().sleep(500);
            }
        }
    }

}
