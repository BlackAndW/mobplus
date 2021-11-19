package com.yeecloud.adplus.admin.service.impl;

import cn.hutool.core.util.PageUtil;
import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.admin.controller.app.form.AppLinkForm;
import com.yeecloud.adplus.admin.service.AppLinkService;
import com.yeecloud.adplus.admin.util.BaseUtil;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppLinkLogRepository;
import com.yeecloud.adplus.dal.repository.AppLinkRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongSupplier;

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

    @Autowired
    private AppLinkLogRepository appLinkLogRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AppLink> query(Query query) throws ServiceException, ParseException {
        return null;
    }

    @Override
    public Page<AppLinkLog> queryLog(Query query) throws ServiceException, ParseException {
        QAppLinkLog QappLinkLog = QAppLinkLog.appLinkLog;
        Predicate predicate = QappLinkLog.deleted.eq(false);
        QAppLink QappLink = QAppLink.appLink;
        Predicate predicate2 = QappLink.deleted.eq(false);

        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            predicate = ExpressionUtils.and(predicate, QappLinkLog.appLink.app.id.eq(appId));
            predicate2 = ExpressionUtils.and(predicate2, QappLink.app.id.eq(appId));
        }
        String startTimeStr = query.get("startTimeStr", String.class);
        String endTimeStr = query.get("endTimeStr", String.class);
        // 默认统计当天数据
        if (startTimeStr != null && startTimeStr.length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(startTimeStr).getTime();
            long endTime = simpleDateFormat.parse(endTimeStr).getTime();
            predicate = ExpressionUtils.and(predicate, QappLinkLog.createdAt.between(startTime, endTime));
        } else {
            DateTime today = new DateTime();
            long startTime = today.withMillisOfDay(0).getMillis();
            long endTime = today.plusDays(1).withMillisOfDay(0).getMillis();
            predicate = ExpressionUtils.and(predicate, QappLinkLog.createdAt.between(startTime, endTime));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "clickNum"), new Sort.Order(Sort.Direction.DESC, "showNum"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        Integer modeType = query.get("modeType", Integer.class);

        // 默认1是累计模式
        if (modeType == 1) {
            List<AppLink> linkList = (List<AppLink>) appLinkRepository.findAll(predicate2);
            // 聚合函数统计
            List<Tuple> logSumList = jpaQueryFactory
                    .select(QappLinkLog.appLink, QappLinkLog.appLink.id, QappLinkLog.clickNum.sum(), QappLinkLog.showNum.sum())
                    .from(QappLinkLog)
                    .where(predicate)
                    .groupBy(QappLinkLog.appLink.id)
                    .orderBy(QappLinkLog.clickNum.sum().desc(), QappLinkLog.showNum.sum().desc())
                    .fetch();
            List<AppLinkLog> appLinkLogList = new ArrayList<>();
            if (logSumList != null) {
                List<Integer> linkIdList = new ArrayList<>();
                // 将有统计值的数据录入列表
                logSumList.forEach(logSum -> {
                    List<Object> list = Arrays.asList(logSum.toArray());
                    AppLinkLog log = new AppLinkLog();
                    linkIdList.add((Integer) list.get(1));
                    log.setAppLink((AppLink) list.get(0));
                    log.setClickNum((Integer) list.get(2));
                    log.setShowNum((Integer) list.get(3));
                    appLinkLogList.add(log);
                });
                // 将无统计值的数据补足
                for (AppLink appLink : linkList) {
                    if (!linkIdList.contains(appLink.getId())) {
                        AppLinkLog log = new AppLinkLog();
                        log.setAppLink(appLink);
                        appLinkLogList.add(log);
                    }
                }
            }
            return new PageImpl<>(BaseUtil.getPageList(query, appLinkLogList), pagRequest, appLinkLogList.size());
        }
        return appLinkLogRepository.findAll(predicate, pagRequest);
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
