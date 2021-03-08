package com.yeecloud.adplus.admin.controller.app;

import com.yeecloud.adplus.admin.controller.app.convert.AppFunctionConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppFunctionForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppFunctionVO;
import com.yeecloud.adplus.admin.service.AppFunctionService;
import com.yeecloud.adplus.dal.entity.AppFunction;
import com.yeecloud.adplus.dal.entity.AppVersion;
import com.yeecloud.adplus.dal.entity.Channel;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/function")
public class AppFunctionController {
    
    @Autowired
    AppFunctionService appFunctionService;
    
    @Autowired
    AppFunctionConvert appFunctionConvert;

    @GetMapping
    @RequiresPermissions("app:entity:query")
    public Result list(@RequestParam Map<String, Object> params) throws ServiceException {

        Page<AppFunction> page = appFunctionService.query(new Query(params));
        PageInfo<AppFunctionVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:function:info")
    public Result info(@PathVariable Integer id) throws ServiceException {
        AppFunction result = appFunctionService.findById(id);
        return Result.SUCCESS(appFunctionConvert.convert(result));
    }

    @PostMapping
    @RequiresPermissions("app:function:create")
    public Result create(@RequestBody @Valid AppFunctionForm form, BindingResult bindingResult) throws ServiceException {
        log.info("Form: " + form);
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
        }
        appFunctionService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:function:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppFunctionForm form, BindingResult bindingResult) throws ServiceException {
        log.info("form: " + form );
        if (bindingResult.hasErrors()){
            return Result.FAILURE(getErrorMessage(bindingResult));
        }
        appFunctionService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:function:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appFunctionService.delete(ids);
        return Result.SUCCESS();
    }

    private PageInfo<AppFunctionVO> convert(Page<AppFunction> result) {
        List<AppFunctionVO> resultList = appFunctionConvert.convert(result.getContent());
        System.out.println(resultList);
        formatString2List(resultList, result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private String getErrorMessage(BindingResult bindingResult){
        return String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
    }

    /**
     * 将结果集里的String转换为VO对象的List<String>
     * @param appFunctionVOList
     * @param result
     */
    private void formatString2List (List<AppFunctionVO> appFunctionVOList, List<AppFunction> result) {
        result.forEach( resultItem ->
            appFunctionVOList.forEach(appFunctionVO -> {
                if (resultItem.getId().equals(appFunctionVO.getId())) {
                    appFunctionVO.setAppVersionCheckList(Arrays.asList(resultItem.getAppVersionList().split("\\|")));
                    appFunctionVO.setChannelCheckList(Arrays.asList(resultItem.getChannelList().split("\\|")));
                }
        }));
    }
}
