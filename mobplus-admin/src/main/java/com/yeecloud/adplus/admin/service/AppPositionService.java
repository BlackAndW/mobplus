package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppPositionAdPositionForm;
import com.yeecloud.adplus.dal.entity.AppPosition;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-08 17:21
 */
public interface AppPositionService {
    Page<AppPosition> query(Query query) throws ServiceException;

    AppPosition findById(Integer id) throws ServiceException;

    void create(AppPosition form) throws ServiceException;

    void update(Integer id, AppPosition form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;

    /*==================================================*/
    void saveAppPositionAdPositionLink(Integer appPosId, List<AppPositionAdPositionForm> list) throws ServiceException;
}
