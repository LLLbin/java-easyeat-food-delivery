package com.lllebin.utils;

/**
 * ClassName: BaseContext
 * Package: com.lllebin.utils
 * Description: 基于ThreadLocal封装工具类，用于保存和获取当前登录的用户ID
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
public class BaseContextUtils {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
