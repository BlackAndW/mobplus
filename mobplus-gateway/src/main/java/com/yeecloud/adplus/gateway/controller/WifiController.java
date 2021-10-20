package com.yeecloud.adplus.gateway.controller;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.QWifiInfo;
import com.yeecloud.adplus.dal.entity.WifiInfo;
import com.yeecloud.adplus.dal.repository.WifiInfoRepository;
import com.yeecloud.adplus.gateway.controller.vo.WifiInfoVO;
import com.yeecloud.adplus.gateway.service.WifiService;
import com.yeecloud.adplus.gateway.util.GPSUtil;
import com.yeecloud.meeto.common.result.Result;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

    /**
     * 获取距离原点的距离
     * @param gps 用户gps坐标
     * @return
     */
    @ApiDoc
    @RequestMapping("distance")
    public Result getDistance(@RequestBody WifiInfo wifiInfo,
                              @RequestParam(required = true, defaultValue = "") String gps) {
        QWifiInfo qWifiInfo = QWifiInfo.wifiInfo;
        Predicate predicate = qWifiInfo.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qWifiInfo.id.eq(1));
        WifiInfo entity = wifiInfoRepository.findOne(predicate).orElse(null);

        double result = GPSUtil.GetPointDistance(wifiInfo.getGps(), entity.getGps());
        return Result.SUCCESS(result);
    }

    /**
     * 获取范围内wifi列表
     * @param distance  搜索范围
     * @param gps       用户gps坐标
     * @return
     */
    @ApiDoc
    @RequestMapping("list")
    public Result<List<WifiInfoVO>> getWifiList(@RequestBody WifiInfo wifiInfo,
                              @RequestParam(required = false, value = "distance", defaultValue = "1000.000") double distance,
                              @RequestParam(required = true, defaultValue = "") String gps) {
        List<WifiInfoVO> resultList = wifiService.getWifiList(wifiInfo, distance);
        if (resultList == null || resultList.size() < 1) { return Result.FAILURE("get wifiInfo list failure! No wifi found!"); }
        return Result.SUCCESS(resultList);
    }

    /**
     * 获取指定wifi信息
     * @param gps   用户gps坐标
     * @return
     */
    @ApiDoc
    @RequestMapping("item")
    public Result<WifiInfoVO> getWifi(@RequestBody WifiInfo wifiInfo,
                          @RequestParam(required = true, defaultValue = "") String gps) {
        WifiInfoVO result = wifiService.getWifi(wifiInfo);
        return Result.SUCCESS(result);
    }

    /**
     * 保存wifi信息
     * @param form
     * @return
     */
    @ApiDoc
    @RequestMapping("save")
    public Result save(@RequestBody WifiInfoForm form) {
        WifiInfo wifiInfo = new WifiInfo();
        NewBeanUtils.copyProperties(wifiInfo, form, true);
        wifiService.saveWifi(wifiInfo);
        return Result.SUCCESS("save success!");
    }
}

@Data
class WifiInfoForm{
    private String name;
    private String gps;
    private String mac;
    private String type;
    private String password;
}