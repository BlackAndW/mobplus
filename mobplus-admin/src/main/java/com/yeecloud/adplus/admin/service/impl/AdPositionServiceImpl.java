package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.service.AdPositionService;
import com.yeecloud.adplus.dal.entity.AdPosition;
import com.yeecloud.adplus.dal.entity.QAdPosition;
import com.yeecloud.adplus.dal.repository.AdPositionRepository;
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
 * @author: Huang
 * @create: 2020-12-08 16:38
 */
@Service
@Slf4j
public class AdPositionServiceImpl implements AdPositionService {

    @Autowired
    private AdPositionRepository adPositionRepository;

    @Override
    public Page<AdPosition> query(Query query) throws ServiceException {
        try {
            QAdPosition adPosition = QAdPosition.adPosition;
            Predicate predicate = adPosition.deleted.eq(false);

            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, adPosition.app.id.eq(appId));
            }
            Integer advId = query.get("advId", Integer.class);
            if (advId != null && advId > 0) {
                predicate = ExpressionUtils.and(predicate, adPosition.advertiser.id.eq(advId));
            }
//            Integer type = query.get("type", Integer.class);
//            if (type != null && type > 0) {
//                predicate = ExpressionUtils.and(predicate, adPosition.type.id.eq(type));
//            }
            String code = query.get("code", String.class);
            if (StringUtils.isNotBlank(code)) {
                predicate = ExpressionUtils.and(predicate, adPosition.code.like(code + "%"));
            }
            String name = query.get("name", String.class);
            if (StringUtils.isNotBlank(name)) {
                predicate = ExpressionUtils.and(predicate, adPosition.name.like("%" + name + "%"));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return adPositionRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AdPosition findById(Integer id) throws ServiceException {
        try {
            AdPosition entity = adPositionRepository.findById(id).orElse(null);
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
    public void create(AdPosition form) throws ServiceException {
        try {
            adPositionRepository.save(form);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AdPosition form) throws ServiceException {
        try {
            AdPosition entity = adPositionRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                adPositionRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        adPositionRepository.deleteById(ids);
    }
}
