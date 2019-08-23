package com.cloud.rediswebmanager.vo;

import io.swagger.annotations.ApiModelProperty;

public class PostHash extends BaseVo {
    @ApiModelProperty(required = true,value = "hashKey",example = "hashKey")
    private String key;
    @ApiModelProperty(required = true,value = "hashField",example = "hashField")
    private String field;
    @ApiModelProperty(value = "hashValue",example = "hashValue")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
