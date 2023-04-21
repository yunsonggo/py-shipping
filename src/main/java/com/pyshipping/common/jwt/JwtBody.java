package com.pyshipping.common.jwt;

import com.alibaba.fastjson.JSON;

public class JwtBody {
    private String userID;
    private String username;
    private String password;
    private Integer status;

    public JwtBody(String userID, String username, String password, Integer status) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static JwtBody toJwtBody (String strBody) {
        return JSON.parseObject(strBody,JwtBody.class);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
