package com.yeecloud.adplus.admin.controller.release;

import com.yeecloud.adplus.admin.controller.release.form.ChannelForm;
import com.yeecloud.adplus.admin.controller.release.vo.ChannelVO;
import com.yeecloud.adplus.admin.controller.release.convert.ChannelConvert;
import com.yeecloud.adplus.admin.service.ChannelService;
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
import java.util.List;
import java.util.Map;

/**
 * 渠道
 *
 * @author: Huang
 * @create: 2020-12-01 14:23
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/release/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelConvert channelConvert;

    @GetMapping
    @RequiresPermissions("release:channel:query")
    public Result<ChannelVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<Channel> page = channelService.query(new Query(params));
        PageInfo<ChannelVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("release:channel:info")
    public Result<ChannelVO> info(@PathVariable Integer id) throws ServiceException {
        Channel result = channelService.findById(id);
        return Result.SUCCESS(channelConvert.convert(result));
    }

    @PostMapping
    @RequiresPermissions("release:channel:create")
    public Result create(@RequestBody @Valid ChannelForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        channelService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("release:channel:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid ChannelForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        channelService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("release:channel:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        channelService.delete(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/sct")
    @RequiresPermissions("release:channel:query")
    public Result<ChannelVO> select(@RequestParam Map<String, Object> params) throws ServiceException {
        params.put("pageSize", Integer.MAX_VALUE);
        Query query = new Query(params);
        query.put("status", 2);
        Page<Channel> page = channelService.query(query);
        PageInfo<ChannelVO> result = convert(page);
        return Result.SUCCESS(result.getData());
    }

    private PageInfo<ChannelVO> convert(Page<Channel> result) {
        List<ChannelVO> resultList = channelConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
