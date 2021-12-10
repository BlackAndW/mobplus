package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVO;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVOV2;
import com.yeecloud.adplus.gateway.service.AppConfigService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.codec.Codec;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 应用配置
 *
 * @author: Huang
 * @create: 2020-11-30 14:48
 */
@Slf4j
@RestController
public class AppConfigController {

    @Autowired
    private AppConfigService appConfigService;

    /**
     * 获取app项目配置v1
     * @description 返回result: 1为开启, 2为关闭
     * @param m 加密标识，m=1
     * @param body-appId 【body参数】appId
     * @param body-projectCode 【body参数】项目编码
     * @param apiVersion 【header参数】Api-Version:版本号
     * @return
     */
    @PostMapping("/api/v1/app/conf")
    public String getAppProjectConfigV1(@RequestBody String body,
                                        @RequestParam(required = false) String m,
                                        @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            AppConfigVO vo = appConfigService.getAppProjectConfigV1(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = Result.isEncode(apiVersion, JSON.toJSONString(vo, config)).toString();
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        log.debug("RespToApp:{}", response);
        return response;
    }

    /**
     * 获取app项目配置v2
     * @description 返回result: 1为开启, 2为关闭
     * @param m 加密标识，m=1
     * @param body-appId 【body参数】appId
     * @param body-channel 【body参数】渠道
     * @param body-pkgVersion 【body参数】版本
     * @param apiVersion 【header参数】Api-Version:版本号
     */
    @PostMapping("/api/v2/app/conf")
    public String getAppProjectConfigV2(@RequestBody String body,
                                        @RequestParam(required = false) String m,
                                        @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            AppConfigVOV2 vo = appConfigService.getAppProjectConfigV2(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = Result.isEncode(apiVersion, JSON.toJSONString(vo, config)).toString();
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        log.debug("RespToApp:{}", response);
        return response;
    }

    /**
     * 获取应用配置
     * @param m 加密标识，m=1【1.1版本后弃用】
     * @param body-appId 【body参数】appId
     * @param body-channel 【body参数】渠道
     * @param body-pkgVersion 【body参数】版本
     * @param apiVersion 【header参数】Api-Version:版本号
     * @return
     */
    @PostMapping("/api/v1/app/mobile/conf")
    public Result getAppConfig(@RequestBody String body,
                               @RequestParam(required = false) String m,
                               @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
        log.debug("ReqFromApp:{}", body);
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            Map<String,String > result = appConfigService.getAppConfig(form);
            log.debug("RespToApp:{}", result);
            return Result.isEncode(apiVersion, result);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
