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
    TARGET_ALREADY_EXISTS("target already exists"),
    TARGET_NOT_EXISTS("target does not exist"),
    TARGET_IS_ASSOCITED("target is associted to others");
    String description;
}
