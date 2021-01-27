package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppActivityVO;
import com.yeecloud.adplus.gateway.service.AppPositionService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/1/27
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/app")
public class AppPositionController {

    @Autowired
    AppPositionService appPositionService;

    @RequestMapping("/position")
    public Result getAppPositionList (@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m) throws ServiceException {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        log.debug("ReqFromApp:{}", body);
        Result response;
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            List resultMap = appPositionService.getAppPositionConfList(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = Result.SUCCESS(resultMap);
        } catch (Throwable e) {
            throw new org.hibernate.service.spi.ServiceException(e.getMessage());
        }
        return needCodec ? Result.SUCCESS(Codec.encode(JSON.toJSONString(response))) : response;

    }
}
