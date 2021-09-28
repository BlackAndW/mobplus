package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVO;
import com.yeecloud.adplus.gateway.controller.vo.SdkCfgVO;
import com.yeecloud.adplus.gateway.service.SdkService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.result.Result;
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
     * @description 没有做版本渠道控制
     * @param body body json字符串【appId: string】
     * @param m 加密标识，m=1
     * @param request
     * @return
     */
    @ApiDoc(stringResult = "{code:2000, message:'ok', result: {adv:{}, enabled: 'boolean', interval: 300000, pos:{appPositionCode:{cfg:{}, position: [{ad_type: 'int', adv: 'string', limit_click_count:'int', limit_show_count:'int', pos_id:'string', ratio:'int', type_ratio: 'int', unit_id: 'int'}]}}}}")
    @PostMapping("/cfg")
    public String getSdkCfg(@RequestBody String body, @RequestParam String m, HttpServletRequest request) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
//        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            form.setRemoteIp(ParamUtils.getIpAddr(request));
            SdkCfgVO vo = sdkService.getSdkCfg(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.SUCCESS(vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
//        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }

    /**
     * 获取展示位列表(旧|海外版)
     * @description 没有做版本渠道控制
     * @param body body json字符串【appId: string】
     * @param m 加密标识，m=1
     * @param request
     * @return
     */
    @ApiDoc(stringResult = "{code:2000, message:'ok', result: {adv:{}, enabled: 'boolean', interval: 300000, pos:{appPositionCode:{cfg:{}, position: [{ad_type: 'int', adv: 'string', limit_click_count:'int', limit_show_count:'int', pos_id:'string', ratio:'int', type_ratio: 'int', unit_id: 'int'}]}}}}")
    @PostMapping("/en/cfg")
    public String getSdkCfgEn(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m, HttpServletRequest request) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
//        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            form.setRemoteIp(ParamUtils.getIpAddr(request));
            SdkCfgVO vo = sdkService.getSdkCfgEn(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.SUCCESS(vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
//        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }

    /**
     * 获取展示位列表(新)
     * @description 有做版本渠道控制
     * @param body json字符串【appId: string; channel: string; pkgVersion: string】
     * @param m 加密标识，m=1
     * @param request
     * @return
     */
    @ApiDoc(stringResult = "{code:2000, message:'ok', result: {adv:{}, enabled: 'boolean', interval: 300000, pos:{appPositionCode:{cfg:{}, position: [{ad_type: 'int', adv: 'string', limit_click_count:'int', limit_show_count:'int', pos_id:'string', ratio:'int', type_ratio: 'int', unit_id: 'int'}]}}}}")
    @PostMapping("/appPosition")
    public String getAppPosition(@RequestBody String body, @RequestParam String m, HttpServletRequest request) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
//        log.debug("ReqFromApp:{}", body);
        String response = "";
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            form.setRemoteIp(ParamUtils.getIpAddr(request));
            SdkCfgVO vo = sdkService.getPosList(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = JSON.toJSONString(Result.SUCCESS(vo), config);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
//        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }
}
