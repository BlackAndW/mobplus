package com.yeecloud.adplus.admin.controller.sys;

import com.yeecloud.adplus.admin.controller.sys.form.SysMenuForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysMenuVO;
import com.yeecloud.adplus.admin.service.SysMenuService;
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
 * 菜单信息
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
@RequestMapping("/api/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping
    @RequiresPermissions("sys:menu:query")
    public Result<SysMenuVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<SysMenuVO> result = sysMenuService.query(new Query(params));
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("sys:menu:info")
    public Result<SysMenuVO> info(@PathVariable Integer id) throws ServiceException {
        SysMenuVO result = sysMenuService.findById(id);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("sys:menu:create")
    public Result create(@RequestBody @Valid SysMenuForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysMenuService.create(null, form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("sys:menu:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid SysMenuForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysMenuService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("sys:menu:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        sysMenuService.delete(ids);
        return Result.SUCCESS();
    }

    /*============================================*/
    @GetMapping("/{id}/perm")
    @RequiresPermissions("sys:menu:perm")
    public Result<List<GrantPermVO>> getPermissons(@PathVariable Integer id, @RequestParam Map<String, Object> params) throws ServiceException {
        List<GrantPermVO> result = sysMenuService.findGrantPerms(id);
        return Result.SUCCESS(result);
    }

    @PostMapping("/{id}/perm")
    @RequiresPermissions("sys:menu:perm")
    public Result postPermissons(@PathVariable Integer id, @RequestBody List<Integer> permissions) throws ServiceException {
        sysMenuService.grantPermissions(id, permissions);
        return Result.SUCCESS();
    }

    /*============================================*/
    @GetMapping("/{parentId}/item")
    @RequiresPermissions("sys:menu:query")
    public Result<SysMenuVO> itemList(@PathVariable Integer parentId, @RequestParam Map<String, Object> params) throws ServiceException {
        Query query = new Query(params);
        query.put("parentId", parentId);
        PageInfo<SysMenuVO> result = sysMenuService.query(query);
        return Result.SUCCESS(result);
    }

    @PostMapping("/{parentId}/item")
    @RequiresPermissions("sys:menu:create")
    public Result createItem(@PathVariable Integer parentId, @RequestBody @Valid SysMenuForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysMenuService.create(parentId, form);
        return Result.SUCCESS();
    }
}