package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppActivityAwardForm;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityForm;
import com.yeecloud.adplus.admin.controller.app.form.AppActivityTaskForm;
import com.yeecloud.adplus.dal.entity.AdPosition;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.AppActivityAward;
import com.yeecloud.adplus.dal.entity.AppActivityTask;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.util.List;

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

    /** 任务接口 */
    Page<AppActivityTask> queryTask(Query query) throws ServiceException;

    AppActivityTask findByTaskId(Integer id) throws ServiceException;

    void create(AppActivityTaskForm form) throws ServiceException;

    void update(Integer id, AppActivityTaskForm form) throws ServiceException;

    void deleteTask(Integer[] ids) throws ServiceException;

    /** 奖品接口 */
    Page<AppActivityAward> queryAward(Query query) throws ServiceException;

    AppActivityAward findByAwardId(Integer id) throws ServiceException;

    void create(AppActivityAwardForm form) throws ServiceException;

    void update(Integer id, AppActivityAwardForm form) throws ServiceException;

    void deleteAward(Integer[] ids) throws ServiceException;
}
