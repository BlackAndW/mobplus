package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.vo.AppLinkVO;
import com.yeecloud.adplus.gateway.service.AppLinkService;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/app/link")
public class AppLinkController {

    @Autowired
    AppLinkService appLinkService;

    @PostMapping("list")
    public Result getLinks(@RequestBody AppLinkForm form) {
        List<AppLinkVO> list = appLinkService.query(form);
        return Result.SUCCESS(list);
    }

    @PostMapping("update")
    public Result updateData(@RequestBody AppLinkForm form) {
        return appLinkService.updateData(form);
    }
}
