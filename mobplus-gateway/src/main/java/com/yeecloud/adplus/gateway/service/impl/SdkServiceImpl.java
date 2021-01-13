package com.yeecloud.adplus.gateway.service.impl;

import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppAdvertiser;
import com.yeecloud.adplus.dal.entity.AppPosition;
import com.yeecloud.adplus.dal.entity.AppPositionAdPosition;
import com.yeecloud.adplus.dal.repository.AppAdvertiserRepository;
import com.yeecloud.adplus.dal.repository.AppPositionAdPositionRepository;
import com.yeecloud.adplus.dal.repository.AppPositionRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AdPositionVO;
import com.yeecloud.adplus.gateway.controller.vo.AdvertiserCfgVO;
import com.yeecloud.adplus.gateway.controller.vo.AppPosCfgVO;
import com.yeecloud.adplus.gateway.controller.vo.SdkCfgVO;
import com.yeecloud.adplus.gateway.service.DeviceService;
import com.yeecloud.adplus.gateway.service.SdkService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-09 14:19
 */
@Slf4j
@Service
public class SdkServiceImpl implements SdkService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ConfigureService configureService;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppAdvertiserRepository appAdvertiserRepository;

    @Autowired
    private AppPositionRepository appPositionRepository;

    @Autowired
    private AppPositionAdPositionRepository appPositionAdPositionRepository;


    @Override
    public SdkCfgVO getSdkCfg(DeviceForm form) throws ServiceException {
        deviceService.createOrUpdateOpenDevice(form);
        // 根据安卓端请求的值查找对应的应用
        String appId = form.getAppId();
        App app = appRepository.findByAppId(appId);
        if (app == null || app.isDeleted()) {
            throw new ServiceException("应用不存在，请重新配置应用");
        }
        // 获取服务器sdk配置
        Integer interval = getCacheValue("sdk.cfg.interval", 300 * 1000);
        Integer enabled = getCacheValue("sdk.enabled", 1);
        String notifyUrl = configureService.getValueByKey("sdk.cfg.notify_url", String.class);
        // 获取应用的友盟统计配置
        String umengKey = (String) app.getParameters().get("umengKey");
        // 构造配置参数
        SdkCfgVO vo = new SdkCfgVO();
        vo.setInterval(interval);
        vo.setEnabled(enabled > 0);
        if (StringUtils.isNotBlank(notifyUrl)) {
            vo.setNotifyUrl(notifyUrl);
        }
        if (StringUtils.isNotBlank(umengKey)) {
            vo.setUmengAppKey(umengKey);
        }
        // 获取应用对应广告平台的配置
        List<AppAdvertiser> advertiserList = appAdvertiserRepository.findByApp(app);
        for (AppAdvertiser appAdvertiser : advertiserList) {
            if (appAdvertiser.isDeleted()) {
                continue;
            }
            AdvertiserCfgVO advertiserCfgVO = new AdvertiserCfgVO();
            advertiserCfgVO.setAppId(appAdvertiser.getAppId());
            advertiserCfgVO.setAppKey(appAdvertiser.getAppKey());
            advertiserCfgVO.setAppName(appAdvertiser.getAppName());

            // 添加广告平台列表
            vo.getAdvertiserList().put(appAdvertiser.getAdvertiser().getCode(), advertiserCfgVO);

        }
        // 获取应用的所有展示位
        List<AppPosition> appPositionList = appPositionRepository.findByApp(app);
        if (appPositionList.isEmpty()) {
            throw new ServiceException("应用没有配置展示位，请先进行配置");
        }
        for (AppPosition appPosition : appPositionList) {
            if (appPosition.isDeleted()) {
                continue;
            }
            if (appPosition.getStatus()==2){
                continue;
            }
            AppPosCfgVO appPosCfgVO = new AppPosCfgVO();
            // 获取展示位绑定的广告位
            appPosCfgVO.setParams(appPosition.getParameters());
            List<AppPositionAdPosition> adPositionList = appPosition.getAdPosList();
            for (AppPositionAdPosition adPosition : adPositionList) {
                if (adPosition.isDeleted()){
                    continue;
                }
                AdPositionVO adPositionVO = new AdPositionVO();
                adPositionVO.setAdvertiser(adPosition.getAdPosition().getAdvertiser().getCode());
                adPositionVO.setPosId(adPosition.getAdPosition().getCode());
                adPositionVO.setRatio(adPosition.getRatio());

                appPosCfgVO.getPositionList().add(adPositionVO);
            }
            vo.getAdPosList().put(appPosition.getCode(), appPosCfgVO);
        }

        return vo;
    }

    @Override
    public SdkCfgVO getSdkCfgEn(DeviceForm form) throws ServiceException {
        deviceService.createOrUpdateOpenDevice(form);
        // 根据安卓端请求的值查找对应的应用
        String appId = form.getAppId();
        App app = appRepository.findByAppId(appId);
        if (app == null || app.isDeleted()) {
            throw new ServiceException("The application does not exist, please reconfigure the application!");
        }
        // 构造配置参数
        SdkCfgVO vo = new SdkCfgVO();
        // 获取应用对应广告平台的配置
        List<AppAdvertiser> advertiserList = appAdvertiserRepository.findByApp(app);
        for (AppAdvertiser appAdvertiser : advertiserList) {
            if (appAdvertiser.isDeleted()) {
                continue;
            }
            AdvertiserCfgVO advertiserCfgVO = new AdvertiserCfgVO();
            advertiserCfgVO.setAppId(appAdvertiser.getAppId());
            advertiserCfgVO.setAppKey(appAdvertiser.getAppKey());
            advertiserCfgVO.setAppName(appAdvertiser.getAppName());

            // 添加广告平台列表
            vo.getAdvertiserList().put(appAdvertiser.getAdvertiser().getCode(), advertiserCfgVO);

        }
        // 获取应用的所有展示位
        List<AppPosition> appPositionList = appPositionRepository.findByApp(app);
        if (appPositionList.isEmpty()) {
            throw new ServiceException("The application has not configured the display position, please configure it first!");
        }
        for (AppPosition appPosition : appPositionList) {
            if (appPosition.isDeleted()) {
                continue;
            }
            if (appPosition.getStatus()==2){
                continue;
            }
            AppPosCfgVO appPosCfgVO = new AppPosCfgVO();
            // 获取展示位绑定的广告位
            // appPosCfgVO.setParams(appPosition.getParameters());
            List<AppPositionAdPosition> adPositionList = appPosition.getAdPosList();
            for (AppPositionAdPosition adPosition : adPositionList) {
                if (adPosition.isDeleted()){
                    continue;
                }
                AdPositionVO adPositionVO = new AdPositionVO();
                adPositionVO.setAdvertiser(adPosition.getAdPosition().getAdvertiser().getCode());
                adPositionVO.setPosId(adPosition.getAdPosition().getCode());
                adPositionVO.setRatio(adPosition.getRatio());

                appPosCfgVO.getPositionList().add(adPositionVO);
            }
            vo.getAdPosList().put(appPosition.getCode(), appPosCfgVO);
        }

        return vo;
    }

    private Integer getCacheValue(String key, Integer value) {
        Integer v = configureService.getValueByKey(key, Integer.class);
        if (v == null) {
            v = value;
        }
        return v;
    }
}
