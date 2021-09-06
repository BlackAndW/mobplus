package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.form.UserFeedbackForm;
import com.yeecloud.adplus.admin.service.UserFeedbackService;
import com.yeecloud.adplus.dal.entity.QUserFeedback;
import com.yeecloud.adplus.dal.entity.UserFeedback;
import com.yeecloud.adplus.dal.repository.UserFeedbackRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/9/2
 */

@Slf4j
@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {

    @Autowired
    UserFeedbackRepository userFeedbackRepository;

    @Override
    public Page<UserFeedback> query(Query query) throws ServiceException, ParseException {

        QUserFeedback QuserFeedback = QUserFeedback.userFeedback;
        Predicate predicate = QuserFeedback.deleted.eq(false);
        Integer appId = query.get("appId", Integer.class);
        if (appId != null && appId > 0) {
            predicate = ExpressionUtils.and(predicate, QuserFeedback.app.id.eq(appId));
        }
        Integer status = query.get("status", Integer.class);
        if (status != null && status > 0) {
            predicate = ExpressionUtils.and(predicate, QuserFeedback.status.eq(status));
        }
        // 按时间选择框筛选数据，默认展示所有数据
        String startTimeStr = query.get("startTimeStr", String.class);
        String endTimeStr = query.get("endTimeStr", String.class);
        if (startTimeStr != null && startTimeStr.length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(startTimeStr).getTime();
            long endTime = simpleDateFormat.parse(endTimeStr).getTime();
            predicate = ExpressionUtils.and(predicate, QuserFeedback.createdAt.between(startTime, endTime));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return userFeedbackRepository.findAll(predicate, pagRequest);
    }

    @Override
    public UserFeedback findById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void create(UserFeedbackForm form) throws ServiceException {

    }

    @Override
    public void update(Integer id, UserFeedbackForm form) throws ServiceException {
        UserFeedback userFeedback = userFeedbackRepository.findById(id).orElse(null);
        if (userFeedback != null) {
            NewBeanUtils.copyProperties(userFeedback, form, true);
            userFeedbackRepository.save(userFeedback);
        }
    }

    @Override
    public void delete(Integer[] ids) throws ServiceException {

    }
}
