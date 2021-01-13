package com.yeecloud.adplus.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.*;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVO;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVOV2;
import com.yeecloud.adplus.gateway.controller.vo.AppPosCfgVO;
import com.yeecloud.adplus.gateway.service.AppConfigService;
import com.yeecloud.adplus.gateway.service.DeviceService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-11-30 14:50
 */
@Slf4j
@Service
public class AppConfigServiceImpl implements AppConfigService {


    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Autowired
    private AppProjectRepository appProjectRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private AppMobileConfRepository appMobileConfRepository;

    @Autowired
    private DeviceService deviceService;

    @Override
    public AppConfigVO getAppProjectConfigV1(DeviceForm form) throws ServiceException {
        // deviceService.createOrUpdateOpenDevice(form);
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null || app.isDeleted()) {
            throw new ServiceException("该应用不存在！");
        }
        AppProject project = appProjectRepository.findByCode(form.getProjectCode());
        if (project == null || project.isDeleted()) {
            throw new ServiceException("该项目不存在！");
        }
        AppConfig appConfig = appConfigRepository.findByAppProject(project);
        if (appConfig == null || appConfig.isDeleted()) {
            throw new ServiceException("该项目还未进行配置");
        }
        AppConfigVO vo = new AppConfigVO();
        if (appConfig.getStatus() == 1) {
            vo.setAdSwitch(2);
            vo.setContentSwitch(2);
            vo.setIndexSwitch(2);
        } else {
            String conf = appConfig.getConf();
            if (StringUtils.isNotBlank(conf)) {
                Object parse = JSONObject.parse(conf);
                vo.setConf(parse);
            }
            vo.setAdSwitch(appConfig.getAdSwitch());
            vo.setContentSwitch(appConfig.getContentSwitch());
            vo.setIndexSwitch(appConfig.getIndexSwitch());
        }
        return vo;
    }

    @Override
    public AppConfigVOV2 getAppProjectConfigV2(DeviceForm form) throws ServiceException {
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null || app.isDeleted()) {
            throw new ServiceException("The app does not exist!");
        }
        Channel channel = channelRepository.findByCode(form.getChannel());
        if (channel==null||channel.isDeleted()){
            throw new ServiceException("The channel does not exist!");
        }
        AppVersion appVersion = appVersionRepository.findByAppAndCode(app,form.getPkgVersion());
        if (appVersion==null||appVersion.isDeleted()){
            throw new ServiceException("The version does not exist!");
        }
        AppProject project = appProjectRepository.findByAppAndChannelAndAppVersionAndDeleted(app,channel,appVersion,false);
        if (project == null) {
            throw new ServiceException("The project does not exist!");
        }
        AppConfig appConfig = appConfigRepository.findByAppProject(project);
        if (appConfig == null || appConfig.isDeleted()) {
            throw new ServiceException("The project has not yet been configured!");
        }
        AppConfigVOV2 vo = new AppConfigVOV2();
        if (appConfig.getStatus() == 1) {
            vo.setAdSwitch(2);
            vo.setContentSwitch(2);
            vo.setIndexSwitch(2);
        } else {
            String conf = appConfig.getConf();
            if (StringUtils.isNotBlank(conf)) {
                Object parse = JSONObject.parse(conf);
                vo.setConf(parse);
            }
            vo.setAdSwitch(appConfig.getAdSwitch());
            vo.setContentSwitch(appConfig.getContentSwitch());
            vo.setIndexSwitch(appConfig.getIndexSwitch());
        }
        return vo;
    }

    @Override
    public Map<String, String> getAppConfig(DeviceForm form) throws ServiceException {
        // 查找当前应用
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null || app.isDeleted()) {
            throw new ServiceException("The app does not exist!");
        }
        List<AppMobileConf> list = appMobileConfRepository.findByApp(app);
        Map<String, String> result = Maps.newHashMap();
        for (AppMobileConf appMobileConf : list) {
            if (appMobileConf.isDeleted()) {
                continue;
            }
            if (appMobileConf.getStatus() == 1) {
                result.put(appMobileConf.getKey(), "0");
            } else {
                result.put(appMobileConf.getKey(), appMobileConf.getValue());
            }

        }
        return result;
    }
}
