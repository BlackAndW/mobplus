package com.yeecloud.adplus.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppForm;
import com.yeecloud.adplus.admin.service.AppService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppConfigRepository;
import com.yeecloud.adplus.dal.repository.AppProjectRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.AppVersionRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author: Huang
 * @create: 2020-12-02 10:08
 */
@Slf4j
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Autowired
    private AppProjectRepository appProjectRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Override
    public Page<App> query(Query query) throws ServiceException {
        try {
            QApp app = QApp.app;
            Predicate predicate = app.deleted.eq(false);
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, app.status.eq(status));
            }
            Integer type = query.get("type", Integer.class);
            if (type != null && type > 0) {
                predicate = ExpressionUtils.and(predicate, app.type.eq(type));
            }
            Integer vpn = query.get("vpn", Integer.class);
            if (vpn != null && vpn > 0) {
                predicate = ExpressionUtils.and(predicate, app.vpn.eq(vpn));
            }
            Integer runtime = query.get("runtime", Integer.class);
            if (type != null && type > 0) {
                predicate = ExpressionUtils.and(predicate, app.runtime.eq(runtime));
            }
            String name = query.get("name", String.class);
            if (StringUtils.isNotBlank(name)) {
                String express = "%".concat(name.concat("%"));
                predicate = ExpressionUtils.and(predicate, app.name.like(express));
            }
            String appId = query.get("appId", String.class);
            if (StringUtils.isNotBlank(appId)) {
                predicate = ExpressionUtils.and(predicate, app.appId.eq(appId));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public App findById(Integer id) throws ServiceException {
        try {
            App app = appRepository.findById(id).orElse(null);
            if (app != null && !app.isDeleted()) {
                return app;
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AppForm form) throws ServiceException {
        try {
            App entity = new App();
            entity.setAppId(IdUtil.objectId());
            entity.setConf(form.getExtra().toJSONString());
            NewBeanUtils.copyProperties(entity, form, true);
            appRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppForm form) throws ServiceException {
        try {
            App entity = appRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                entity.setConf(form.getExtra().toJSONString());
                NewBeanUtils.copyProperties(entity, form, true);
                appRepository.save(entity);
            }
            List<AppProject> projectList = appProjectRepository.findAllByApp(entity);
            projectList.forEach( appProject -> {
                String[] nameList = appProject.getName().split("-");
                String newName = entity.getName() + "-"
                        + nameList[nameList.length - 2] + "-"
                        + nameList[nameList.length - 1];
                appProject.setName(newName);
                appProjectRepository.save(appProject);
            });
            List<AppConfig> configList = appConfigRepository.findAllByApp(entity);
            configList.forEach(appConfig -> {
                String[] nameList = appConfig.getName().split("-");
                String newName = entity.getName() + "-"
                        + nameList[nameList.length - 3] + "-"
                        + nameList[nameList.length - 2] + "-" +
                        nameList[nameList.length - 1];
                appConfig.setName(newName);
                appConfigRepository.save(appConfig);
            });
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        for (int id : ids) {
            App app = appRepository.findById(id).orElse(null);
            List<AppVersion> appVersionList = appVersionRepository.findAllByAppAndDeleted(app, false);
            if (!appVersionList.isEmpty()){
                for (AppVersion appVersion : appVersionList) {
                    List<AppProject> appProjectList = appProjectRepository.findAllByAppVersionAndDeleted(appVersion, false);
                    if (!appProjectList.isEmpty()) {
                        for (AppProject appProject : appProjectList) {
                            AppConfig appConfig = appConfigRepository.findByAppProject(appProject);
                            appConfigRepository.deleteById(appConfig.getId());
                            appProjectRepository.deleteById(appProject.getId());
                        }
                    }
                    appVersionRepository.deleteById(appVersion.getId());
                }
            }
            appRepository.deleteById(id);
        }
    }
}
