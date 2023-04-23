package com.pyshipping.common.msg;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Msg<T> implements Serializable {
    private Integer code;
    private String msg;
    private String error;
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
