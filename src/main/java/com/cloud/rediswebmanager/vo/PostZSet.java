package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

public class PostZSet extends BaseVo {
    @ApiModelProperty(required = true,value = "zsetKey")
    private String key;
    @ApiModelProperty(value = "zsetValue")
    private String value;
    @ApiModelProperty(value = "zsetScore")
    private double score;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
