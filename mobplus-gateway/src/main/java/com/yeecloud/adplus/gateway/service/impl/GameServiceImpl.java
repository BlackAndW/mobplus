package com.yeecloud.adplus.gateway.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.dal.entity.QGame;
import com.yeecloud.adplus.dal.repository.GameRepository;
import com.yeecloud.adplus.gateway.service.GameService;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-11-30 11:12
 */
@Slf4j
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public List<Game> findGameList(int type, int items) throws ServiceException {
        try {
            QGame game = QGame.game;
            Predicate predicate = ExpressionUtils.and(game.deleted.eq(false), game.status.ne(1));
            predicate = ExpressionUtils.and(predicate, game.status.ne(0));
            predicate = ExpressionUtils.and(predicate, game.type.eq(type));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(0, items, sort);
            return gameRepository.findAll(predicate, pagRequest).getContent();
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }
}
