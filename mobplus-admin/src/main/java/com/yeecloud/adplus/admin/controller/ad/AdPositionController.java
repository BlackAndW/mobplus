package com.yeecloud.adplus.admin.controller.ad;

import com.yeecloud.adplus.admin.controller.ad.convert.AdPositionConvert;
import com.yeecloud.adplus.admin.controller.ad.vo.AdPositionVO;
import com.yeecloud.adplus.admin.service.AdPositionService;
import com.yeecloud.adplus.dal.entity.AdPosition;
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
 * @create: 2020-12-08 16:26
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/ad/position")
public class AdPositionController {

    @Autowired
    private AdPositionService adPositionService;

    @Autowired
    private AdPositionConvert adPositionConvert;

    @GetMapping
    @RequiresPermissions("ad:position:query")
    public Result<AdPositionVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<AdPosition> page = adPositionService.query(new Query(params));
        PageInfo<AdPositionVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("ad:position:create")
    public Result create(@RequestBody @Valid AdPosition form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        adPositionService.create(form);
        return Result.SUCCESS();
    }


    @PutMapping("/{id}")
    @RequiresPermissions("ad:position:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid AdPosition form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        adPositionService.update(id, form);
        return Result.SUCCESS();
    }


    @DeleteMapping
    @RequiresPermissions("ad:position:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        adPositionService.delete(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/sct")
    @RequiresPermissions("ad:position:query")
    public Result<AdPositionVO> select(@RequestParam Map<String, Object> params) throws ServiceException {
        params.put("pageSize", Integer.MAX_VALUE);
        Page<AdPosition> page = adPositionService.query(new Query(params));
        PageInfo<AdPositionVO> result = convert(page);
        return Result.SUCCESS(result.getData());
    }

    private PageInfo<AdPositionVO> convert(Page<AdPosition> result) {
        List<AdPositionVO> resultList = adPositionConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
