package com.lllebin.controller;

import com.lllebin.response.CommonResponse;
import com.lllebin.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ExceptionHandlerController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ServiceException.class)
    public CommonResponse<String> exceptionHandler(ServiceException e) {
        log.info(e.getServiceExceptionCode().getDescription());
        return CommonResponse.error("操作失败");
    }
}
