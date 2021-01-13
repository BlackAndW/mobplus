package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppAdvertiserForm;
import com.yeecloud.adplus.dal.entity.AppAdvertiser;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-09 11:10
 */

public interface AppAdvertiserService {
    Page<AppAdvertiser> query(Query query) throws ServiceException;

    AppAdvertiser findById(Integer id) throws ServiceException;

    void create(AppAdvertiserForm form) throws ServiceException;

    void update(Integer id, AppAdvertiserForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
