package com.pyshipping.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pyshipping.common.msg.CustomException;
import com.pyshipping.common.msg.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class ExceptionController {
    @RequestMapping("/unauthenticated")
    public void unauthenticated(HttpServletRequest request) {
        // 重新抛出异常 使用全局异常处理
        log.error(request.getAttribute("authException").toString());
        if (request.getAttribute("authException") instanceof JWTDecodeException) {
            throw ((JWTDecodeException) request.getAttribute("authException"));
        } else if(request.getAttribute("authException") instanceof TokenExpiredException){
            throw ((TokenExpiredException) request.getAttribute("authException"));
        }else {
            throw ((CustomException) request.getAttribute("authException"));
        }
    }
}
