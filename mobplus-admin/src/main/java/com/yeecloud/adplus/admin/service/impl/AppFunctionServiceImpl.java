package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.yeecloud.adplus.admin.controller.app.form.AppFunctionForm;
import com.yeecloud.adplus.admin.service.AppFunctionService;
import com.yeecloud.adplus.dal.entity.AppFunction;
import com.yeecloud.adplus.dal.entity.QAppFunction;
import com.yeecloud.adplus.dal.repository.AppFunctionRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
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

    @Override
    public Page<AppFunction> query(Query query) throws ServiceException {
        try {
            QAppFunction appFunction = QAppFunction.appFunction;
            Predicate predicate = appFunction.deleted.eq(false);

            Integer appId = query.get("appId", Integer.class);
            if (null != appId && appId > 0){
                predicate = ExpressionUtils.and(predicate, appFunction.app.id.eq(appId));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
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
        AppFunction entity = new AppFunction();
        NewBeanUtils.copyProperties(entity, form, true);
        entity.setAdTypeConf(form.getAdTypeList());
        try {
            appFunctionRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppFunctionForm form) throws ServiceException {
        try {
            AppFunction entity = appFunctionRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                entity.setAdTypeConf(form.getAdTypeList());
                appFunctionRepository.save(entity);
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
}
