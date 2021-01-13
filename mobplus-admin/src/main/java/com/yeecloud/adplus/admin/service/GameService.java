package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

/**
 * Title
 *
 * Date: 2020-06-19 15:46:44
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface GameService {

    Page<Game> query(Query query) throws ServiceException;

    Game findById(Integer id) throws ServiceException;

    void create(Game form) throws ServiceException;

    void update(Integer id, Game form) throws ServiceException;

    void delete(Integer[] ids) throws ServiceException;
}
