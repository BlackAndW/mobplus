package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.service.GameService;
import com.yeecloud.adplus.dal.entity.Game;
import com.yeecloud.adplus.dal.entity.QGame;
import com.yeecloud.adplus.dal.repository.GameRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Title
 *
 * Date: 2020-06-19 15:56:25
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;

    @Override
    public Page<Game> query(Query query) throws ServiceException {
        try {
            QGame game = QGame.game;
            Predicate predicate = game.deleted.eq(false);

            Integer type = query.get("type", Integer.class);
            if (type != null && type > 0) {
                predicate = ExpressionUtils.and(predicate, game.type.eq(type));
            }
            String keyword = (String) query.get("keyword");
            if (StringUtils.isNotBlank(keyword)) {
                String express = keyword.concat("%");
                Predicate or = ExpressionUtils.or(game.name.like(express), game.desc.like(express));
                predicate = ExpressionUtils.and(predicate, or);
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return gameRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Game findById(Integer id) throws ServiceException {
        try {
            Game entity = gameRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return entity;
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(Game form) throws ServiceException {
        try {
            gameRepository.save(form);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, Game form) throws ServiceException {
        try {
            Game entity = gameRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                gameRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        gameRepository.deleteById(ids);
    }
}
