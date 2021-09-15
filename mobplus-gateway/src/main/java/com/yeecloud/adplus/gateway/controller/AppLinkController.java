package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.vo.AppLinkVO;
import com.yeecloud.adplus.gateway.service.AppLinkService;
import com.yeecloud.meeto.common.result.Result;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 链接管理
 * @author: Leonard
 * @create: 2021/7/16
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/app/link")
public class AppLinkController {

    @Autowired
    AppLinkService appLinkService;

    /**
     * 获取链接
     * @description 随机获取一个结果
     * 入参：[id, appId]
     * @param form
     * @return
     */
    @ApiDoc
    @PostMapping("list")
    public Result<AppLinkVO> getLinks(@RequestBody AppLinkForm form) {
        AppLinkVO appLinkVO = appLinkService.query(form);
        return Result.SUCCESS(appLinkVO);
    }

    /**
     * 更新链接数据
     * @description 入参: [id, clickNum, showNum]
     * @param form
     * @return
     */
    @ApiDoc
    @PostMapping("update")
    public Result<String> updateData(@RequestBody AppLinkForm form) {
        return appLinkService.updateData(form);
    }
}
