package com.yeecloud.adplus.gateway.controller;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.QWifiInfo;
import com.yeecloud.adplus.dal.entity.WifiInfo;
import com.yeecloud.adplus.dal.repository.WifiInfoRepository;
import com.yeecloud.adplus.gateway.controller.vo.WifiInfoVO;
import com.yeecloud.adplus.gateway.service.WifiService;
import com.yeecloud.adplus.gateway.util.GPSUtil;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/wifi")
public class WifiController {

    @Autowired
    WifiInfoRepository wifiInfoRepository;

    @Autowired
    WifiService wifiService;

    @RequestMapping("distance")
    public Result getDistance(@RequestBody WifiInfo wifiInfo) {
        QWifiInfo qWifiInfo = QWifiInfo.wifiInfo;
        Predicate predicate = qWifiInfo.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qWifiInfo.id.eq(1));
        WifiInfo entity = wifiInfoRepository.findOne(predicate).orElse(null);

        double result = GPSUtil.GetPointDistance(wifiInfo.getGps(), entity.getGps());
        return Result.SUCCESS(result);
    }

    @RequestMapping("list")
    public Result getWifiList(@RequestBody WifiInfo wifiInfo, @RequestParam(value = "distance", defaultValue = "1000.000") double distance) {
        List<WifiInfoVO> resultList = wifiService.getWifiList(wifiInfo, distance);
        if (resultList == null || resultList.size() < 1) { return Result.FAILURE("get wifiInfo list failure! No wifi found!"); }
        return Result.SUCCESS(resultList);
    }

    @RequestMapping("item")
    public Result getWifi(@RequestBody WifiInfo wifiInfo) {
        WifiInfoVO result = wifiService.getWifi(wifiInfo);
        return Result.SUCCESS(result);
    }

    @RequestMapping("save")
    public Result save(@RequestBody WifiInfo wifiInfo) {
        wifiService.saveWifi(wifiInfo);
        return Result.SUCCESS("save success!");
    }
}
