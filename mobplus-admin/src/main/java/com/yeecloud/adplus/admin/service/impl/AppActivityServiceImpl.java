package com.yeecloud.adplus.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityForm;
import com.yeecloud.adplus.admin.service.AppActivityService;
import com.yeecloud.adplus.dal.entity.AdPosition;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.QAdPosition;
import com.yeecloud.adplus.dal.entity.QAppActivity;
import com.yeecloud.adplus.dal.repository.AdPositionRepository;
import com.yeecloud.adplus.dal.repository.AppActivityRepository;
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
 * @author: Leonard
 * @create: 2021/1/18
 */
@Slf4j
@Service
public class AppActivityServiceImpl implements AppActivityService {

    @Autowired
    private AppActivityRepository appActivityRepository;

    @Override
    public Page<AppActivity> query(Query query) throws ServiceException {
        try {
            QAppActivity appActivity = QAppActivity.appActivity;
            Predicate predicate = appActivity.deleted.eq(false);

            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appActivity.app.id.eq(appId));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appActivityRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppActivity findById(Integer id) throws ServiceException {
        try {
            AppActivity entity = appActivityRepository.findById(id).orElse(null);
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
    public void create(AppActivityForm form) throws ServiceException {
        AppActivity appActivity = new AppActivity();
        NewBeanUtils.copyProperties(appActivity, form, true);
        try {
            appActivityRepository.save(appActivity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppActivityForm form) throws ServiceException {
        try {
            AppActivity entity = appActivityRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                appActivityRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        appActivityRepository.deleteById(ids);
    }
}
