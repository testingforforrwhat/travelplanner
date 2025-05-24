package com.test.travelplanner.redis;

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

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
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
