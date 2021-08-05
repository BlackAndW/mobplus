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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Throwable.class)
    public List<Game> findGameList(int type, int size) throws ServiceException {
        try {
            QGame game = QGame.game;
            Predicate predicate = ExpressionUtils.and(game.deleted.eq(false), game.status.ne(1));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "type"), new Sort.Order(Sort.Direction.ASC, "name"));
            PageRequest pagRequest = PageRequest.of(0, size, sort);
            return gameRepository.findAll(predicate, pagRequest).getContent();
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Game> findGameListNew() throws ServiceException {
        return findGameList(-1,999);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateNumByName(Game form) throws ServiceException {
        if (form.getClickNum() != null && form.getClickNum() > 0) {
            QGame Qgame = QGame.game;
            Predicate predicate = ExpressionUtils.and(Qgame.deleted.eq(false), Qgame.status.ne(1));
            predicate = ExpressionUtils.and(predicate, Qgame.name.eq(form.getName()));
            Game game = gameRepository.findOne(predicate).orElse(null);
            game.setClickNum(game.getClickNum() + 1);
            gameRepository.save(game);
        }
    }
}
