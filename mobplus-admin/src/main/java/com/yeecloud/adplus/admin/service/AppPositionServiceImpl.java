package com.yeecloud.adplus.admin.service;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.app.convert.AppPosAdPosConvert;
import com.yeecloud.adplus.admin.controller.app.form.AppPositionAdPositionForm;
import com.yeecloud.adplus.admin.controller.app.vo.AppPositionAdPositionVO;
import com.yeecloud.adplus.dal.entity.AppPosition;
import com.yeecloud.adplus.dal.entity.AppPositionAdPosition;
import com.yeecloud.adplus.dal.entity.QAppPosition;
import com.yeecloud.adplus.dal.repository.AppPositionAdPositionRepository;
import com.yeecloud.adplus.dal.repository.AppPositionRepository;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Huang
 * @create: 2020-12-08 17:24
 */
@Slf4j
@Service
public class AppPositionServiceImpl implements AppPositionService {

    @Autowired
    private AppPositionRepository appPositionRepository;

    @Autowired
    private AppPositionAdPositionRepository appPositionAdPositionRepository;


    @Autowired
    private AppPosAdPosConvert appPosAdPosConvert;


    @Override
    public Page<AppPosition> query(Query query) throws ServiceException {
        try {
            QAppPosition appPosition = QAppPosition.appPosition;
            Predicate predicate = appPosition.deleted.eq(false);

            Integer appId = query.get("appId", Integer.class);
            if (appId != null && appId > 0) {
                predicate = ExpressionUtils.and(predicate, appPosition.app.id.eq(appId));
            }
//            Integer type = query.get("type", Integer.class);
//            if (type != null && type > 0) {
//                predicate = ExpressionUtils.and(predicate, appPosition.type.id.eq(type));
//            }
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, appPosition.status.eq(status));
            }
            String code = query.get("code", String.class);
            if (StringUtils.isNotBlank(code)) {
                predicate = ExpressionUtils.and(predicate, appPosition.code.like("%" + code + "%"));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "code"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return appPositionRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AppPosition findById(Integer id) throws ServiceException {
        try {
            AppPosition entity = appPositionRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return entity;
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(AppPosition form) throws ServiceException {
        try {
//            // 判断广告类型是否为插屏视频
//            if (form.getType().getId().equals(4)){
//                form.setParams("{\"style\":3}");
//            }
//            // 判断广告类型是否为插屏
//            if (form.getType().getId().equals(3)){
//                form.setParams("{\"style\":1}");
//            }
            appPositionRepository.save(form);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Integer id, AppPosition form) throws ServiceException {
        try {
            AppPosition entity = appPositionRepository.findById(id).orElse(null);
//            // 判断广告类型是否为插屏视频
//            if (form.getType().getId().equals(4)){
//                form.setParams("{\"style\":3}");
//            }
//            // 判断广告类型是否为插屏
//            if (form.getType().getId().equals(3)){
//                form.setParams("{\"style\":1}");
//            }
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                appPositionRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer[] ids) throws ServiceException {
        appPositionRepository.deleteById(ids);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveAppPositionAdPositionLink(Integer appPosId, List<AppPositionAdPositionForm> list) throws ServiceException {
        AppPosition appPosition = this.findById(appPosId);
        if (appPosition == null) {
            return;
        }
        appPositionAdPositionRepository.deleteByAppPosition(appPosition);
        List<AppPositionAdPosition> adPosList = Lists.newArrayList();

        list.forEach(form -> {
            AppPositionAdPosition pos = appPosAdPosConvert.convert(form);
            pos.setAppPosition(appPosition);
            adPosList.add(pos);
        });
        appPositionAdPositionRepository.saveAll(adPosList);
    }
}
