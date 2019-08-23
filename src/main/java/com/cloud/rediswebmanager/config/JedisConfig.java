package com.cloud.rediswebmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JedisConfig {
    @Value("${redis.pool.maxtotal}")
    private int maxActive;
    @Value("${redis.pool.maxidle}")
    private int maxIdle;
    @Value("${redis.pool.minidle}")
    private int minIdle;
    @Value("${redis.pool.maxwait}")
    private int maxWait;

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
}
