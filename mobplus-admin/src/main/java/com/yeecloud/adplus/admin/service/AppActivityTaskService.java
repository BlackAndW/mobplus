package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.AppActivityTask;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */
public interface AppActivityTaskService {

    Page<AppActivityTask> query(Query query) throws ServiceException;

    AppActivityTask findById(Integer id) throws ServiceException;

    void create(AppActivityTask form) throws ServiceException;

    void update(Integer id, AppActivityTask form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
