package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppActivityConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityAwardForm;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityForm;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityTaskForm;
import com.yeecloud.adplus.admin.controller.app.vo.*;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.admin.service.AppActivityService;
import com.yeecloud.adplus.admin.service.AppService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.AppActivityAward;
import com.yeecloud.adplus.dal.entity.AppActivityTask;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.Arrays;
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
    public Result list(@RequestParam Map<String, Object> params) throws ServiceException {

        Page<AppActivity> page = appActivityService.query(new Query(params));
        PageInfo<AppActivityVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:activity:info")
    public Result info(@PathVariable Integer id) throws ServiceException {
        AppActivity result = appActivityService.findById(id);
        return Result.SUCCESS(appActivityConvert.convert(result));
    }

    @PostMapping
    @RequiresPermissions("app:activity:create")
    public Result create(@RequestBody @Valid AppActivityForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
        }
        appActivityService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:activity:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppActivityForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
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

    @GetMapping("/task")
    @RequiresPermissions("app:entity:query")
    public Result taskList(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppActivityTask> taskList = appActivityService.queryTask(new Query(params));
        List<AppActivityTaskVO> resultList = appActivityConvert.convertTask(taskList.getContent());
        PageInfo<AppActivityTaskVO> result = new PageInfo<>(taskList.getNumber() + 1, taskList.getSize(), (int) taskList.getTotalElements(), resultList);
        return Result.SUCCESS(result);
    }

    @GetMapping("/task/{id}")
    @RequiresPermissions("app:activity:edit")
    public Result taskInfo(@PathVariable Integer id) throws ServiceException {
        AppActivityTask result = appActivityService.findByTaskId(id);
        return Result.SUCCESS(appActivityConvert.convertTask(result));
    }

    @PostMapping("/task")
    @RequiresPermissions("app:activity:create")
    public Result createTask(@RequestBody @Valid AppActivityTaskForm TaskForm, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
        }
        appActivityService.create(TaskForm);
        return Result.SUCCESS();
    }

    @PutMapping("task/{id}")
    @RequiresPermissions("app:activity:edit")
    public Result updateTask(@PathVariable Integer id, @RequestBody @Valid AppActivityTaskForm TaskForm, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
        }
        appActivityService.update(id, TaskForm);
        return Result.SUCCESS();
    }

    @DeleteMapping("task")
    @RequiresPermissions("app:activity:delete")
    public Result deleteTask(@RequestBody Integer[] ids) throws ServiceException {
        appActivityService.deleteTask(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/award")
    @RequiresPermissions("app:entity:query")
    public Result awardList(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppActivityAward> awardList = appActivityService.queryAward(new Query(params));
        List<AppActivityAwardVO> resultList = appActivityConvert.convertAward(awardList.getContent());
        PageInfo<AppActivityAwardVO> result = new PageInfo<>(awardList.getNumber() + 1, awardList.getSize(), (int) awardList.getTotalElements(), resultList);
        return Result.SUCCESS(result);
    }

    @GetMapping("/award/{id}")
    @RequiresPermissions("app:activity:edit")
    public Result awardInfo(@PathVariable Integer id) throws ServiceException {
        AppActivityAward result = appActivityService.findByAwardId(id);
        return Result.SUCCESS(appActivityConvert.convertAward(result));
    }

    @PostMapping("/award")
    @RequiresPermissions("app:activity:create")
    public Result createAward(@RequestBody @Valid AppActivityAwardForm AwardForm, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
        }
        appActivityService.create(AwardForm);
        return Result.SUCCESS();
    }

    @PutMapping("award/{id}")
    @RequiresPermissions("app:activity:edit")
    public Result updateAward(@PathVariable Integer id, @RequestBody @Valid AppActivityAwardForm AwardForm, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
        }
        appActivityService.update(id, AwardForm);
        return Result.SUCCESS();
    }

    @DeleteMapping("award")
    @RequiresPermissions("app:activity:delete")
    public Result deleteAward(@RequestBody Integer[] ids) throws ServiceException {
        appActivityService.deleteAward(ids);
        return Result.SUCCESS();
    }

    private PageInfo<AppActivityVO> convert(Page<AppActivity> result) {
        List<AppActivityVO> resultList = appActivityConvert.convert(result.getContent());
        formatString2List(resultList, result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private String getErrorMessage(BindingResult bindingResult){
        return String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
    }

    private void formatString2List (List<AppActivityVO> appActivityVOList, List<AppActivity> result) {
        result.forEach( resultItem ->
            appActivityVOList.forEach(appActivityVO -> {
                if (resultItem.getId().equals(appActivityVO.getId())) {
                    appActivityVO.setAppVersionCheckList(Arrays.asList(resultItem.getAppVersionList().split("\\|")));
                    appActivityVO.setChannelCheckList(Arrays.asList(resultItem.getChannelList().split("\\|")));
                }
        }));
    }
}
