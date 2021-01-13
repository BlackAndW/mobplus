package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppMobileConfConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppMobileConfForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppMobileConfVO;
import com.yeecloud.adplus.admin.service.AppMobileConfService;
import com.yeecloud.adplus.dal.entity.AppMobileConf;
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
 * @create: 2020-12-15 09:33
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/mobile/conf")
public class AppMobileConfController {


    @Autowired
    private AppMobileConfService appMobileConfService;

    @Autowired
    private AppMobileConfConvert appMobileConfConvert;

    @GetMapping
    @RequiresPermissions("app:config:query")
    public Result<AppMobileConfVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppMobileConf> page = appMobileConfService.query(new Query(params));
        PageInfo<AppMobileConfVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:config:info")
    public Result<AppMobileConfVO> info(@PathVariable Integer id) throws ServiceException {
        AppMobileConf appConfig = appMobileConfService.findById(id);
        return Result.SUCCESS(appMobileConfConvert.convert(appConfig));
    }


    @PostMapping
    @RequiresPermissions("app:config:create")
    public Result create(@RequestBody @Valid AppMobileConfForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appMobileConfService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:config:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppMobileConfForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appMobileConfService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:config:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appMobileConfService.delete(ids);
        return Result.SUCCESS();
    }

    private PageInfo<AppMobileConfVO> convert(Page<AppMobileConf> result) {
        List<AppMobileConfVO> resultList = appMobileConfConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

}
