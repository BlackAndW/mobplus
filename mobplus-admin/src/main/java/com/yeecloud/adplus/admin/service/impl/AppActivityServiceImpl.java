package com.yeecloud.adplus.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityAwardForm;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityForm;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityTaskForm;
import com.yeecloud.adplus.admin.service.AppActivityService;
import com.yeecloud.adplus.admin.service.AppActivityTaskService;
import com.yeecloud.adplus.admin.util.BaseUtil;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.*;
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
 * @author: Leonard
 * @create: 2021/1/18
 */
@Slf4j
@Service
public class AppActivityServiceImpl implements AppActivityService {

    @Autowired
    private AppActivityRepository appActivityRepository;

    @Autowired
    private AppActivityTaskRepository appActivityTaskRepository;

    @Autowired
    private AppActivityAwardRepository appActivityAwardRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AppActivity> query(Query query) throws ServiceException {
        try {
            QAppActivity appActivity = QAppActivity.appActivity;
            Predicate predicate = appActivity.deleted.eq(false);

            Integer appId = query.get("appId", Integer.class);
            String channelCode = query.get("channelCode", String.class);
            String appVersionCode = query.get("appVersionCode", String.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appActivity.app.id.eq(appId));
            }
            if (!StringUtils.isEmpty(channelCode) && !channelCode.equals("0")) {
                predicate = ExpressionUtils.and(predicate, appActivity.channelList.like("%" + channelCode + "%"));
            }
            if (!StringUtils.isEmpty(appVersionCode) && !appVersionCode.equals("0")) {
                predicate = ExpressionUtils.and(predicate, appActivity.appVersionList.like("%" + appVersionCode + "%"));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
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
        try {
            App app = appRepository.findById(form.getAppId()).orElse(null);
            if ( null != app) {
                appActivity.setApp(app);
                copyAppActivityValue(appActivity, form);
                appActivityRepository.save(appActivity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppActivityForm form) throws ServiceException {
        try {
            AppActivity appActivity = appActivityRepository.findById(id).orElse(null);
            App app = appRepository.findById(form.getAppId()).orElse(null);
            if (appActivity != null && !appActivity.isDeleted() && null != app) {
                copyAppActivityValue(appActivity, form);
                appActivityRepository.save(appActivity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) {
        appActivityRepository.deleteById(ids);
    }

    @Override
    public Page<AppActivityTask> queryTask(Query query) throws ServiceException {
        try{
            QAppActivityTask appActivityTask = QAppActivityTask.appActivityTask;
            Predicate predicate = appActivityTask.deleted.eq(false);

            Integer activityId = query.get("activityId", Integer.class);
            if (activityId != null && activityId > 0){
                predicate = ExpressionUtils.and(predicate, appActivityTask.appActivity.id.eq(activityId));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appActivityTaskRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppActivityTask findByTaskId(Integer id) throws ServiceException {
        try {
            AppActivityTask entity = appActivityTaskRepository.findById(id).orElse(null);
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
    public void create(AppActivityTaskForm form) throws ServiceException {
        AppActivityTask appActivityTask = new AppActivityTask();
        NewBeanUtils.copyProperties(appActivityTask, form, true);
        try {
            appActivityTaskRepository.save(appActivityTask);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppActivityTaskForm form) throws ServiceException {
        try {
            AppActivityTask entity = appActivityTaskRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                appActivityTaskRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteTask(Integer[] ids) throws ServiceException {
        appActivityTaskRepository.deleteById(ids);
    }

    @Override
    public Page<AppActivityAward> queryAward(Query query) throws ServiceException {
        try {
            QAppActivityAward appActivityAward = QAppActivityAward.appActivityAward;
            Predicate predicate = appActivityAward.deleted.eq(false);

            Integer activityId = query.get("activityId", Integer.class);
            if (activityId != null && activityId > 0){
                predicate = ExpressionUtils.and(predicate, appActivityAward.appActivity.id.eq(activityId));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appActivityAwardRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppActivityAward findByAwardId(Integer id) throws ServiceException {
        try {
            AppActivityAward entity = appActivityAwardRepository.findById(id).orElse(null);
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
    public void create(AppActivityAwardForm form) throws ServiceException {
        AppActivityAward appActivityAward = new AppActivityAward();
        NewBeanUtils.copyProperties(appActivityAward, form, true);
        try {
            appActivityAwardRepository.save(appActivityAward);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AppActivityAwardForm form) throws ServiceException {
        try {
            AppActivityAward entity = appActivityAwardRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                appActivityAwardRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAward(Integer[] ids) throws ServiceException {
        appActivityAwardRepository.deleteById(ids);
    }

    private void copyAppActivityValue(AppActivity appActivity, AppActivityForm form) {
        NewBeanUtils.copyProperties(appActivity, form, true);
        appActivity.setAppVersionList(BaseUtil.formatList2String(form.getAppVersionCheckList()));
        appActivity.setChannelList(BaseUtil.formatList2String(form.getChannelCheckList()));
    }

}
