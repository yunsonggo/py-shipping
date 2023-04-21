package com.pyshipping.common.pwd;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class Pwd {
    public String encode(String password) {
       return DigestUtils.sha256Hex(password);
    }

    public boolean check(String password,String encodedPassword) {
        return encodedPassword.equals(DigestUtils.sha256Hex(password));
    }
}
