package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RedisConn implements Serializable {
    @NotNull(message = "host 不能为空")
    @ApiModelProperty(required = true,value = "host",example = "127.0.0.1")
    private String host;
    @NotNull(message = "port 不能为空")
    @ApiModelProperty(required = true,value = "port",example = "6379")
    private int port;
    private String auth;
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

}
