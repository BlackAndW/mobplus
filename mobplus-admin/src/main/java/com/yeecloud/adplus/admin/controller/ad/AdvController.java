package com.yeecloud.adplus.admin.controller.ad;

import com.yeecloud.adplus.admin.controller.ad.convert.AdvertiserConvert;
import com.yeecloud.adplus.admin.controller.ad.vo.AdvertiserVO;
import com.yeecloud.adplus.admin.service.AdvertiserService;
import com.yeecloud.adplus.dal.entity.Advertiser;
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
 * @create: 2020-12-08 15:37
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/ad/adv")
public class AdvController {

    @Autowired
    private AdvertiserService advertiserService;
    @Autowired
    private AdvertiserConvert advertiserConvert;

    @GetMapping
    @RequiresPermissions("ad:adv:query")
    public Result<AdvertiserVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<Advertiser> page = advertiserService.query(new Query(params));
        PageInfo<AdvertiserVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("ad:adv:info")
    public Result<AdvertiserVO> info(@PathVariable Integer id) throws ServiceException {
        Advertiser result = advertiserService.findById(id);
        return Result.SUCCESS(advertiserConvert.convert(result));
    }

    @PostMapping
    @RequiresPermissions("ad:adv:create")
    public Result create(@RequestBody @Valid Advertiser form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        advertiserService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("ad:adv:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid Advertiser form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        advertiserService.update(id, form);
        return Result.SUCCESS();
    }
    @DeleteMapping
    @RequiresPermissions("ad:adv:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        advertiserService.delete(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/sct")
    @RequiresPermissions("ad:adv:query")
    public Result<AdvertiserVO> select(@RequestParam Map<String, Object> params) throws ServiceException {
        params.put("pageSize", Integer.MAX_VALUE);
        Page<Advertiser> page = advertiserService.query(new Query(params));
        PageInfo<AdvertiserVO> result = convert(page);
        return Result.SUCCESS(result.getData());
    }

    private PageInfo<AdvertiserVO> convert(Page<Advertiser> result) {
        List<AdvertiserVO> resultList = advertiserConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}