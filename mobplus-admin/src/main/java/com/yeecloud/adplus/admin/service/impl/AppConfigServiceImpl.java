package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppConfigForm;
import com.yeecloud.adplus.admin.service.AppConfigService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppConfig;
import com.yeecloud.adplus.dal.entity.AppProject;
import com.yeecloud.adplus.dal.entity.QAppConfig;
import com.yeecloud.adplus.dal.repository.AppConfigRepository;
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

/**
 * @author: Huang
 * @create: 2020-12-02 17:32
 */
@Slf4j
@Service
public class AppConfigServiceImpl implements AppConfigService {


    @Autowired
    private AppConfigRepository appConfigRepository;

    @Override
    public Page<AppConfig> query(Query query) throws ServiceException {
        try {
            QAppConfig appConfig = QAppConfig.appConfig;
            Predicate predicate = appConfig.deleted.eq(false);
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, appConfig.status.eq(status));
            }
            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appConfig.app.id.eq(appId));
            }
            Integer appVersionId = query.get("appVersionId", Integer.class);
            if (appVersionId != null && appVersionId > 0) {
                predicate = ExpressionUtils.and(predicate, appConfig.appVersion.id.eq(appVersionId));
            }
            Integer channelId = query.get("channelId", Integer.class);
            if (channelId != null && channelId > 0) {
                predicate = ExpressionUtils.and(predicate, appConfig.channel.id.eq(channelId));
            }
            String name = query.get("name", String.class);
            if (StringUtils.isNotBlank(name)) {
                String express = "%".concat(name.concat("%"));
                predicate = ExpressionUtils.and(predicate, appConfig.name.like(express));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appConfigRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppConfig findById(Integer id) throws ServiceException {
        try {
            AppConfig appConfig = appConfigRepository.findById(id).orElse(null);
            if (appConfig != null && !appConfig.isDeleted()) {
                return appConfig;
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppConfigForm form) throws ServiceException {
        try {
            AppConfig entity = appConfigRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                appConfigRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void controlAd(Integer[] ids, int i) throws ServiceException {
        try {
            appConfigRepository.controlAd(ids, i);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }
}
