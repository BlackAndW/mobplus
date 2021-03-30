package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.UserAdInfoForm;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */
public interface UserAdInfoService {

    void createOrUpdateInfo(UserAdInfoForm userForm, String userIp);
}
