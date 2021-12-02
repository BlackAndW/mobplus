package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppFunctionVO;
import com.yeecloud.adplus.gateway.service.AppPositionService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.exception.ServiceException;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置管理（项目）和功能管理
 * @author: Leonard
 * @create: 2021/1/27
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/app")
public class AppPositionController {

    @Autowired
    AppPositionService appPositionService;

    /**
     * 获取配置管理（项目）中的JSON格式的配置
     * @param m 加密标识，m=1
     * @param appId 【body参数】appId
     * @param channel 【body参数】渠道
     * @param pkgVersion 【body参数】版本
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/position")
    public Result getAppPositionList (@RequestBody String body,
                                            @RequestParam(required = true, defaultValue = "") String appId,
                                            @RequestParam(required = true, defaultValue = "") String channel,
                                            @RequestParam(required = true, defaultValue = "") String pkgVersion,
                                            @RequestParam(required = false) String m,
                                            @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        Result response;
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            List resultMap = appPositionService.getAdTypeConfList(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = Result.isEncode(apiVersion, resultMap);
        } catch (Throwable e) {
            throw new org.hibernate.service.spi.ServiceException(e.getMessage());
        }
        return response;
    }

    /**
     * 功能管理-获取列表
     * @param m 加密标识，m=1
     * @param appId 【body参数】appId
     * @param channel 【body参数】渠道
     * @param pkgVersion 【body参数】版本
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/positionNew")
    public Result getAppPositionListNew (@RequestBody String body,
                                                              @RequestParam(required = true, defaultValue = "") String appId,
                                                              @RequestParam(required = true, defaultValue = "") String channel,
                                                              @RequestParam(required = true, defaultValue = "") String pkgVersion,
                                                              @RequestParam(required = false) String m,
                                                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        Result response;
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            List resultMap = appPositionService.getAdTypeConfListNew(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = Result.isEncode(apiVersion, resultMap);
        } catch (Throwable e) {
            throw new org.hibernate.service.spi.ServiceException(e.getMessage());
        }
        return response;
    }
}
