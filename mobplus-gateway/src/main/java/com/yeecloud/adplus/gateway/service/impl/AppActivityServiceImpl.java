package com.yeecloud.adplus.gateway.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.QAppActivity;
import com.yeecloud.adplus.dal.repository.AppActivityRepository;
import com.yeecloud.adplus.gateway.service.AppActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.yeecloud.meeto.common.util.Query;

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

    @Override
    public Page<AppActivity> getAppActivityPage(Query query) {
        QAppActivity appActivity = QAppActivity.appActivity;
        Predicate predicate = appActivity.deleted.eq(false);

        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            predicate = ExpressionUtils.and(predicate, appActivity.app.id.eq(appId));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return appActivityRepository.findAll(predicate, pagRequest);
    }

    @Override
    public Optional<AppActivity> getAppActivityList(Query query) {
        QAppActivity appActivity = QAppActivity.appActivity;
        Predicate predicate = appActivity.deleted.eq(false);

        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            return null;
        }
        return appActivityRepository.findById(appId);
    }
}
