package com.cloud.rediswebmanager.vo;

public enum KeyType {
    STRING("string"),LIST("list"),HASH("hash"),SET("set"),ZSET("zset");
    private String type;

    KeyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
