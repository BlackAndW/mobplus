package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.dal.entity.GameLog;
import com.yeecloud.adplus.dal.entity.QGame;
import com.yeecloud.adplus.dal.entity.QGameLog;
import com.yeecloud.adplus.dal.repository.GameLogRepository;
import com.yeecloud.adplus.dal.repository.GameRepository;
import com.yeecloud.adplus.gateway.controller.form.GameForm;
import com.yeecloud.adplus.gateway.service.GameService;
import com.yeecloud.adplus.gateway.util.EmptyUtil;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.DateUtils;
import com.yeecloud.meeto.common.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    private GameLogRepository gameLogRepository;

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
    public void updateLogById(GameForm form) throws ServiceException {
        if (form.getId() != null && form.getId() > 0) {
            Game game = gameRepository.findById(form.getId()).orElse(null);
            if (EmptyUtil.isEmpty(game)) {
                log.error("there is no game about this id:{}", form.getId());
                return;
            }

            QGameLog QgameLog = QGameLog.gameLog;
            Predicate predicate = QgameLog.deleted.eq(false);
            predicate = ExpressionUtils.and(predicate, QgameLog.game.id.eq(form.getId()));
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(0, 1, sort);
            Page<GameLog> gameLogPage = gameLogRepository.findAll(predicate, pagRequest);
            // 不存在对应game_id的log，新增
            if (gameLogPage.getContent().size() == 0) {
                log.info("create new game log, game id is {}", form.getId());
                GameLog newRecord = new GameLog();
                updateLog(newRecord, form, game);
                gameLogRepository.save(newRecord);
                return;
            }

            GameLog gameLog = gameLogPage.getContent().get(0);
            DateTime today = new DateTime();
            today = today.withMillisOfDay(0);
            DateTime gameCreatedDay = new DateTime(gameLog.getCreatedAt()).withMillisOfDay(0);
            Period period = new Period(gameCreatedDay, today, PeriodType.days());
            // 相隔一天以上的数据，新增
            if (period.getDays() >= 1) {
                GameLog newRecord = new GameLog();
                updateLog(newRecord, form, game);
                gameLogRepository.save(newRecord);
                return;
            }

            updateLog(gameLog, form, game);
            gameLogRepository.save(gameLog);
        }
    }

    private void updateLog(GameLog gameLog, GameForm form, Game game) {
        gameLog.setGame(game);
        if (form.getClickNum() != null && form.getClickNum() > 0) {
            gameLog.setClickNum(gameLog.getClickNum() + 1);
        }
        if (form.getIpNum() != null && form.getIpNum() > 0) {
            gameLog.setIpNum(gameLog.getIpNum() + 1);
        }
        if (form.getPlayTime() != null && form.getPlayTime() > 0) {
            gameLog.setPlayTime(gameLog.getPlayTime() + form.getPlayTime());
        }
    }
}
