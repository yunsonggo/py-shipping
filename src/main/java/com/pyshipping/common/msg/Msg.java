package com.pyshipping.common.msg;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel("返回数据")
public class Msg<T> implements Serializable {
    private static final long serialVersionUID = -1;
    @ApiModelProperty("响应返回码")
    private Integer code;
    @ApiModelProperty("返回消息")
    private String msg;
    @ApiModelProperty("返回错误")
    private String error;
    @ApiModelProperty("返回数据")
    private T data;
    private Map map = new HashMap();

    public static <T> Msg<T> success(Integer code, String msg, T object) {
        Msg<T> r = new Msg<T>();
        r.data = object;
        r.code = code;
        r.msg = msg;
        r.error = null;
        return r;
    }

    public static <T> Msg<T> error(Integer code, String msg, String err) {
        Msg<T> r = new Msg<T>();
        r.data = null;
        r.msg = msg;
        r.code = code;
        r.error = err;
        return r;
    }

    public Msg<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
