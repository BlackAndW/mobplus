package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.WifiInfo;
import com.yeecloud.adplus.gateway.controller.vo.WifiInfoVO;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/24
 */
public interface WifiService {

    List<WifiInfoVO> getWifiList(WifiInfo wifiInfo, double distance);

    WifiInfoVO getWifi(WifiInfo wifiInfo);

    void saveWifi(WifiInfo wifiInfo);
}
