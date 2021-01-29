package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppFunctionForm;
import com.yeecloud.adplus.dal.entity.AppFunction;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */
public interface AppFunctionService {

    Page<AppFunction> query(Query query) throws ServiceException;

    AppFunction findById(Integer id) throws ServiceException;

    void create(AppFunctionForm form) throws ServiceException;

    void update(Integer id, AppFunctionForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
