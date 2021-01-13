package com.yeecloud.adplus.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppProjectForm;
import com.yeecloud.adplus.admin.service.AppProjectService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.*;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Huang
 * @create: 2020-12-02 15:59
 */
@Slf4j
@Service
public class AppProjectServiceImpl implements AppProjectService {

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

    @Override
    public Page<AppProject> query(Query query) throws ServiceException {
        try {
            QAppProject appProject = QAppProject.appProject;
            Predicate predicate = appProject.deleted.eq(false);
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, appProject.status.eq(status));
            }
            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appProject.app.id.eq(appId));
            }
            Integer appVersionId = query.get("appVersionId", Integer.class);
            if (appVersionId != null && appVersionId > 0) {
                predicate = ExpressionUtils.and(predicate, appProject.appVersion.id.eq(appVersionId));
            }
            Integer channelId = query.get("channelId", Integer.class);
            if (channelId != null && channelId > 0) {
                predicate = ExpressionUtils.and(predicate, appProject.channel.id.eq(channelId));
            }
            String name = query.get("name", String.class);
            if (StringUtils.isNotBlank(name)) {
                String express = "%".concat(name.concat("%"));
                predicate = ExpressionUtils.and(predicate, appProject.name.like(express));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appProjectRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppProject findById(Integer id) throws ServiceException {
        try {
            AppProject entity = appProjectRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return entity;
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AppProjectForm form) throws ServiceException {
        try {
            // 默认这些都存在
            App app = appRepository.findById(form.getAppId()).orElse(null);
            Channel channel = channelRepository.findById(form.getChannelId()).orElse(null);
            AppVersion appVersion = appVersionRepository.findById(form.getAppVersionId()).orElse(null);
            AppProject entity = new AppProject();
            // 判断是否当前应用存在相同版本相同渠道的项目
            checkAppProject(entity, app, channel, appVersion);
            // 创建项目
            entity.setApp(app);
            entity.setAppVersion(appVersion);
            entity.setChannel(channel);
            entity.setCode(IdUtil.objectId());
            entity.setName(app.getName() + "-" + appVersion.getCode() + "-" + channel.getName());
            entity.setStatus(form.getStatus());
            entity.setRemark(form.getRemark());
            entity = appProjectRepository.save(entity);
            // 初始化项目配置
            AppConfig appConfig = new AppConfig();
            appConfig.setApp(app);
            appConfig.setAppProject(entity);
            appConfig.setAppVersion(entity.getAppVersion());
            appConfig.setChannel(entity.getChannel());
            appConfig.setName(entity.getName() + "-配置");
            appConfig.setAdSwitch(2);
            appConfig.setContentSwitch(2);
            appConfig.setIndexSwitch(2);
            appConfig.setStatus(2);
            appConfigRepository.save(appConfig);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppProjectForm form) throws ServiceException {
        try {
            AppProject entity = appProjectRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                App app = appRepository.findById(form.getAppId()).orElse(null);
                Channel channel = channelRepository.findById(form.getChannelId()).orElse(null);
                AppVersion appVersion = appVersionRepository.findById(form.getAppVersionId()).orElse(null);
                checkAppProject(entity, app, channel, appVersion);
                entity.setApp(app);
                entity.setAppVersion(appVersion);
                entity.setChannel(channel);
                entity.setCode(IdUtil.simpleUUID());
                entity.setName(app.getName() + "-" + appVersion.getCode() + "-" + channel.getName());
                entity.setStatus(form.getStatus());
                entity.setRemark(form.getRemark());
                entity = appProjectRepository.save(entity);
                AppConfig appConfig = appConfigRepository.findByAppProject(entity);
                appConfig.setName(entity.getName() + "-配置");
                appConfigRepository.save(appConfig);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    private void checkAppProject(AppProject appProject, App app, Channel channel, AppVersion appVersion) throws ServiceException {
        AppProject entity = appProjectRepository.findByAppAndChannelAndAppVersionAndDeleted(app, channel, appVersion, false);
        if (entity != null && !entity.getId().equals(appProject.getId())) {
            throw new ServiceException("相同的项目已经存在，请重新选择");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        // 删除应用项目时需要判断当前应用项目的配置是否设为未启用
        for (int id : ids) {
            AppProject appProject = appProjectRepository.findById(id).orElse(null);
            AppConfig appConfig = appConfigRepository.findByAppProject(appProject);
            if (appConfig.getStatus() == 2) {
                throw new ServiceException("当前删除的项目中存在还在使用的项目，请修改后再进行操作");
            } else {
                appProjectRepository.deleteById(id);
                appConfigRepository.deleteById(appConfig.getId());
            }
        }

    }
}
