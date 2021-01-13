package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppVO;
import com.yeecloud.adplus.admin.service.AppService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-12-02 10:06
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/entity")
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private AppConvert appConvert;

    @GetMapping
    @RequiresPermissions("app:entity:query")
    public Result<AppVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<App> page = appService.query(new Query(params));
        PageInfo<AppVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:entity:info")
    public Result<AppVO> info(@PathVariable Integer id) throws ServiceException {
        App app = appService.findById(id);
        return Result.SUCCESS(appConvert.convert(app));
    }

    @PostMapping
    @RequiresPermissions("app:entity:create")
    public Result create(@RequestBody @Valid AppForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:entity:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:entity:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appService.delete(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/item")
    @RequiresPermissions("app:entity:query")
    public Result<AppVO> getAppItem(@RequestParam Map<String, Object> params) throws ServiceException {
        params.put("pageSize", Integer.MAX_VALUE);
        Query query = new Query(params);
        query.put("status", "2");
        Page<App> page = appService.query(query);
        PageInfo<AppVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    private PageInfo<AppVO> convert(Page<App> result) {
        List<AppVO> resultList = appConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
