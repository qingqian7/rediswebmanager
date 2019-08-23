package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.ApiModelProperty;

public class PostString extends BaseVo{
    @ApiModelProperty(required = true,value = "key",example = "key")
    private String key;
    @ApiModelProperty(required = false,value = "value",example = "value")
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
