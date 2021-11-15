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
     * @description 随机获取一个结果
     * @param id 【body参数】链接id
     * @param appId 【body参数】appId
     * @return
     */
    @ApiDoc
    @PostMapping("list")
    public Result<AppLinkVO> getLinks(@RequestBody AppLinkForm form,
                                      @RequestParam(required = false, defaultValue = "0") Integer id,
                                      @RequestParam(required = true, defaultValue = "") String appId,
                                      @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
        AppLinkVO appLinkVO = appLinkService.query(form);
        return Result.isEncode(apiVersion, appLinkVO);
    }

    /**
     * 更新链接数据
     * @param id 【body参数】链接id
     * @param clickNum 【body参数】点击次数 传0或1
     * @param showNum 【body参数】展示次数 传0或1
     * @return
     */
    @ApiDoc
    @PostMapping("update")
    public Result<String> updateData(@RequestBody AppLinkForm form,
                                     @RequestParam(required = true, defaultValue = "0") Integer id,
                                     @RequestParam(required = true, defaultValue = "0") Integer clickNum,
                                     @RequestParam(required = true, defaultValue = "0") Integer showNum) {
        return appLinkService.updateData(form);
    }
}
