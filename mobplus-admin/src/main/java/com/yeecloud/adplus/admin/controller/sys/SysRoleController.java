package com.yeecloud.adplus.admin.controller.sys;

import com.google.common.collect.Lists;
import com.yeecloud.adplus.admin.controller.sys.form.SysRoleForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysRoleVO;
import com.yeecloud.adplus.admin.service.SysRoleService;
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
import java.util.List;
import java.util.Map;

/**
 * 系统角色
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
@RequestMapping("/api/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping
    @RequiresPermissions("sys:role:query")
    public Result<SysRoleVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<SysRoleVO> result = sysRoleService.query(new Query(params));
        return Result.SUCCESS(result);
    }

    @GetMapping("/sct")
    @RequiresPermissions("sys:role:query")
    public Result<List<SysRoleVO>> roleList(@RequestParam Map<String, Object> params) throws ServiceException {
        List<SysRoleVO> resultList = Lists.newArrayList();
        List<SysRoleVO> result = sysRoleService.query(new Query(params)).getData();
        result.stream().forEach(vo -> {
            SysRoleVO dst = new SysRoleVO();
            dst.setId(vo.getId());
            dst.setCode(vo.getCode());
            dst.setName(vo.getName());
            resultList.add(dst);
        });
        return Result.SUCCESS(resultList);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("sys:role:info")
    public Result<SysRoleVO> info(@PathVariable Integer id) throws ServiceException {
        SysRoleVO result = sysRoleService.findById(id);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("sys:role:create")
    public Result create(@RequestBody @Valid SysRoleForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysRoleService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("sys:role:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid SysRoleForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysRoleService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        sysRoleService.delete(ids);
        return Result.SUCCESS();
    }

    /*==========================================================*/
    @GetMapping("/{id}/perm")
    @RequiresPermissions("sys:role:perm")
    public Result<List<GrantPermVO>> getPermissons(@PathVariable Integer id, @RequestParam Map<String, Object> params) throws ServiceException {
        List<GrantPermVO> result = sysRoleService.findGrantPerms(id);
        return Result.SUCCESS(result);
    }

    @PostMapping("/{id}/perm")
    @RequiresPermissions("sys:role:perm")
    public Result postPermissons(@PathVariable Integer id, @RequestBody List<Integer> permissions) throws ServiceException {
        sysRoleService.grantPermissions(id, permissions);
        return Result.SUCCESS();
    }
}