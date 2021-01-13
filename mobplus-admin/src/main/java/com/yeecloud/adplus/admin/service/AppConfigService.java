package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppConfigForm;
import com.yeecloud.adplus.dal.entity.AppConfig;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
*@author: Huang
*@create: 2020-12-02 17:32
*/
public interface AppConfigService {
    Page<AppConfig> query(Query query) throws ServiceException;

    AppConfig findById(Integer id) throws ServiceException;

    void update(Integer id, AppConfigForm form) throws ServiceException;

    void controlAd(Integer[] ids, int i) throws ServiceException;

}
