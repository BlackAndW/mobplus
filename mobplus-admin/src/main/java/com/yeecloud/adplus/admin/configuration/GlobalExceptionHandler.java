package com.yeecloud.adplus.admin.configuration;

import com.yeecloud.adplus.admin.StatusCode;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

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

    @ExceptionHandler({UnauthorizedException.class})
    public Result handler(UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return Result.FAILURE(StatusCode.NO_AUTH.getCode(), e.getMessage());
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public Result handler(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return Result.FAILURE(StatusCode.KEY_DUPLICATE.getCode(), e.getMessage());
    }
}
