package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.vo.AppLinkVO;
import com.yeecloud.adplus.gateway.service.AppLinkService;
import com.yeecloud.adplus.gateway.util.Result;
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
     * @description
     * @param form-id 【body参数】链接id，若不传则随机获取一个
     * @param form-appId 【body参数】appId
     * @return
     */
    @PostMapping("list")
    public Result getLinks(@RequestBody AppLinkForm form,
                           @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
        AppLinkVO appLinkVO = appLinkService.query(form);
        return Result.isEncode(apiVersion, appLinkVO);
    }

    /**
     * 更新链接数据
     * @param form-id 【body参数】链接id
     * @param form-clickNum 【body参数】点击次数 传0或1
     * @param form-showNum 【body参数】展示次数 传0或1
     * @return
     */
    @PostMapping("update")
    public Result<String> updateData(@RequestBody AppLinkForm form) {
        return appLinkService.updateData(form);
    }
}
