package com.yeecloud.adplus.gateway.service;

import com.querydsl.core.Tuple;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.meeto.common.exception.ServiceException;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-11-30 11:11
 */
public interface GameService {

    List<Tuple> findGameList(int type, int items) throws ServiceException;

    List<Tuple> findGameListNew() throws ServiceException;

    void updateNumByName(Game game) throws ServiceException;
}
