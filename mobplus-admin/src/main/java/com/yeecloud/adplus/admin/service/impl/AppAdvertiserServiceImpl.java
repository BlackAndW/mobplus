package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppAdvertiserForm;
import com.yeecloud.adplus.admin.service.AppAdvertiserService;
import com.yeecloud.adplus.dal.entity.AppAdvertiser;
import com.yeecloud.adplus.dal.entity.QAppAdvertiser;
import com.yeecloud.adplus.dal.repository.AppAdvertiserRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Huang
 * @create: 2020-12-09 11:12
 */
@Slf4j
@Service
public class AppAdvertiserServiceImpl implements AppAdvertiserService {

    @Autowired
    private AppAdvertiserRepository appAdvertiserRepository;

    @Override
    public Page<AppAdvertiser> query(Query query) throws ServiceException {
        try {
            QAppAdvertiser appAdvertiser = QAppAdvertiser.appAdvertiser;
            Predicate predicate = appAdvertiser.deleted.eq(false);
            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appAdvertiser.app.id.eq(appId));
            }
            Integer advId = query.get("advId", Integer.class);
            if (advId != null && advId > 0) {
                predicate = ExpressionUtils.and(predicate, appAdvertiser.advertiser.id.eq(advId));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appAdvertiserRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppAdvertiser findById(Integer id) throws ServiceException {
        try {
            AppAdvertiser appAdvertiser = appAdvertiserRepository.findById(id).orElse(null);
            if (appAdvertiser != null && !appAdvertiser.isDeleted()) {
                return appAdvertiser;
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AppAdvertiserForm form) throws ServiceException {
        try {
            AppAdvertiser appAdvertiser1 = appAdvertiserRepository.findByAppAndAdvertiser(form.getApp(), form.getAdvertiser());
            if (appAdvertiser1 != null) {
                throw new ServiceException("存在相同的配置，请重新选择");
            } else {
                AppAdvertiser appAdvertiser2 = new AppAdvertiser();
                NewBeanUtils.copyProperties(appAdvertiser2, form, true);
                appAdvertiserRepository.save(appAdvertiser2);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppAdvertiserForm form) throws ServiceException {
        try {
            AppAdvertiser appAdvertiser1 = appAdvertiserRepository.findById(id).orElse(null);
            AppAdvertiser appAdvertiser2 = appAdvertiserRepository.findByAppAndAdvertiser(form.getApp(), form.getAdvertiser());
            if (appAdvertiser2 != null && !appAdvertiser2.getId().equals(appAdvertiser1.getId())) {
                throw new ServiceException("存在相同的配置，请重新选择");
            }
            if (appAdvertiser1 != null) {
                NewBeanUtils.copyProperties(appAdvertiser1, form, true);
                appAdvertiserRepository.save(appAdvertiser1);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        try {
            appAdvertiserRepository.deleteById(ids);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }
}
