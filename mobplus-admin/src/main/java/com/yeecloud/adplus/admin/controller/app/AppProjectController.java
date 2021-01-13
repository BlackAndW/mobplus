package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppProjectConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppProjectForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppProjectVO;
import com.yeecloud.adplus.admin.service.AppProjectService;
import com.yeecloud.adplus.admin.service.AppProjectService;
import com.yeecloud.adplus.dal.entity.AppProject;
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
 * @create: 2020-12-02 15:58
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/project")
public class AppProjectController {

    @Autowired
    private AppProjectService appProjectService;

    @Autowired
    private AppProjectConvert appProjectConvert;

    @GetMapping
    @RequiresPermissions("app:project:query")
    public Result<AppProjectVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppProject> page = appProjectService.query(new Query(params));
        PageInfo<AppProjectVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:project:info")
    public Result<AppProjectVO> info(@PathVariable Integer id) throws ServiceException {
        AppProject appProject = appProjectService.findById(id);
        return Result.SUCCESS(appProjectConvert.convert(appProject));
    }

    @PostMapping
    @RequiresPermissions("app:project:create")
    public Result create(@RequestBody @Valid AppProjectForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appProjectService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:project:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppProjectForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appProjectService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:project:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appProjectService.delete(ids);
        return Result.SUCCESS();
    }

    private PageInfo<AppProjectVO> convert(Page<AppProject> result) {
        List<AppProjectVO> resultList = appProjectConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
