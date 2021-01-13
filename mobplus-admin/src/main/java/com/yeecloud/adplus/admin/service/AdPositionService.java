package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.dal.entity.AdPosition;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-08 16:37
 */
public interface AdPositionService {
    Page<AdPosition> query(Query query) throws ServiceException;

    AdPosition findById(Integer id) throws ServiceException;

    void create(AdPosition form) throws ServiceException;

    void update(Integer id, AdPosition form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
