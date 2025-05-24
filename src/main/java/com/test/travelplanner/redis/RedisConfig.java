package com.test.travelplanner.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.net.UnknownHostException;

@Configuration
public class RedisConfig {

    /**
     *
     * # 创建订单
     * curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{"userId": 1, "totalAmount": 99.99}'
     * 
     * "Could not write JSON: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module \"com.fasterxml.jackson.datatype:jackson-datatype-jsr310\" to enable handling (through reference chain: com.test.travelplanner.model.entity.order.Order[\"createdAt\"])",
     *
     * fix: 这个错误是因为 Jackson 无法序列化 Java 8 的时间类型（如 LocalDateTime）。
     *      Spring Boot 2.x+ 默认已经包含了对 JSR310 的支持，但可能配置不当或版本问题导致未生效。
     *
     * 由于你在用 Redis 缓存 Order 对象，确保 RedisTemplate 的序列化器也支持 JSR310
     * Redis 配置一定要修改，否则缓存 Order 对象时还会出错
     *
     * 这样修改后，你的 LocalDateTime 字段就能正常序列化/反序列化了，无论是 REST API 返回 JSON 还是 Redis 缓存都没问题。
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 配置支持 Java 8 时间类型的 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 使用配置好的 ObjectMapper 创建序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer);

        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateObject(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /**
     *
     * 如果容器中不存在名为 "redisTemplate" 的 Bean，那么当前的 Bean 就会被自动配置
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(
            name = { "redisTemplate" }
    )
    public RedisTemplate<String, Serializable> redisTemplateSerializable(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer( new StringRedisSerializer());
        redisTemplate.setValueSerializer( new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    /**
     * 如果容器中不存在对应类型的 Bean，那么当前的 Bean 才会被注册
     * 如果Spring容器中已经存在相同类型的bean，那么bean不会被注册。这可以用来确保只有在需要时才会注册某个bean，以避免冲突或重复注册
     * @param connectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
