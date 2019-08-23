package com.cloud.rediswebmanager.cache;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
@Component
public class RedisCache {
    //存储多个jedis连接
    private static Map<String, JedisPool> redisMap = new HashMap<>();

    public void addConn(String name,JedisPool jedisPool){
        if(redisMap.containsKey(name))
            return;
        redisMap.put(name,jedisPool);
    }

    public JedisPool getConn(String name){
        return redisMap.get(name);
    }

    public void delConn(String name){
        redisMap.remove(name);
    }
}
