package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.querydsl.core.types.ExpressionUtils;
import com.yeecloud.adplus.admin.controller.app.form.AppFunctionForm;
import com.yeecloud.adplus.admin.service.AppFunctionService;
import com.yeecloud.adplus.admin.util.BaseUtil;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppFunctionRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.AppVersionRepository;
import com.yeecloud.adplus.dal.repository.ChannelRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.querydsl.core.types.Predicate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

@Slf4j
@Service
public class AppFunctionServiceImpl implements AppFunctionService {

    @Autowired
    AppFunctionRepository appFunctionRepository;

    @Autowired
    AppRepository appRepository;

    @Autowired
    AppVersionRepository appVersionRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Override
    public Page<AppFunction> query(Query query) throws ServiceException {
        try {
            QAppFunction appFunction = QAppFunction.appFunction;
            Predicate predicate = appFunction.deleted.eq(false);

            Integer appId = query.get("appId", Integer.class);
            String channelCode = query.get("channelCode", String.class);
            String appVersionCode = query.get("appVersionCode", String.class);
            if (null != appId && appId > 0){
                predicate = ExpressionUtils.and(predicate, appFunction.app.id.eq(appId));
            }
            if (!StringUtils.isEmpty(channelCode) && !channelCode.equals("0")) {
                predicate = ExpressionUtils.and(predicate, appFunction.channelList.like("%" + channelCode + "%"));
            }
            if (!StringUtils.isEmpty(appVersionCode) && !appVersionCode.equals("0")) {
                predicate = ExpressionUtils.and(predicate, appFunction.appVersionList.like("%" + appVersionCode + "%"));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appFunctionRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppFunction findById(Integer id) throws ServiceException {
        try {
            AppFunction entity = appFunctionRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return entity;
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AppFunctionForm form) throws ServiceException {
        AppFunction appFunction = new AppFunction();
        try {
            App app = appRepository.findById(form.getAppId()).orElse(null);
            if (null != app){
                appFunction.setApp(app);
                copyAppFunctionValue(appFunction, form);
                appFunctionRepository.save(appFunction);
            }

        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppFunctionForm form) throws ServiceException {
        try {
            App app = appRepository.findById(form.getAppId()).orElse(null);
            AppFunction appFunction = appFunctionRepository.findById(id).orElse(null);
            if (appFunction != null && !appFunction.isDeleted() && null != app) {
                appFunction.setApp(app);
                copyAppFunctionValue(appFunction, form);
                appFunctionRepository.save(appFunction);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        appFunctionRepository.deleteById(ids);
    }

    private void copyAppFunctionValue(AppFunction appFunction, AppFunctionForm form) {
        NewBeanUtils.copyProperties(appFunction, form, true);
        System.out.println(appFunction);
        appFunction.setAdTypeConf(form.getAdTypeList());
        appFunction.setAppVersionList(BaseUtil.formatList2String(form.getAppVersionCheckList()));
        appFunction.setChannelList(BaseUtil.formatList2String(form.getChannelCheckList()));
    }
}
