package com.pyshipping.common.msg;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pyshipping.common.codes.Codes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DataTruncation;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalException {
    // 业务异常
    @ExceptionHandler(CustomException.class)
    public Msg<String> customExceptionHandler(CustomException ex) {
        return Msg.error(Codes.Internal,ex.getMessage(),"access error");
    }

    // token异常
    @ExceptionHandler(value = JWTDecodeException.class)
    public Msg<String> jwtExceptionHandler(JWTDecodeException ex) {
        return Msg.error(Codes.InvalidArgument,"token error",ex.getMessage());
    }
    // token过期
    @ExceptionHandler(value = TokenExpiredException.class)
    public Msg<String> tokenExpiredHandler(TokenExpiredException ex) {
        return Msg.error(Codes.InvalidArgument,"token 过期",ex.getMessage());
    }
    // sql异常
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public Msg<String> sqlViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message;
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            message = split[2] + "已存在";
        } else {
            message = "param error";
        }
        return Msg.error(Codes.InvalidArgument,message,ex.getMessage());
    }

    // sql异常
    @ExceptionHandler(value = DataTruncation.class)
    public Msg<String> mysqlDataTruncationExceptionHandler(DataTruncation ex) {
        String message;
        if (ex.getMessage().contains("Data truncation")) {
            String[] split = ex.getMessage().split(" ");
            message = split[7] + "格式错误";
        } else {
            message = "param error";
        }
        return Msg.error(Codes.InvalidArgument,message,ex.getMessage());
    }
    //sql异常
    @ExceptionHandler(value = SQLException.class)
    public Msg<String> sqlExceptionHandler(SQLException ex) {
        return Msg.error(Codes.InvalidArgument,"数据参数错误",ex.getMessage());
    }
    // 文件异常
    @ExceptionHandler(value = IOException.class)
    public Msg<String> fileNotFoundExceptionHandler(IOException ex) {
        return Msg.error(Codes.InvalidArgument,"文件不存在",ex.getMessage());
    }
}
