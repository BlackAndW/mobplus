package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVO;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVOV2;
import com.yeecloud.adplus.gateway.service.AppConfigService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.result.Result;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
     * @param appId 【body参数】appId
     * @param projectCode 【body参数】项目编码
     * @return
     */
    @ApiDoc(stringResult = "{ code:2000, message:'ok', result: {ad: 1, cms: 1, index: 1} }")
    @PostMapping("/api/v1/app/conf")
    public String getAppProjectConfigV1(@RequestBody String body,
                                        @RequestParam(required = true, defaultValue = "") String appId,
                                        @RequestParam(required = true, defaultValue = "") String projectCode,
                                        @RequestParam String m) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            AppConfigVO vo = appConfigService.getAppProjectConfigV1(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.SUCCESS(vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }

    /**
     * 获取app项目配置v2
     * @description 返回result: 1为开启, 2为关闭
     * @param m 加密标识，m=1
     * @param appId 【body参数】appId
     * @param channel 【body参数】渠道
     * @param pkgVersion 【body参数】版本
     */
    @ApiDoc(stringResult = "{ code:2000, message:'ok', result: {ad: 1, content: 1, index: 1} }")
    @PostMapping("/api/v2/app/conf")
    public String getAppProjectConfigV2(@RequestBody String body,
                                        @RequestParam(required = true, defaultValue = "") String appId,
                                        @RequestParam(required = true, defaultValue = "") String channel,
                                        @RequestParam(required = true, defaultValue = "") String pkgVersion,
                                        @RequestParam String m) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            AppConfigVOV2 vo = appConfigService.getAppProjectConfigV2(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.SUCCESS(vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }

    /**
     * 获取应用配置
     * @param m 加密标识，m=1
     * @param appId 【body参数】appId
     * @param channel 【body参数】渠道
     * @param pkgVersion 【body参数】版本
     * @return
     */
    @ApiDoc(stringResult = "{ code:2000, message:'ok', result: { keyName: 'keyValue' } }")
    @PostMapping("/api/v1/app/mobile/conf")
    public String getAppConfig(@RequestBody String body,
                               @RequestParam(required = true, defaultValue = "") String appId,
                               @RequestParam(required = true, defaultValue = "") String channel,
                               @RequestParam(required = true, defaultValue = "") String pkgVersion,
                               @RequestParam String m) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            Map<String,String > result = appConfigService.getAppConfig(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.SUCCESS(result), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }
}
