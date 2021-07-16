package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.form.AppLinkForm;
import com.yeecloud.adplus.admin.service.AppLinkService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppLink;
import com.yeecloud.adplus.dal.entity.QAppLink;
import com.yeecloud.adplus.dal.repository.AppLinkRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Service
public class AppLinkServiceImpl implements AppLinkService {

    @Value("${file.path.aws.rootPath}")
    String rootPath;

    @Value("${file.path.aws.imgKeyPath}")
    String imgKeyPath;

    @Autowired
    AppLinkRepository appLinkRepository;

    @Autowired
    private AppRepository appRepository;

    @Override
    public Page<AppLink> query(Query query) throws ServiceException {
        QAppLink qAppLink = QAppLink.appLink;
        Predicate predicate = qAppLink.deleted.eq(false);
        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            predicate = ExpressionUtils.and(predicate, qAppLink.app.id.eq(appId));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return appLinkRepository.findAll(predicate, pagRequest);
    }

    @Override
    public AppLink findById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AppLinkForm form) throws ServiceException {
        try {
            AppLink entity = new AppLink();
            App app = appRepository.findById(form.getAppId()).orElse(null);
            if (app != null) {
                entity.setApp(app);
                NewBeanUtils.copyProperties(entity, form, true);
                appLinkRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppLinkForm form) throws ServiceException {
        try {
            AppLink appLink = appLinkRepository.findById(id).orElse(null);
            App app = appRepository.findById(form.getAppId()).orElse(null);
            if (appLink != null && !appLink.isDeleted() && app != null) {
                NewBeanUtils.copyProperties(appLink, form, true);
                appLinkRepository.save(appLink);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        appLinkRepository.deleteById(ids);
    }
}
