package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppForm;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-02 10:08
 */
public interface AppService {
    Page<App> query(Query query) throws ServiceException;

    App findById(Integer id) throws ServiceException;

    void create(AppForm form) throws ServiceException;

    void update(Integer id, AppForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
