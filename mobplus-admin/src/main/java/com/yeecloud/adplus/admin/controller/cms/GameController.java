package com.yeecloud.adplus.admin.controller.cms;

import com.yeecloud.adplus.admin.controller.cms.convert.GameConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.GameVO;
import com.yeecloud.adplus.admin.service.GameService;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.dal.entity.GameLog;
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
 * Title
 *
 * Date: 2020-06-19 15:31:35
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cms/game")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private GameConvert gameConvert;

    @GetMapping
    @RequiresPermissions("cms:game:query")
    public Result<GameVO> list(@RequestParam Map<String, Object> params) throws ServiceException {
        Page<GameLog> page = gameService.query(new Query(params));
        PageInfo<GameVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("cms:game:info")
    public Result<GameVO> info(@PathVariable Integer id) throws ServiceException {
        Game result = gameService.findById(id);
        return Result.SUCCESS(gameConvert.convert(result));
    }

    @PostMapping
    @RequiresPermissions("cms:game:create")
    public Result create(@RequestBody @Valid Game form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        gameService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("cms:game:edit")
    public Result update(@PathVariable Integer id, @RequestBody @Valid Game form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        gameService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("cms:game:delete")
    public Result delete(@RequestBody Integer[] ids) throws ServiceException {
        gameService.delete(ids);
        return Result.SUCCESS();
    }

    private PageInfo<GameVO> convert(Page<GameLog> result) {
        List<GameVO> resultList = gameConvert.convertLog(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }


}
