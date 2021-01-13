package com.yeecloud.adplus.admin.controller.sys;

import com.yeecloud.adplus.admin.controller.sys.vo.SysAuditLogVO;
import com.yeecloud.adplus.admin.service.SysAuditLogService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 操作日志
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/sys/auditlog")
public class SysAuditLogController {

    @Autowired
    private SysAuditLogService sysAuditLogService;

    @GetMapping
    @RequiresPermissions("sys:auditlog:query")
    public Result<SysAuditLogVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<SysAuditLogVO> result = sysAuditLogService.query(new Query(params));
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("sys:auditlog:info")
    public Result<SysAuditLogVO> info(@PathVariable Integer id) throws ServiceException {
        SysAuditLogVO result = sysAuditLogService.findById(id);
        return Result.SUCCESS(result);
    }

    @DeleteMapping
    @RequiresPermissions("sys:auditlog:delete")
    public Result delete(@RequestParam(value = "clear", defaultValue = "0") Integer clear, @RequestBody Integer[] ids) throws ServiceException {
        if (clear == 1) {
            sysAuditLogService.deleteAll();
        } else {
            sysAuditLogService.delete(ids);
        }
        return Result.SUCCESS();
    }
}