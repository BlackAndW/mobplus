package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppActivityVO;
import com.yeecloud.adplus.gateway.service.AppActivityService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.result.Result;
import io.github.yedaxia.apidocs.ApiDoc;
import org.hibernate.service.spi.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Result getAppActivityList(@RequestBody(required = false) String body, @RequestParam(value = "m", required = false) String m) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        Result response;
        try {
            DeviceForm form = JSON.parseObject(body, DeviceForm.class);
            List<AppActivityVO> appActivityVOList = appActivityService.getAppActivityList(form);
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            response = Result.SUCCESS(appActivityVOList);
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return needCodec ? Result.SUCCESS(Codec.encode(JSON.toJSONString(response))) : response;
    }

    @RequestMapping("/today")
    public long getToday() {
        return new Date().getTime();
    }
}
