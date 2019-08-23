package com.cloud.rediswebmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class BeanConfig {
    @Autowired
    JedisConfig jedisConfig;
    @Bean
    JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisConfig.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisConfig.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisConfig.getMaxWait());
        jedisPoolConfig.setMaxTotal(jedisConfig.getMaxActive());
        return jedisPoolConfig;
    }
}
