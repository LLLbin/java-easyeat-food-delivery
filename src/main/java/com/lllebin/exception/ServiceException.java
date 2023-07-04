package com.lllebin.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: ServiceException
 * Package: com.lllebin
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceException extends RuntimeException{
    private ServiceExceptionCode serviceExceptionCode;
}
