package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppProjectForm;
import com.yeecloud.adplus.dal.entity.AppProject;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-02 15:59
 */
public interface AppProjectService {
    Page<AppProject> query(Query query) throws ServiceException;

    AppProject findById(Integer id) throws ServiceException;

    void create(AppProjectForm form) throws ServiceException;

    void update(Integer id, AppProjectForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
