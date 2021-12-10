package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.SdkCfgVO;
import com.yeecloud.adplus.gateway.service.SdkService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.util.ParamUtils;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 展示位管理
 * @author: Huang
 * @create: 2020-12-09 14:18
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/sdk")
public class SdkController {

    @Autowired
    private SdkService sdkService;

    /**
     * 获取展示位列表(旧)
     * @param m 加密标识，m=1
     * @param body-appId 【body参数】appId
     * @param body-channel 【body参数】渠道
     * @param body-pkgVersion 【body参数】版本
     * @return
     */
    @PostMapping("/cfg")
    public String getSdkCfg(@RequestBody String body,
                            @RequestParam(required = false) String m,
                            @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion,
                            HttpServletRequest request) {
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            form.setRemoteIp(ParamUtils.getIpAddr(request));
            SdkCfgVO vo = sdkService.getSdkCfg(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.isEncode(apiVersion, vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return response;
    }

    /**
     * 获取展示位列表(旧|海外版)
     * @param m 加密标识，m=1
     * @param body-appId 【body参数】appId
     * @param body-channel 【body参数】渠道
     * @param body-pkgVersion 【body参数】版本
     * @return
     */
    @PostMapping("/en/cfg")
    public String getSdkCfgEn(@RequestBody String body,
                              @RequestParam(required = false) String m,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion,
                              HttpServletRequest request) {
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            form.setRemoteIp(ParamUtils.getIpAddr(request));
            SdkCfgVO vo = sdkService.getSdkCfgEn(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.isEncode(apiVersion, vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return response;
    }

    /**
     * 获取展示位列表(新)
     * @param m 加密标识，m=1
     * @param body-appId 【body参数】appId
     * @param body-channel 【body参数】渠道
     * @param body-pkgVersion 【body参数】版本
     * @return
     */
    @PostMapping("/appPosition")
    public String getAppPosition(@RequestBody String body,
                                 @RequestParam(required = false) String m,
                                 @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion,
                                 HttpServletRequest request) {
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            form.setRemoteIp(ParamUtils.getIpAddr(request));
            SdkCfgVO vo = sdkService.getPosList(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            if (Double.valueOf(apiVersion) > 1.0) {
                return Result.ENCODE(JSON.toJSONString(vo, config)).toString();
            }
            response = JSON.toJSONString(Result.isEncode(apiVersion, vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return response;
    }
}
