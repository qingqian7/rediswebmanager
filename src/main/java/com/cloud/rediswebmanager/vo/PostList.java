package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.ApiModelProperty;

public class PostList extends BaseVo{
    @ApiModelProperty(required = true,value = "key",example = "listKey")
    private String key;
    @ApiModelProperty(required = false,value = "value",example = "listValue")
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
