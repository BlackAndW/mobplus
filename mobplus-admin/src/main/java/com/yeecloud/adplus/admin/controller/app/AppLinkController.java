package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppLinkConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppLinkForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppLinkVO;
import com.yeecloud.adplus.admin.controller.cms.form.ChargeBannerForm;
import com.yeecloud.adplus.admin.controller.cms.vo.ChargeBannerVO;
import com.yeecloud.adplus.admin.service.AppLinkService;
import com.yeecloud.adplus.dal.entity.AppLink;
import com.yeecloud.adplus.dal.entity.ChargeBanner;
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
 * @create: 2021/7/16
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/link")
public class AppLinkController {

    @Autowired
    AppLinkService appLinkService;

    @Autowired
    AppLinkConvert appLinkConvert;

    @GetMapping
    @RequiresPermissions("app:link:query")
    public Result getLink(@RequestParam Map<String, Object> params) throws Exception {
        Page<AppLink> page = appLinkService.query(new Query(params));
        PageInfo<AppLinkVO> result = convertAppLink(page);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("app:link:create")
    public Result create(@RequestBody @Valid AppLinkForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appLinkService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:link:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppLinkForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appLinkService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:link:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appLinkService.delete(ids);
        return Result.SUCCESS();
    }

    private PageInfo<AppLinkVO> convertAppLink(Page<AppLink> result) {
        List<AppLinkVO> resultList = appLinkConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
