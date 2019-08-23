package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.ApiModelProperty;

public class PostSet extends BaseVo {
    @ApiModelProperty(required = true,value = "key",example = "setKey")
    private String key;
    @ApiModelProperty(value = "value",example = "setValue")
    private String value;

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
}
