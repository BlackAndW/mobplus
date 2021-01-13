package com.yeecloud.adplus.admin.controller.sys;

import com.yeecloud.adplus.admin.controller.sys.form.SysConfForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysConfVO;
import com.yeecloud.adplus.admin.service.SysConfService;
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
 * 系统配置
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
@RequestMapping("/api/sys/conf")
public class SysConfController {

    @Autowired
    private SysConfService sysConfService;

    @GetMapping
    @RequiresPermissions("sys:conf:query")
    public Result<SysConfVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<SysConfVO> result = sysConfService.query(new Query(params));
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("sys:conf:info")
    public Result<SysConfVO> info(@PathVariable Integer id) throws ServiceException {
        SysConfVO result = sysConfService.findById(id);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("sys:conf:create")
    public Result create(@RequestBody @Valid SysConfForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysConfService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("sys:conf:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid SysConfForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysConfService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("sys:conf:delete")
    public Result delete(@RequestBody Integer [] ids) throws ServiceException {
        sysConfService.delete(ids);
        return Result.SUCCESS();
    }
}