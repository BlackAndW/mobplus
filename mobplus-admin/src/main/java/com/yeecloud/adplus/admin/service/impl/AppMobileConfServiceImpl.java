package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppMobileConfForm;
import com.yeecloud.adplus.admin.service.AppMobileConfService;
import com.yeecloud.adplus.admin.util.BaseUtil;
import com.yeecloud.adplus.dal.entity.AppMobileConf;
import com.yeecloud.adplus.dal.entity.QAppMobileConf;
import com.yeecloud.adplus.dal.repository.AppMobileConfRepository;
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
 * @create: 2020-12-15 09:45
 */
@Slf4j
@Service
public class AppMobileConfServiceImpl implements AppMobileConfService {

    @Autowired
    private AppMobileConfRepository appMobileConfRepository;


    @Override
    public Page<AppMobileConf> query(Query query) throws ServiceException {
        try {
            QAppMobileConf appMobileConf = QAppMobileConf.appMobileConf;
            Predicate predicate = appMobileConf.deleted.eq(false);
            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appMobileConf.app.id.eq(appId));
            }
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, appMobileConf.status.eq(status));
            }
            String name = query.get("name", String.class);
            if (StringUtils.isNotBlank(name)) {
                predicate = ExpressionUtils.and(predicate, appMobileConf.name.like("%" + name + "%"));
            }
            String key = query.get("key", String.class);
            if (StringUtils.isNotBlank(key)) {
                predicate = ExpressionUtils.and(predicate, appMobileConf.key.like("%" + key + "%"));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appMobileConfRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppMobileConf findById(Integer id) throws ServiceException {
        try {
            AppMobileConf appMobileConf = appMobileConfRepository.findById(id).orElse(null);
            if (appMobileConf != null && !appMobileConf.isDeleted()) {
                return appMobileConf;
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AppMobileConfForm form) throws ServiceException {
        try {
            AppMobileConf appMobileConf = new AppMobileConf();
            copyAppMobileConfValue(appMobileConf, form);
            appMobileConfRepository.save(appMobileConf);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppMobileConfForm form) throws ServiceException {
        try {
            AppMobileConf appMobileConf = appMobileConfRepository.findById(id).orElse(null);
            if (appMobileConf != null && !appMobileConf.isDeleted()) {
                copyAppMobileConfValue(appMobileConf, form);
                appMobileConfRepository.save(appMobileConf);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        try {
            appMobileConfRepository.deleteById(ids);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    private void copyAppMobileConfValue (AppMobileConf appMobileConf, AppMobileConfForm form) {
        NewBeanUtils.copyProperties(appMobileConf, form, true);
        appMobileConf.setAppVersionList(BaseUtil.formatList2String(form.getAppVersionCheckList()));
        appMobileConf.setChannelList(BaseUtil.formatList2String(form.getChannelCheckList()));
    }
}
