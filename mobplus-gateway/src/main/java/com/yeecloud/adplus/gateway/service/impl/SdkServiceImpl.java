package com.yeecloud.adplus.gateway.service.impl;

import com.google.common.collect.Maps;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.*;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.*;
import com.yeecloud.adplus.gateway.service.AppConfigService;
import com.yeecloud.adplus.gateway.service.DeviceService;
import com.yeecloud.adplus.gateway.service.SdkService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    private AppConfigService appConfigService;

    @Autowired
    private ConfigureService configureService;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppAdvertiserRepository appAdvertiserRepository;

    @Autowired
    private AppPositionRepository appPositionRepository;

    @Autowired
    private AppMobileConfRepository appMobileConfRepository;

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
        addAdvertiserList(vo, app);
        // 获取应用的所有展示位
        addAppPositionList(vo, app, false);
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
        addAdvertiserList(vo, app);
        // 获取应用的所有展示位
        addAppPositionList(vo, app, true);
        return vo;
    }

    @Override
    public SdkCfgVO getPosList(DeviceForm form) throws ServiceException {
        deviceService.createOrUpdateOpenDevice(form);
        AppConfigVOV2 appConfigVOV2 = appConfigService.getAppProjectConfigV2(form);
        // 根据安卓端请求的值查找对应的应用
        String appId = form.getAppId();
        App app = appRepository.findByAppId(appId);
        if (app == null || app.isDeleted()) {
            throw new ServiceException("The application does not exist, please reconfigure the application!");
        }
        // 构造配置参数
        SdkCfgVO vo = new SdkCfgVO();
        // 获取应用的所有展示位
        if (appConfigVOV2.getAdSwitch() == 1) {
            addAppPositionList(vo, app, true);
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

    private void addAppPositionList(SdkCfgVO vo, App app, Boolean isEn) throws ServiceException {
        List<AppPosition> appPositionList = appPositionRepository.findByApp(app);
        if (appPositionList.isEmpty()) {
            if (isEn) {
                throw new ServiceException("The application has not configured the display position, please configure it first!");
            }
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

            // 查看全局配置
            Integer showConfig = appPosition.getLimitShowConfig();
            Integer clickConfig = appPosition.getLimitClickConfig();

            for (AppPositionAdPosition appPosAdPosition : adPositionList) {
                if (appPosAdPosition.isDeleted()){
                    continue;
                }
                AdPositionVO adPositionVO = new AdPositionVO();
                adPositionVO.setAdvertiser(appPosAdPosition.getAdPosition().getAdvertiser().getCode());
                adPositionVO.setPosId(appPosAdPosition.getAdPosition().getCode());
                adPositionVO.setUnitId(appPosAdPosition.getAdPosition().getMintegralUnitId());
                adPositionVO.setAdType(appPosAdPosition.getAdPosition().getType().getId());
                adPositionVO.setRatio(appPosAdPosition.getRatio());
                adPositionVO.setTypeRatio(appPosAdPosition.getTypeRatio());
                adPositionVO.setLimitShowCount(appPosAdPosition.getLimitShowCount());
                adPositionVO.setLimitClickCount(appPosAdPosition.getLimitClickCount());
                // 根据配置，设置限制次数
                Map<String, Integer> limitCountMap = getLimitCountMap(appPosAdPosition.getAdPosition());
                adPositionVO = setAdPositionVO(adPositionVO, appPosAdPosition.getAdPosition(), limitCountMap, showConfig, clickConfig);
                appPosCfgVO.getPositionList().add(adPositionVO);
            }
            vo.getAdPosList().put(appPosition.getCode(), appPosCfgVO);
        }
    }

    private void addAdvertiserList(SdkCfgVO vo, App app) {
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
    }

    /***
     * 获取全局配置的限制次数
     * @param adPosition
     * @return  limitCountMap
     */
    private Map<String, Integer> getLimitCountMap(AdPosition adPosition) {
        List<AppMobileConf> appMobileConfList;
        appMobileConfList = appMobileConfRepository.findByApp(adPosition.getApp());
        Map<String, Integer> limitCountMap = Maps.newHashMap();
        for (AppMobileConf appMobileConf : appMobileConfList) {
            if (appMobileConf.getKey().equals("google_show_count")) {
                limitCountMap.put("GGshowCount", Integer.parseInt(appMobileConf.getValue()));
                continue;
            }
            if (appMobileConf.getKey().equals("google_click_count")) {
                limitCountMap.put("GGclickCount", Integer.parseInt(appMobileConf.getValue()));
                continue;
            }
            if (appMobileConf.getKey().equals("fb_show_count")) {
                limitCountMap.put("FBshowCount", Integer.parseInt(appMobileConf.getValue()));
                continue;
            }
            if (appMobileConf.getKey().equals("fb_click_count")) {
                limitCountMap.put("FBclickCount", Integer.parseInt(appMobileConf.getValue()));
            }
        }
        return limitCountMap;
    }

    /***
     * 根据配置，设置限制次数
     * @param vo
     * @param adPosition
     * @param limitCountMap 配置值
     * @param showConfig    展示次数配置开关
     * @param clickConfig   点击次数配置开关
     * @return
     */
    private AdPositionVO setAdPositionVO(AdPositionVO vo, AdPosition adPosition,
                                                     Map<String, Integer> limitCountMap, Integer showConfig, Integer clickConfig){
        if (clickConfig == 1) {
            if (adPosition.getAdvertiser().getCode().equals("google")) {
                vo.setLimitShowCount(limitCountMap.get("GGshowCount"));
            }
            if (adPosition.getAdvertiser().getCode().equals("FB")) {
                vo.setLimitShowCount(limitCountMap.get("FBshowCount"));
            }
        }
        if (showConfig == 1) {
            if (adPosition.getAdvertiser().getCode().equals("google")) {
                vo.setLimitClickCount(limitCountMap.get("GGclickCount"));
            }
            if (adPosition.getAdvertiser().getCode().equals("FB")) {
                vo.setLimitClickCount(limitCountMap.get("FBclickCount"));
            }
        }
        return vo;
    }
}
