package com.yeecloud.adplus.admin.controller.sys;

import com.yeecloud.adplus.admin.controller.sys.form.SysCategoryForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysCategoryVO;
import com.yeecloud.adplus.admin.service.SysCategoryService;
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
 * 数据分类
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
@RequestMapping("/api/sys/category")
public class SysCategoryController {

    @Autowired
    private SysCategoryService sysCategoryService;

    @GetMapping
    @RequiresPermissions("sys:category:query")
    public Result<SysCategoryVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Query query = new Query(params);
        query.put("parentId", 0);
        query.put("recursion", true);

        PageInfo<SysCategoryVO> result = sysCategoryService.query(query);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("sys:category:info")
    public Result<SysCategoryVO> info(@PathVariable Integer id) throws ServiceException {
        SysCategoryVO result = sysCategoryService.findById(id);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("sys:category:create")
    public Result create(@RequestBody @Valid SysCategoryForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysCategoryService.create(null, form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("sys:category:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid SysCategoryForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysCategoryService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("sys:category:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        sysCategoryService.delete(ids);
        return Result.SUCCESS();
    }

    /*==============================================*/
    @GetMapping("/{parentId}/item")
    @RequiresPermissions("sys:category:query")
    public Result<PageInfo<SysCategoryVO>> itemList(@PathVariable Integer parentId, @RequestParam Map<String, Object> params) throws ServiceException {
        Query query = new Query(params);
        query.put("parentId", parentId);
        query.put("recursion", true);
        PageInfo<SysCategoryVO> result = sysCategoryService.query(query);
        return Result.SUCCESS(result);
    }

    @PostMapping("/{parentId}/item")
    @RequiresPermissions("sys:category:create")
    public Result createItem(@PathVariable Integer parentId, @RequestBody @Valid SysCategoryForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        sysCategoryService.create(parentId, form);
        return Result.SUCCESS();
    }
}