package com.yeecloud.adplus.gateway.service.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.dal.entity.QGame;
import com.yeecloud.adplus.dal.repository.GameRepository;
import com.yeecloud.adplus.gateway.service.GameService;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<Tuple> findGameList(int type, int size) throws ServiceException {
        try {
            QGame game = QGame.game;
            Predicate predicate = ExpressionUtils.and(game.deleted.eq(false), game.status.ne(1));
            return jpaQueryFactory.
                    selectDistinct(game.name, game.type).
                    from(game).
                    where(predicate).
                    orderBy(game.type.asc(), game.name.asc()).
                    fetch();
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Tuple> findGameListNew() throws ServiceException {
        return findGameList(-1,999);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateNumByName(Game form) throws ServiceException {
        if (form.getClickNum() != null && form.getClickNum() > 0) {
            QGame Qgame = QGame.game;
            Predicate predicate = ExpressionUtils.and(Qgame.deleted.eq(false), Qgame.status.ne(1));
            predicate = ExpressionUtils.and(predicate, Qgame.name.eq(form.getName()));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(0, 1, sort);
            Game game = gameRepository.findAll(predicate, pagRequest).getContent().get(0);

            DateTime today = new DateTime();
            today = today.withMillisOfDay(0);
            DateTime gameCreatedTime = new DateTime(game.getCreatedAt());
            Period period = new Period(gameCreatedTime, today, PeriodType.days());
            if (period.getDays() >= 1) {
                Game newRecord = new Game();
                BeanUtils.copyProperties(game, newRecord, "id", "clickNum", "createdAt");
                newRecord.setClickNum(newRecord.getClickNum() + 1);
                gameRepository.save(newRecord);
                return;
            }
            game.setClickNum(game.getClickNum() + 1);
            gameRepository.save(game);
        }
    }
}
