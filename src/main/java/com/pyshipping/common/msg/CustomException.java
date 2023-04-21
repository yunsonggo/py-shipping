package com.pyshipping.common.msg;

/**
 * 自定义业务异常
 * 处理业务中的异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
