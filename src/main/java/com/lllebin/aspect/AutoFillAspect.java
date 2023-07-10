package com.lllebin.aspect;

import com.lllebin.utils.BaseContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * ClassName: AutoFillAspect
 * Package: com.lllebin.aop
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@Aspect
@Component
public class AutoFillAspect {
    /**
     * 前置通知，给INSERT公共字段赋值
     */
    @Before("execution(* com.lllebin.mapper.*Mapper.insert*(..))")
    public void insertAutoFill(JoinPoint joinPoint){
        log.info("填充INSERT公共字段...");

        // 1.方法的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        // 2.赋值
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContextUtils.getCurrentId();

        try {
            Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
            setCreateTime.invoke(entity, now);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
            setCreateUser.invoke(entity, currentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
            setUpdateTime.invoke(entity, now);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
            setUpdateUser.invoke(entity, currentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("INSERT公共字段填充完毕：{}", entity);
    }

    /**
     * 前置通知，给UPDATE公共字段赋值
     */
    @Before("execution(* com.lllebin.mapper.*Mapper.update*(..))")
    public void updateAutoFill(JoinPoint joinPoint){
        log.info("填充UPDATE公共字段...");

        // 1.方法的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        // 2.赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContextUtils.getCurrentId();

        // 3.赋值
        try {
            Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
            setUpdateTime.invoke(entity, now);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
            setUpdateUser.invoke(entity, currentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("UPDATE公共字段填充完毕：{}", entity);
    }
}
