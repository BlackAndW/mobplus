package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.release.form.ChannelForm;
import com.yeecloud.adplus.dal.entity.Channel;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * @author: Huang
 * @create: 2020-12-01 14:27
 */
public interface ChannelService {
    Page<Channel> query(Query query) throws ServiceException;

    Channel findById(Integer id) throws ServiceException;

    void create(ChannelForm form) throws ServiceException;

    void update(Integer id, ChannelForm form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
