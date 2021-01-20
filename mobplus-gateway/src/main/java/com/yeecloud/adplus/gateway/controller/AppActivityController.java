package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.gateway.service.AppActivityService;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: Leonard
 * @create: 2021/1/20
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/app/activity")
public class AppActivityController {

    @Autowired
    AppActivityService appActivityService;

    @RequestMapping("/page")
    public Page<AppActivity> getAppActivityList(@RequestParam Map<String, Object> params) {
        return appActivityService.getAppActivityPage(new Query(params));
    }

    @RequestMapping("/status")
    public int getAppActivityStatus(@RequestParam Map<String, Object> params) {
        Page<AppActivity> page = appActivityService.getAppActivityPage(new Query(params));
        log.info("beforeConvert: " + page.getContent().toString());
        return page.getContent().get(0).getStatus();
    }

    @RequestMapping("/list")
    public List<AppActivity> getAppActivityList2(@RequestParam Map<String, Object> params) {
        Page<AppActivity> page = appActivityService.getAppActivityPage(new Query(params));
        log.info("beforeConvert: " + page.getContent().toString());
        return page.getContent();
    }
}
