package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppActivityConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.admin.controller.app.vo.AppVO;
import com.yeecloud.adplus.admin.service.AppActivityService;
import com.yeecloud.adplus.admin.service.AppService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppActivity;
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
 * @author: Leonard
 * @create: 2021/1/18
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/activity")
public class AppActivityController {

    @Autowired
    AppActivityService appActivityService;

    @Autowired
    AppActivityConvert appActivityConvert;

    @GetMapping
    @RequiresPermissions("app:entity:query")
    public Result<AppActivityVO> list(@RequestParam Map<String, Object> params) throws ServiceException {

        Page<AppActivity> page = appActivityService.query(new Query(params));
        log.info("beforeConvert: " + page.getContent().toString());
        PageInfo<AppActivityVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:activity:info")
    public Result<AppActivityVO> info(@PathVariable Integer id) throws ServiceException {
        AppActivity result = appActivityService.findById(id);
        return Result.SUCCESS(appActivityConvert.convert(result));
    }

    @PostMapping
    @RequiresPermissions("app:activity:create")
    public Result create(@RequestBody @Valid AppActivityForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appActivityService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:activity:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppActivityForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appActivityService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:activity:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appActivityService.delete(ids);
        return Result.SUCCESS();
    }

    private PageInfo<AppActivityVO> convert(Page<AppActivity> result) {
        List<AppActivityVO> resultList = appActivityConvert.convert(result.getContent());
        log.info("afterConvert: " + resultList.toString());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
