package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.gateway.controller.form.GameForm;
import com.yeecloud.meeto.common.exception.ServiceException;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-11-30 11:11
 */
public interface GameService {

    List<Game> findGameList(int type, int items) throws ServiceException;

    List<Game> findGameListNew() throws ServiceException;

    void updateLogById(GameForm game) throws ServiceException;
}
