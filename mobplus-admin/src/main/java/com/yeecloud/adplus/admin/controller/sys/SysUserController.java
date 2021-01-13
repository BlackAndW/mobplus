package com.yeecloud.adplus.admin.controller.sys;

import com.yeecloud.adplus.admin.controller.sys.form.SysPasswdForm;
import com.yeecloud.adplus.admin.controller.sys.form.SysUserForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysUserVO;
import com.yeecloud.adplus.admin.service.SysUserService;
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
 * 系统用户
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
@RequestMapping("/api/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping
    @RequiresPermissions("sys:user:query")
    public Result<SysUserVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Query query = new Query(params);
        query.put("withRoles", true);
        PageInfo<SysUserVO> result = sysUserService.query(query);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("sys:user:info")
    public Result<SysUserVO> info(@PathVariable Integer id) throws ServiceException {
        SysUserVO result = sysUserService.findById(id);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("sys:user:create")
    public Result create(@RequestBody @Valid SysUserForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysUserService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("sys:user:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid SysUserForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysUserService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        sysUserService.delete(ids);
        return Result.SUCCESS();
    }


    /*=========================================*/
    @PutMapping("/{id}/passwd")
    @RequiresPermissions("sys:user:passwd")
    public Result update(@PathVariable Integer id, @RequestBody @Valid SysPasswdForm form, BindingResult bindingResult) throws ServiceException {
        sysUserService.updatePasswd(id, form);
        return Result.SUCCESS();
    }
}