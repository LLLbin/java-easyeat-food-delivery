package com.lllebin.exception;

import lombok.*;

/**
 * ClassName: ServiceExceptionCode
 * Package: com.lllebin.exception
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ServiceExceptionCode {
    TARGET_ALREADY_EXISTS("target already exists");

    String description;
}