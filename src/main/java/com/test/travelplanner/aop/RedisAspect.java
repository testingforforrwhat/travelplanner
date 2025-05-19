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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

        logger.info( "Redis ==> 开启缓存策略！ " );

        // 步骤一：去Redis中读取缓存数据
        // Redis Key 生成规则：方法签名+实参数据
        Map<String,Object> keyMap = new HashMap<>();
        keyMap.put( "generateKeyName - signature" , joinPoint.getSignature().toString() );
        keyMap.put( "arguments" , joinPoint.getArgs() );
        String key = JSON.toJSONString( keyMap );
        logger.info("Redis Key 生成 ( 方法签名+实参数据 ) ==> {}", key);

        /**
         *
         * ------------------------------------------------------------------------------------------
         * joinPoint.getSignature().toString()返回的是方法签名的描述，包含方法的修饰、类、参数、返回值等关键信息。
         * ------------------------------------------------------------------------------------------
         *
         * 方法调用会返回如下格式的字符串：修饰符 返回类型 包名.类名.方法名(参数列表)
         * 例如：
         * 假设你有如下方法：
         *
         * ```java
         * @RedisCache
         * @GetMapping
         * public String getDestination(Long id) {}
         * ```
         *
         * 对应的signature.toString() 可能输出：
         *
         * String com.test.travelplanner.controller.DestinationController.getDestinationById(Long) - 返回值类型 包名.类名.方法名(参数类型列表)
         *
         *
         * String methodName = joinPoint.getSignature().getName(); - Spring AOP的JoinPoint提供的Signature对象，只获取方法名 getDestinationById）
         * String methodName = joinPoint.getSignature().getDeclaringTypeName(); - Spring AOP的JoinPoint提供的Signature对象，只获取声明类名（如com.test.travelplanner.controller.DestinationController）
         * String methodName = joinPoint.getSignature().toShortString(); - Spring AOP的JoinPoint提供的Signature对象，只返回简短签名。
         * String methodName = joinPoint.getSignature().toLongString(); - Spring AOP的JoinPoint提供的Signature对象，返回更详细签名（包含参数名）。
         *
         * 
         * joinPoint.getArgs() - 获取当前切面方法的所有参数数组。
         *
         * Arrays.stream(...).iterator().next() - 将参数数组转为Stream，并拿到第一个参数。 如果参数为空数组会抛异常。
         *
         * .toString() - 把第一个参数转换成字符串（假设这个参数本应该是数字类型或能正确toString）。
         *
         * Long.valueOf(...) - 将这个字符串转换为 Long 类型。  转换失败会抛异常。
         *
         * destinationRepository.findById(...) - 用 Long 类型主键去数据库查找 Destination 实体。 如果数据库找不到这个ID，会抛 NoSuchElementException。
         *
         * .get().getName() - 拿到查询到的 destination 的 name 字段。
         *
         * 
         */
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
            logger.info("Redis ==> generate key and  key 去Redis中查询缓存数据 (对应存储的 value) ！ ");
            // get cacheData by redisKeyName
            Object cacheData = redisUtil.get(key);
            logger.info("Redis key (对应存储的 value) ==> get cacheData by redisKeyName ==> cacheData:{}", cacheData);

            // 如果第一个键为空，再尝试使用备用键
            if (cacheData == null) {
                System.out.println("第一个键为空，尝试使用备用键");
                cacheData = redisUtil.get(keyTopTen);
            }

            // 步骤二：判断缓存数据是否存在
            if (cacheData != null) {
                // 2.1 缓存命中，直接返回缓存数据
                logger.info("Redis ==> 缓存命中，直接返回缓存数据！ ");

                destinationsUtils.incrementClickCount(Arrays.stream(joinPoint.getArgs()).iterator().next().toString());

                String name = destinationRepository.findById(Long.valueOf(Arrays.stream(joinPoint.getArgs()).iterator().next().toString())).get().getName();
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
                logger.info("Redis ==> 缓存未命中，去MySQL查询数据！ ");
                // 通过joinPoint连接点，调用代理的目标方法（业务逻辑层中的核心业务方法）
                Object returnValue = joinPoint.proceed();
                /**
                 * Cannot construct instance of `org.springframework.http.ResponseEntity` (no Creators, like default constructor, exist)
                 *
                 * 这个错误表明你的 Redis AOP 缓存机制尝试将 ResponseEntity 类型的对象直接缓存到 Redis 中，但反序列化时失败。这是因为 ResponseEntity 没有默认构造函数。
                 *
                 * 在你给出的DTO代码环境下，其本质是你把ResponseEntity封装的内容直接缓存到了Redis，
                 * 并希望反序列化( object to jsonString )为ResponseEntity——
                 * 但是Jackson和RedisTemplate无法完成对ResponseEntity的反序列化（因为它没有无参构造方法和标准JavaBean约定，不适合直接做序列化）。
                 *
                 */
                logger.info(" check response type ==> returnValue: {}", returnValue.getClass().getName());

                // 步骤四：将MySQL中查询到的数据，生成缓存到Redis中
                logger.info("Redis ==> MySQL中查询到的数据，生成缓存到Redis中！ ");
                // ==> 缓存穿透 => 判断 将MySQL中查询到的数据是否为null
                if (returnValue == null) {
                    // ==> 缓存穿透 => 对null空值，依然生成缓存，生命周期较短。为了避免缓存穿透。
                    logger.info("MySQL中查询到的数据为null 对null空值，依然生成缓存，生命周期较短。为了避免缓存穿透。");
                    redisUtil.set(key, "null", 50);
                } else {
                    // ==> 缓存穿透 => 正常生成缓存
                    if (returnValue instanceof ResponseEntity<?> responseEntity) {
                        // 只缓存响应体，不缓存整个ResponseEntity
                        Object body = responseEntity.getBody();
                        if (body != null) {
                            logger.info("返回值是ResponseEntity，只缓存响应体，不缓存整个ResponseEntity");
                            redisUtil.set(
                                    key,                      // redis存储的key
                                    body,              // redis存储的value
                                    // redis数据的生命周期（单位：秒） ==> 缓存雪崩 => 生命周期中增加随机部分，避免缓存在同一时间同时失效
                                    redisCache.duration() + (int)( Math.random() * redisCache.duration() / 10 )
                            );
                            logger.info("Redis ==> 数据已缓存");
                        } else {
                            // 如果返回值不是ResponseEntity，则直接缓存
                            logger.info("返回值不是ResponseEntity，直接缓存");
                            redisUtil.set(
                                    key,                      // redis存储的key
                                    returnValue,              // redis存储的value
                                    // redis数据的生命周期（单位：秒） ==> 缓存雪崩 => 生命周期中增加随机部分，避免缓存在同一时间同时失效
                                    redisCache.duration() + (int)( Math.random() * redisCache.duration() / 10 )
                            );
                            logger.info("Redis ==> 数据已缓存");
                        }
                    }

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

    // 判断返回类型是否为ResponseEntity
    private boolean isResponseEntityReturnType(ProceedingJoinPoint joinPoint) {
        try {
            String returnTypeName = joinPoint.getSignature().toString()
                    .split(" ")[0]; // 假设方法签名的第一部分是返回类型
            return returnTypeName.contains("ResponseEntity");
        } catch (Exception e) {
            return false;
        }
    }

}
