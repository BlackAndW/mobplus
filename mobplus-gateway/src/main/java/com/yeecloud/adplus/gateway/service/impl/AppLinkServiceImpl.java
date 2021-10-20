package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppLinkLogRepository;
import com.yeecloud.adplus.dal.repository.AppLinkRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.vo.AppLinkVO;
import com.yeecloud.adplus.gateway.service.AppLinkService;
import com.yeecloud.adplus.gateway.util.EmptyUtil;
import com.yeecloud.adplus.gateway.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Service
@Slf4j
public class AppLinkServiceImpl implements AppLinkService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    AppLinkRepository appLinkRepository;

    @Autowired
    AppLinkLogRepository appLinkLogRepository;

    @Override
    public AppLinkVO query(AppLinkForm form) {
        QAppLink qAppLink = QAppLink.appLink;
        Predicate predicate = qAppLink.deleted.eq(false);
        if (form.getAppId() != null && form.getAppId().length() > 0) {
            App app = appRepository.findByAppId(form.getAppId());
            predicate = ExpressionUtils.and(predicate, qAppLink.app.eq(app));
        }
        if (form.getId() != null && form.getId() > 0) {
            predicate = ExpressionUtils.and(predicate, qAppLink.id.eq(form.getId()));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));

        PageRequest pageRequest = PageRequest.of(0, 500, sort);
        List<AppLink> appLinks = appLinkRepository.findAll(predicate, pageRequest).getContent();
        Integer randomIndex = new Random().nextInt(appLinks.size());
        AppLink appLink = appLinks.get(randomIndex);
        AppLinkVO vo = new AppLinkVO();
        NewBeanUtils.copyProperties(vo, appLink, true);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public synchronized Result updateData(AppLinkForm form) {
        if (form.getId() != null && form.getId() > 0) {
            AppLink appLink = appLinkRepository.findById(form.getId()).orElse(null);
            if (EmptyUtil.isEmpty(appLink)) {
                return Result.FAILURE("There is no app link about this id: " + form.getId());
            }

            QAppLinkLog QappLinkLog = QAppLinkLog.appLinkLog;
            Predicate predicate = QappLinkLog.deleted.eq(false);
            predicate = ExpressionUtils.and(predicate, QappLinkLog.appLink.id.eq(form.getId()));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(0, 1, sort);
            Page<AppLinkLog> appLinkLogPage = appLinkLogRepository.findAll(predicate, pagRequest);
            // 不存在对应link_id的log，新增
            if (appLinkLogPage.getContent().size() == 0) {
                log.info("create new app link log, link id is {}", form.getId());
                AppLinkLog newRecord = new AppLinkLog();
                updateLog(newRecord, form, appLink);
                appLinkLogRepository.save(newRecord);
                return Result.SUCCESS("upload data success!");
            }

            AppLinkLog appLinkLog = appLinkLogPage.getContent().get(0);
            DateTime today = new DateTime();
            today = today.withMillisOfDay(0);
            DateTime appLinkCreatedDay = new DateTime(appLinkLog.getCreatedAt()).withMillisOfDay(0);
            Period period = new Period(appLinkCreatedDay, today, PeriodType.days());
            // 相隔一天以上的数据，新增
            if (period.getDays() >= 1) {
                AppLinkLog newRecord = new AppLinkLog();
                updateLog(newRecord, form, appLink);
                appLinkLogRepository.save(newRecord);
                return Result.SUCCESS("upload data success!");
            }

            updateLog(appLinkLog, form, appLink);
            appLinkLogRepository.save(appLinkLog);
            return Result.SUCCESS("upload data success!");
        }
        return Result.FAILURE("app link id is empty");
    }

    private void updateLog(AppLinkLog appLinkLog, AppLinkForm form, AppLink appLink) {
        appLinkLog.setAppLink(appLink);
        if (form.getClickNum() != null && form.getClickNum() > 0) {
            appLinkLog.setClickNum(appLinkLog.getClickNum() + 1);
        }
        if (form.getShowNum() != null && form.getShowNum() > 0) {
            appLinkLog.setShowNum(appLinkLog.getShowNum() + 1);
        }
    }
}
