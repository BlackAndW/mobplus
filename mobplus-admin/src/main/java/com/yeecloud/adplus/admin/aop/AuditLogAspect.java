package com.yeecloud.adplus.admin.aop;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.common.form.BaseForm;
import com.yeecloud.adplus.admin.entity.SysAuditLog;
import com.yeecloud.adplus.admin.repository.SysAuditLogRepository;
import com.yeecloud.adplus.admin.security.UserPrincipal;
import com.yeecloud.meeto.common.util.ParamUtils;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Title
 *
 * Date: 2020-04-20 11:52:10
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Aspect
@Component
public class AuditLogAspect {

    @Autowired
    private SysAuditLogRepository sysAuditLogRepository;

    @Around("@annotation(org.apache.shiro.authz.annotation.RequiresPermissions)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = this.getSpecificMethod(joinPoint);
        RequiresPermissions permissions = method.getAnnotation(RequiresPermissions.class);
        if (permissions == null || permissions.value().length == 0) {
            return joinPoint.proceed();
        }
        String action = permissions.value()[0];
        if (StringUtils.containsIgnoreCase(action, "query")) {
            return joinPoint.proceed();
        }
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            result = e.getMessage();
            throw e;
        } finally {
            auditLog(action, joinPoint, result);
        }
        return result;
    }

    private void auditLog(String action, ProceedingJoinPoint joinPoint, Object result) {
        String response = result != null ? JSONObject.toJSONString(result) : "";
        if (response.length() > 1000) {
            response = response.substring(0, 1000);
        }
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        SysAuditLog auditLog = new SysAuditLog();
        auditLog.setAction(action);
        auditLog.setIpAddr(ParamUtils.getIpAddr(request));
        auditLog.setRequestUrl(String.valueOf(request.getRequestURL()));
        auditLog.setParameters(ParamUtils.parameterToString(request));
        auditLog.setDetail(parserArgs(joinPoint));
        auditLog.setStatus(Constants.STATE_ON);
        auditLog.setResponse(response);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityUtils.getSubject().getPrincipal();
        if (userPrincipal != null) {
            auditLog.setUserId(userPrincipal.getId());
        } else {
            auditLog.setUserId(0);
        }
        sysAuditLogRepository.save(auditLog);
    }

    private Method getSpecificMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // The method may be on an interface, but we need attributes from the
        // target class. If the target class is null, the method will be
        // unchanged.
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());
        if (targetClass == null && joinPoint.getTarget() != null) {
            targetClass = joinPoint.getTarget().getClass();
        }
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the
        // original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        return specificMethod;
    }

    private String parserArgs(ProceedingJoinPoint joinPoint) {
        JSONObject json = new JSONObject();
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String[] parameters = methodSignature.getParameterNames();
            for (int idx = 0; idx < joinPoint.getArgs().length; idx++) {
                Object arg = joinPoint.getArgs()[idx];
                if (arg instanceof Number || arg instanceof String || arg instanceof BaseForm
                        || (arg instanceof Number[])) {
                    json.put(parameters[idx], arg);
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return json.toJSONString();
    }
}
