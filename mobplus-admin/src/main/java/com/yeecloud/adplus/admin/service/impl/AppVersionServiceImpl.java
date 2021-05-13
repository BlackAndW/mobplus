package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppVersionForm;
import com.yeecloud.adplus.admin.service.AppVersionService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppConfigRepository;
import com.yeecloud.adplus.dal.repository.AppProjectRepository;
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
 * @create: 2020-12-02 14:42
 */
@Slf4j
@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Autowired
    private AppProjectRepository appProjectRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Override
    public Page<AppVersion> query(Query query) throws ServiceException {
        try {
            QAppVersion appVersion = QAppVersion.appVersion;
            Predicate predicate = appVersion.deleted.eq(false);
            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appVersion.app.id.eq(appId));
            }
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, appVersion.status.eq(status));
            }
            String code = query.get("code", String.class);
            if (StringUtils.isNotBlank(code)) {
                String express = "%".concat(code.concat("%"));
                predicate = ExpressionUtils.and(predicate, appVersion.code.like(code));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appVersionRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppVersion findById(Integer id) throws ServiceException {
        try {
            AppVersion appVersion = appVersionRepository.findById(id).orElse(null);
            if (appVersion != null && !appVersion.isDeleted()) {
                return appVersion;
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AppVersionForm form) throws ServiceException {
        try {
            AppVersion entity = new AppVersion();
            App app = new App();
            app.setId(form.getAppId());
            entity.setApp(app);
            NewBeanUtils.copyProperties(entity, form, true);
            appVersionRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppVersionForm form) throws ServiceException {
        try {
            AppVersion entity = appVersionRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                App app = new App();
                app.setId(form.getAppId());
                entity.setApp(app);
                NewBeanUtils.copyProperties(entity, form, true);
                appVersionRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        // 删除版本时，（不）需要判断该版本下是否存在项目
        for (int id : ids) {
            AppVersion appVersion = appVersionRepository.findById(id).orElse(null);
            List<AppProject> appProjectList = appProjectRepository.findAllByAppVersionAndDeleted(appVersion, false);
            if (!appProjectList.isEmpty()) {
                for (AppProject appProject : appProjectList) {
                    AppConfig appConfig = appConfigRepository.findByAppProject(appProject);
                    appConfigRepository.deleteById(appConfig.getId());
                    appProjectRepository.deleteById(appProject.getId());
                }
            }
            appVersionRepository.deleteById(id);
        }
    }
}
