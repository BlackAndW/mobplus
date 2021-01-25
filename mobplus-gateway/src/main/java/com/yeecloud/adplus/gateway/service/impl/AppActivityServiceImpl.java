package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppActivityRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppActivityAwardVO;
import com.yeecloud.adplus.gateway.controller.vo.AppActivityTaskVO;
import com.yeecloud.adplus.gateway.controller.vo.AppActivityVO;
import com.yeecloud.adplus.gateway.service.AppActivityService;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.yeecloud.meeto.common.util.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: Leonard
 * @create: 2021/1/20
 */

@Slf4j
@Service
public class AppActivityServiceImpl implements AppActivityService {

    @Autowired
    AppActivityRepository appActivityRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AppActivityVO> getAppActivityList(DeviceForm form) throws ServiceException{
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null || app.isDeleted()) {
            throw new ServiceException("该应用不存在！");
        }
        QAppActivity appActivity = QAppActivity.appActivity;
        Predicate predicate = appActivity.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, appActivity.app.id.eq(app.getId()));
        predicate = ExpressionUtils.and(predicate, appActivity.channel.code.eq(form.getChannel()));
        predicate = ExpressionUtils.and(predicate, appActivity.appVersion.code.eq(form.getPkgVersion()));
        List<AppActivity> appActivityList = jpaQueryFactory
                .selectFrom(appActivity)
                .where(predicate)
                .fetch();
        List<AppActivityVO> appActivityVOList = new ArrayList<>();
        log.info("appActivityList: " + appActivityList.toString());
        for(AppActivity appActivityItem : appActivityList){
            AppActivityVO appActivityVO = new AppActivityVO();
            NewBeanUtils.copyProperties(appActivityVO, appActivityItem);
            appActivityVO.setSessionCount(countSession());
            appActivityVO.setTaskList(getAppTaskList(appActivityItem));
            appActivityVO.setAwardList(getAppAwardList(appActivityItem));
            appActivityVOList.add(appActivityVO);
        }
        return appActivityVOList;
    }

    public List<AppActivityTaskVO> getAppTaskList(AppActivity appActivityItem) throws ServiceException {
        log.info("getAppTaskList的appActivityItem: " + appActivityItem.toString());
        QAppActivityTask activityTask = QAppActivityTask.appActivityTask;
        Predicate predicate = activityTask.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, activityTask.appActivity.id.eq(appActivityItem.getId()));
        List<AppActivityTask> appActivityTaskList = jpaQueryFactory
                .selectFrom(activityTask)
                .where(predicate)
                .fetch();
        List<AppActivityTaskVO> AppActivityTaskVOList = new ArrayList<>();
        for (AppActivityTask appActivityTaskItem: appActivityTaskList) {
            AppActivityTaskVO appActivityTaskVO = new AppActivityTaskVO();
            NewBeanUtils.copyProperties(appActivityTaskVO, appActivityTaskItem);
            AppActivityTaskVOList.add(appActivityTaskVO);
        }
        return AppActivityTaskVOList;
    }

    public List<AppActivityAwardVO> getAppAwardList(AppActivity appActivityItem) throws ServiceException {
        QAppActivityAward activityAward = QAppActivityAward.appActivityAward;
        Predicate predicate = activityAward.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, activityAward.appActivity.id.eq(appActivityItem.getId()));
        List<AppActivityAward> appActivityAwardList = jpaQueryFactory
                .selectFrom(activityAward)
                .where(predicate)
                .fetch();
        List<AppActivityAwardVO> AppActivityAwardVOList = new ArrayList<>();
        for (AppActivityAward appActivityAwardItem: appActivityAwardList) {
            AppActivityAwardVO appActivityAwardVO = new AppActivityAwardVO();
            NewBeanUtils.copyProperties(appActivityAwardVO, appActivityAwardItem);
            AppActivityAwardVOList.add(appActivityAwardVO);
        }
        return AppActivityAwardVOList;
    }

    private int countSession(){
        DateTime startTime = new DateTime("2021-1-18");
        DateTime nowTime = new DateTime(new Date());
        Period dayPeriod = new Period(startTime, nowTime, PeriodType.days());
        return (dayPeriod.getDays()/7 + 1);
    }
}
