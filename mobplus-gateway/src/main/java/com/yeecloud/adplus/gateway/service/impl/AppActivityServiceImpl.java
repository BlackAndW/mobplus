package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.AppProject;
import com.yeecloud.adplus.dal.entity.QAppActivity;
import com.yeecloud.adplus.dal.repository.AppActivityRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
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
        List<AppActivity> appActivityList = jpaQueryFactory
                .selectFrom(appActivity)
                .where(predicate)
                .fetch();
        List<AppActivityVO> appActivityVOList = new ArrayList<>();
        for(AppActivity appActivityItem : appActivityList){
            AppActivityVO appActivityVO = new AppActivityVO();
            NewBeanUtils.copyProperties(appActivityVO, appActivityItem);
            appActivityVO.setSessionCount(countSession());
            appActivityVOList.add(appActivityVO);
        }
        return appActivityVOList;
    }

    private int countSession(){
        DateTime startTime = new DateTime("2021-1-18");
        DateTime nowTime = new DateTime(new Date());
        Period dayPeriod = new Period(startTime, nowTime, PeriodType.days());
        return (dayPeriod.getDays()/7 + 1);
    }
}
