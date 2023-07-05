package com.lllebin.anno;

import com.lllebin.aspect.AutoFillType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoFill
 * Package: com.lllebin.annotation
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    AutoFillType value();
}
