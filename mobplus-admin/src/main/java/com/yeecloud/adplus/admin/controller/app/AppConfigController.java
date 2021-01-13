package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppConfigConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppConfigForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppConfigVO;
import com.yeecloud.adplus.admin.service.AppConfigService;
import com.yeecloud.adplus.dal.entity.AppConfig;
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
 * @create: 2020-12-02 17:31
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/config")
public class AppConfigController {

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private AppConfigConvert appConfigConvert;

    @GetMapping
    @RequiresPermissions("app:config:query")
    public Result<AppConfigVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppConfig> page = appConfigService.query(new Query(params));
        PageInfo<AppConfigVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:config:info")
    public Result<AppConfigVO> info(@PathVariable Integer id) throws ServiceException {
        AppConfig appConfig = appConfigService.findById(id);
        return Result.SUCCESS(appConfigConvert.convert(appConfig));
    }
    
    @PutMapping("/{id}")
    @RequiresPermissions("app:config:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppConfigForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appConfigService.update(id, form);
        return Result.SUCCESS();
    }

    @PutMapping("/ad/open")
    @RequiresPermissions("app:config:edit")
    public Result openAd(@RequestBody Integer[] ids) throws ServiceException {
        appConfigService.controlAd(ids,1);
        return Result.SUCCESS();
    }

    @PutMapping("/ad/close")
    @RequiresPermissions("app:config:edit")
    public Result closeAd(@RequestBody Integer[] ids) throws ServiceException {
        appConfigService.controlAd(ids,2);
        return Result.SUCCESS();
    }

    private PageInfo<AppConfigVO> convert(Page<AppConfig> result) {
        List<AppConfigVO> resultList = appConfigConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
