package com.pyshipping.common.keys;

import org.springframework.stereotype.Component;

@Component
public class Keys {

    public String getSmsRedisKey(String phone) {
        return "sms:" + phone;
    }

    public String getCategoryGoodsRedisKey(String categoryId, Integer status, Integer page, Integer size) {
        return "category:goods:" + categoryId + ":" + page + "_" + size + "_" + status;
    }

    public String getCategoryGoodsRedisKeyPrefix() {
        return "category:goods:*";
    }

    public String getCategoryGoodsTotalRedisKey(String categoryId) {
        return "category:goods:" + categoryId + ":total";
    }

    public String getCategorySetmealRedisKey(String categoryId, Integer status, Integer page, Integer size) {
        return "category:setmeal:" + categoryId + ":" + page + "_" + size + "_" + status;
    }

    public String getCategorySetmealRedisKeyPrefix() {
        return "category:setmeal:*";
    }

    public String getCategorySetmealTotalRedisKey(String categoryId) {
        return "category:setmeal:" + categoryId + ":total";
    }
}
