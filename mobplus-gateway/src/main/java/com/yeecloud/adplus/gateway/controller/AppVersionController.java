package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.gateway.controller.form.AppVersionForm;
import com.yeecloud.adplus.gateway.controller.vo.AppVersionVO;
import com.yeecloud.adplus.gateway.service.AppVersionService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 版本管理
 * @author: Leonard
 * @create: 2021/11/29
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/version")
public class AppVersionController {

    @Autowired
    AppVersionService appVersionService;

    @PostMapping("update")
    public Result detectVersion(@RequestBody AppVersionForm form,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
        AppVersionVO result = appVersionService.detectVersion(form);
        if (result != null) {
            return Result.isEncode(apiVersion, result);
        }
        return Result.SUCCESS(new JSONObject());
    }
}
