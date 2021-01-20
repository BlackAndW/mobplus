package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppActivityForm;
import com.yeecloud.adplus.dal.entity.AdPosition;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
public interface AppActivityService {

    Page<AppActivity> query(Query query) throws ServiceException;

    AppActivity findById(Integer id) throws ServiceException;

    void create(AppActivityForm form) throws ServiceException;

    void update(Integer id, AppActivityForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
