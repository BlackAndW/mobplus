package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppVersionConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppVersionForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppVersionVO;
import com.yeecloud.adplus.admin.service.AppVersionService;
import com.yeecloud.adplus.dal.entity.AppVersion;
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
 * @create: 2020-12-02 14:43
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/version")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @Autowired
    private AppVersionConvert appVersionConvert;

    @GetMapping
    @RequiresPermissions("app:version:query")
    public Result<AppVersionVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppVersion> page = appVersionService.query(new Query(params));
        PageInfo<AppVersionVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:version:info")
    public Result<AppVersionVO> info(@PathVariable Integer id) throws ServiceException {
        AppVersion appVersion = appVersionService.findById(id);
        return Result.SUCCESS(appVersionConvert.convert(appVersion));
    }

    @PostMapping
    @RequiresPermissions("app:version:create")
    public Result create(@RequestBody @Valid AppVersionForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appVersionService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:version:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppVersionForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appVersionService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:version:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appVersionService.delete(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/sct")
    @RequiresPermissions("app:project:query")
    public Result<AppVersionVO> select(@RequestParam Map<String, Object> params) throws ServiceException {
        params.put("pageSize", Integer.MAX_VALUE);
        Query query = new Query(params);
        query.put("status", 2);
        Page<AppVersion> page = appVersionService.query(query);
        PageInfo<AppVersionVO> result = convert(page);
        return Result.SUCCESS(result.getData());
    }

    private PageInfo<AppVersionVO> convert(Page<AppVersion> result) {
        List<AppVersionVO> resultList = appVersionConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
