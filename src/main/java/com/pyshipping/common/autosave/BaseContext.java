package com.pyshipping.common.autosave;

/**
 *  基于 ThreadLocal 封装
 *  当前线程局部变量
 *  用于request上下文之外
 *  获取共享数据 例如当前登录用户ID
 */
public class BaseContext {
    private static ThreadLocal<String> employeeThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

    public static void setEmployeeId(String id) {
        employeeThreadLocal.set(id);
    }
    public static String getEmployeeId() {
        return employeeThreadLocal.get();
    }

    public static void setUserId(String id) {
        userThreadLocal.set(id);
    }
    public static String getUserId() {
        return userThreadLocal.get();
    }
}
