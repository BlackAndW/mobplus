package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.UserFeedbackForm;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * @author: Leonard
 * @create: 2021/9/3
 */
public interface UserFeedbackService {

    void commit(UserFeedbackForm form, String ip, String area) throws ServiceException;
}
