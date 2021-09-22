package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.UserFeedback;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.UserFeedbackRepository;
import com.yeecloud.adplus.gateway.controller.form.UserFeedbackForm;
import com.yeecloud.adplus.gateway.service.UserFeedbackService;
import com.yeecloud.adplus.gateway.util.EmptyUtil;
import com.yeecloud.meeto.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2021/9/3
 */
@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {

    @Autowired
    UserFeedbackRepository userFeedbackRepository;

    @Autowired
    AppRepository appRepository;

    @Transactional
    @Override
    public void commit(UserFeedbackForm form, String ip, String area) throws ServiceException {
        UserFeedback userFeedback = new UserFeedback();
        NewBeanUtils.copyProperties(userFeedback, form, true);
        App app = appRepository.findByAppId(form.getAppId());
        if (!EmptyUtil.isEmpty(app)) {
            userFeedback.setApp(app);
            userFeedback.setIp(ip);
            userFeedback.setArea(area);
        } else {
            throw new ServiceException("appId:" + form.getAppId() + ",this app is not exist");
        }
        userFeedbackRepository.save(userFeedback);
    }
}
