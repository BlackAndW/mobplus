package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.dal.entity.Advertiser;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-08 15:39
 */
public interface AdvertiserService {
    Page<Advertiser> query(Query query) throws ServiceException;

    Advertiser findById(Integer id) throws ServiceException;

    Advertiser findByCode(String code) throws ServiceException;

    void create(Advertiser form) throws ServiceException;

    void update(Integer id, Advertiser form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
