package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppMobileConfForm;
import com.yeecloud.adplus.dal.entity.AppMobileConf;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-15 09:45
 */
public interface AppMobileConfService {
    Page<AppMobileConf> query(Query query) throws ServiceException;

    AppMobileConf findById(Integer id) throws ServiceException;

    void create(AppMobileConfForm form) throws ServiceException;

    void update(Integer id, AppMobileConfForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
