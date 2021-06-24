package com.yeecloud.adplus.gateway.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.QWifiInfo;
import com.yeecloud.adplus.dal.entity.WifiInfo;
import com.yeecloud.adplus.dal.repository.WifiInfoRepository;
import com.yeecloud.adplus.gateway.controller.vo.WifiInfoVO;
import com.yeecloud.adplus.gateway.service.WifiService;
import com.yeecloud.adplus.gateway.util.GPSUtil;
import com.yeecloud.meeto.common.util.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/24
 */
@Service
public class WifiServiceImpl implements WifiService {

    @Autowired
    WifiInfoRepository wifiInfoRepository;

    @Override
    public List<WifiInfoVO> getWifiList(WifiInfo wifiInfo, double distanceRange) {
        QWifiInfo qWifiInfo = QWifiInfo.wifiInfo;
        Predicate predicate = qWifiInfo.deleted.eq(false);
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pageRequest = PageRequest.of(0, 999, sort);
        List<WifiInfo> wifiInfoList = wifiInfoRepository.findAll(predicate, pageRequest).getContent();
        List<WifiInfoVO> resultList = new ArrayList<>();
        wifiInfoList.forEach( wifiInfoItem -> {
            double distance = GPSUtil.GetPointDistance(wifiInfo.getGps(), wifiInfoItem.getGps());
            if (distance <= distanceRange) {
                WifiInfoVO vo = new WifiInfoVO();
                BeanUtils.copyProperties(wifiInfoItem, vo);
                vo.setDistance(distance);
                resultList.add(vo);
            }
        });
        return resultList;
    }

    @Override
    public WifiInfoVO getWifi(WifiInfo wifiInfo) {
        QWifiInfo qWifiInfo = QWifiInfo.wifiInfo;
        Predicate predicate = qWifiInfo.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qWifiInfo.gps.eq(wifiInfo.getGps()));
        WifiInfo result = wifiInfoRepository.findOne(predicate).orElse(null);
        WifiInfoVO vo = new WifiInfoVO();
        BeanUtils.copyProperties(result, vo);
        return vo;
    }

    @Override
    public void saveWifi(WifiInfo wifiInfo) {
        wifiInfoRepository.save(wifiInfo);
    }
}
