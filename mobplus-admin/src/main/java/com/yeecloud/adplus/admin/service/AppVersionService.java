package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppVersionForm;
import com.yeecloud.adplus.dal.entity.AppVersion;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-02 14:42
 */
public interface AppVersionService {

    Page<AppVersion> query(Query query) throws ServiceException;

    AppVersion findById(Integer id) throws ServiceException;

    void create(AppVersionForm form) throws ServiceException;

    void update(Integer id, AppVersionForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
