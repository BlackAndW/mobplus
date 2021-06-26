package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.dal.entity.AdAccount;
import com.yeecloud.meeto.common.exception.ServiceException;
import org.springframework.data.domain.Page;
import com.yeecloud.meeto.common.util.Query;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */
public interface BaseService<T> {

    Page<T> query(Query query) throws ServiceException;

    T findById(Integer id) throws ServiceException;

    void create(T form) throws ServiceException;

    void update(Integer id, T form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;

}
