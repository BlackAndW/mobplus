package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppAdvertiserConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppAdvertiserForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppAdvertiserVO;
import com.yeecloud.adplus.admin.service.AppAdvertiserService;
import com.yeecloud.adplus.dal.entity.AppAdvertiser;
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
 * @create: 2020-12-09 10:51
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/advertiser")
public class AppAdvertiserController {

    @Autowired
    private AppAdvertiserConvert appAdvertiserConvert;

    @Autowired
    private AppAdvertiserService appAdvertiserService;

    @GetMapping
    @RequiresPermissions("app:advertiser:query")
    public Result<AppAdvertiserVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppAdvertiser> page = appAdvertiserService.query(new Query(params));
        PageInfo<AppAdvertiserVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:advertiser:info")
    public Result<AppAdvertiserVO> info(@PathVariable Integer id) throws ServiceException {
        AppAdvertiser appConfig = appAdvertiserService.findById(id);
        return Result.SUCCESS(appAdvertiserConvert.convert(appConfig));
    }


    @PostMapping
    @RequiresPermissions("app:advertiser:create")
    public Result create(@RequestBody @Valid AppAdvertiserForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appAdvertiserService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:advertiser:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppAdvertiserForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appAdvertiserService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:advertiser:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appAdvertiserService.delete(ids);
        return Result.SUCCESS();
    }

    private PageInfo<AppAdvertiserVO> convert(Page<AppAdvertiser> result) {
        List<AppAdvertiserVO> resultList = appAdvertiserConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

}
