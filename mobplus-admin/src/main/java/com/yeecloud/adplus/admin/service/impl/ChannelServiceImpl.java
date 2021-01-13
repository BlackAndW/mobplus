package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.release.form.ChannelForm;
import com.yeecloud.adplus.admin.service.ChannelService;
import com.yeecloud.adplus.dal.entity.AppProject;
import com.yeecloud.adplus.dal.entity.Channel;
import com.yeecloud.adplus.dal.entity.QChannel;
import com.yeecloud.adplus.dal.repository.AppProjectRepository;
import com.yeecloud.adplus.dal.repository.ChannelRepository;
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

import java.util.List;


/**
 * @author: Huang
 * @create: 2020-12-01 14:27
 */
@Slf4j
@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private AppProjectRepository appProjectRepository;

    @Override
    public Page<Channel> query(Query query) throws ServiceException {
        try {
            QChannel channel = QChannel.channel;
            Predicate predicate = channel.deleted.eq(false);
            String name = query.get("name", String.class);
            if (StringUtils.isNotBlank(name)) {
                String express = "%".concat(name).concat("%");
                predicate = ExpressionUtils.and(predicate, channel.name.like(express));
            }
            String code = query.get("code", String.class);
            if (StringUtils.isNotBlank(code)) {
                String express = "%".concat(code).concat("%");
                predicate = ExpressionUtils.and(predicate, channel.name.like(express));
            }
            Integer type = query.get("type", Integer.class);
            if (type != null && type > 0) {
                predicate = ExpressionUtils.and(predicate, channel.type.eq(type));
            }
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, channel.status.eq(status));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return channelRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Channel findById(Integer id) throws ServiceException {
        try {
            Channel entity = channelRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return entity;
            } else {
                return null;
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(ChannelForm form) throws ServiceException {
        try {
            Channel entity = new Channel();
            NewBeanUtils.copyProperties(entity, form, true);
            channelRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, ChannelForm form) throws ServiceException {
        try {
            Channel entity = channelRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                channelRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException();
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {

        // 刪除渠道时，判断是否该渠道下存在项目
        for (int id : ids) {
            Channel channel = channelRepository.findById(id).orElse(null);
            List<AppProject> appProjectList = appProjectRepository.findAllByChannelAndDeleted(channel, false);
            if (appProjectList.isEmpty()) {
                channelRepository.deleteById(id);
            } else {
                throw new ServiceException("当前删除的渠道中存在还在使用的渠道，请修改后再进行操作");
            }
        }

    }
}
