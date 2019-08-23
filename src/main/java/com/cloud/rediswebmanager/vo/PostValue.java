package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PostValue extends BaseVo implements Serializable {
    @NotNull(message = "键名不能为空")
    @ApiModelProperty(required = true,value = "键名",example = "key")
    private String key;

    @ApiModelProperty(value = "index",example = "0")
    private int index;

    @ApiModelProperty(value = "pattern")
    private String pattern;

    @ApiModelProperty(value = "ttl")
    private int ttl;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
