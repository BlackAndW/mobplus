package com.yeecloud.adplus.admin.controller.sys;

import com.yeecloud.adplus.admin.controller.sys.form.SysPermissionForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysPermissionVO;
import com.yeecloud.adplus.admin.service.SysPermissionService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 权限信息
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
@RequestMapping("/api/sys/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    @GetMapping
    @RequiresPermissions("sys:permission:query")
    public Result<SysPermissionVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<SysPermissionVO> result = sysPermissionService.query(new Query(params));
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("sys:permission:info")
    public Result<SysPermissionVO> info(@PathVariable Integer id) throws ServiceException {
        SysPermissionVO result = sysPermissionService.findById(id);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("sys:permission:create")
    public Result create(@RequestBody @Valid SysPermissionForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysPermissionService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("sys:permission:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid SysPermissionForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysPermissionService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("sys:permission:delete")
    public Result delete(@RequestBody Integer [] ids) throws ServiceException {
        sysPermissionService.delete(ids);
        return Result.SUCCESS();
    }
}