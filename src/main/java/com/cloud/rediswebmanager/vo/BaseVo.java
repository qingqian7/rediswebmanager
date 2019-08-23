package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.ApiModelProperty;

public class BaseVo {
    @ApiModelProperty(required = true,value = "connName",example = "127.0.0.1:6379")
    private String connName;
    @ApiModelProperty(required = true,value = "database",example = "0")
    private int database;

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

}
