package com.yeecloud.adplus.gateway.configuration;

import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Title
 *
 * Date: 2019-11-06 17:18:59
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Throwable.class})
    public Result handler(Throwable e) {
        log.error(e.getMessage(), e);
        return Result.FAILURE(e.getMessage());
    }

    @ExceptionHandler({ServiceException.class})
    public Result handler(ServiceException e) {
        log.error(e.getMessage(), e);
        return Result.FAILURE(e.getCode(), e.getMessage());
    }
}
