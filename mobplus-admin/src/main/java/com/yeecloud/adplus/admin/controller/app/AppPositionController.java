package com.yeecloud.adplus.admin.controller.app;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yeecloud.adplus.admin.controller.ad.convert.AdPositionConvert;
import com.yeecloud.adplus.admin.controller.app.convert.AppPosAdPosConvert;
import com.yeecloud.adplus.admin.controller.app.convert.AppPositionConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppPositionAdPositionForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppPositionAdPositionVO;
import com.yeecloud.adplus.admin.controller.app.vo.AppPositionVO;
import com.yeecloud.adplus.admin.service.AdPositionService;
import com.yeecloud.adplus.admin.service.AppPositionService;
import com.yeecloud.adplus.dal.entity.AdPosition;
import com.yeecloud.adplus.dal.entity.AppPosition;
import com.yeecloud.adplus.dal.entity.AppPositionAdPosition;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.result.ResultCode;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author: Huang
 * @create: 2020-12-08 17:19
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/position")
public class AppPositionController {

    @Autowired
    private AppPositionService appPositionService;

    @Autowired
    private AdPositionService adPositionService;

    @Autowired
    private AppPositionConvert appPositionConvert;

    @Autowired
    private AdPositionConvert adPositionConvert;

    @Autowired
    private AppPosAdPosConvert appPosAdPosConvert;

    @GetMapping
    @RequiresPermissions("app:position:query")
    public Result<AppPositionVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AppPosition> page = appPositionService.query(new Query(params));
        PageInfo<AppPositionVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("app:position:info")
    public Result<AppPositionVO> info(@PathVariable Integer id) throws ServiceException {
        AppPosition result = appPositionService.findById(id);
        return Result.SUCCESS(appPositionConvert.convert(result));
    }

    @PostMapping
    @RequiresPermissions("app:position:create")
    public Result create(@RequestBody @Valid AppPosition form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appPositionService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:position:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AppPosition form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appPositionService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("app:position:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        appPositionService.delete(ids);
        return Result.SUCCESS();
    }

    /*===================================================*/
    @GetMapping("/ad/{appPosId}")
    @RequiresPermissions("app:position:query")
    public Result<PageInfo<AppPositionAdPositionVO>> getAdPos(@PathVariable Integer appPosId) throws ServiceException {
        AppPosition position = appPositionService.findById(appPosId);
        if (position == null) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageSize", Integer.MAX_VALUE);
        params.put("appId", position.getApp().getId());

        // 将相同名字的广告位和展示位筛选出来
        String[] split = position.getName().split("-");
        params.put("name",split[0]);

        Set<Integer> existsAdPosSet = Sets.newHashSet();
        List<AppPositionAdPositionVO> resultList = Lists.newArrayList();

        // 添加中间表已存在的数据
        position.getAdPosList().forEach(appPosAdPos -> {
            existsAdPosSet.add(appPosAdPos.getAdPosition().getId());
            resultList.add(appPosAdPosConvert.convert(appPosAdPos));
        });
        // 根据name，appId查询未关联到中间表的数据，并添加到resultList
        Page<AdPosition> page = adPositionService.query(new Query(params));
        page.stream().forEach(adPosition -> {
            if (!existsAdPosSet.contains(adPosition.getId())) {
                resultList.add(new AppPositionAdPositionVO(adPosition.getId(), false, adPositionConvert.convert(adPosition), 1, 1));
            }
        });
        // 将新的resultList按广告平台重新排序
        Collections.sort(resultList);
        // 统计每个平台包含的广告类型个数
        Map<Integer, Long> typeSize = resultList.stream().collect(Collectors.groupingBy(
                (AppPositionAdPositionVO vo) ->
                    vo.getAdPos().getAdvId(), Collectors.counting()
        ));
        // 标记每个平台的第一项为可修改的权重项
        int index = 0;
        for (Map.Entry<Integer, Long> entry: typeSize.entrySet()) {
            resultList.get(index).setRatioFlag(true);
            index += entry.getValue();
        }
        PageInfo<AppPositionAdPositionVO> pageInfo = new PageInfo<>(1, resultList.size(), resultList.size(), resultList);
        return Result.SUCCESS(pageInfo);
    }

    @PutMapping("/ad/{appPosId}")
    @RequiresPermissions("app:position:edit")
    public Result<AppPositionAdPosition> putAdPos(@PathVariable Integer appPosId, @RequestBody @Valid List<AppPositionAdPositionForm> list, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        appPositionService.saveAppPositionAdPositionLink(appPosId, list);
        return Result.SUCCESS();
    }


    private PageInfo<AppPositionVO> convert(Page<AppPosition> result) {
        List<AppPositionVO> resultList = appPositionConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
