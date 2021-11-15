package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppActivityVO;
import com.yeecloud.adplus.gateway.service.AppActivityService;
import com.yeecloud.adplus.gateway.util.Result;
import org.hibernate.service.spi.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 活动管理
 * @author: Leonard
 * @create: 2021/1/20
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/app")
public class AppActivityController {

    @Autowired
    AppActivityService appActivityService;

    /**
     * 获取活动信息列表
     * @param body 设备信息JSON字符串
     * @description
     * @param m 解码标识
     * @return
     */
    @RequestMapping("/activityList")
    public Result getAppActivityList(@RequestBody(required = false) String body,
                                     @RequestParam(value = "m", required = false) String m,
                                     @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
        Result response;
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            List<AppActivityVO> appActivityVOList = appActivityService.getAppActivityList(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = Result.isEncode(apiVersion, appActivityVOList);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return  response;
    }

    /**
     * 获取系统时间
     * @return
     */
    @RequestMapping("/today")
    public long getToday() {
        return new Date().getTime();
    }
}
