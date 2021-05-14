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
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
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
     * 国内
     */
    @PostMapping("/cfg")
    public String getSdkCfg(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m, HttpServletRequest request) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
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
        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }

    /**
     * 海外
     */
    @PostMapping("/en/cfg")
    public String getSdkCfgEn(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m, HttpServletRequest request) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
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
        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }

    /**
     * 合并后的新接口
     */
    @PostMapping("/appPosition")
    public String getAppPosition(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m, HttpServletRequest request) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
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
        log.debug("RespToApp:{}", response);
        return needCodec ? Codec.encode(response) : response;
    }
}
