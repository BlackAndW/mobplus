package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Maps;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.controller.sys.form.SysConfForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysConfVO;
import com.yeecloud.adplus.admin.convert.SysConfConvert;
import com.yeecloud.adplus.admin.entity.QSysConf;
import com.yeecloud.adplus.admin.entity.SysConf;
import com.yeecloud.adplus.admin.repository.SysConfRepository;
import com.yeecloud.adplus.admin.service.SysConfService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@Service
public class SysConfServiceImpl implements SysConfService {

    @Autowired
    private SysConfConvert sysConfConvert;

    @Autowired
    private SysConfRepository sysConfRepository;

    private Map<String, String> configureCache;

    public SysConfServiceImpl() {
        configureCache = Maps.newHashMap();
    }

    @PostConstruct
    public void init() {
        this.refreshAsync();
    }

    @Override
    public PageInfo<SysConfVO> query(Query query) throws ServiceException {
        try {
            QSysConf sysConf = QSysConf.sysConf;
            Predicate predicate = ExpressionUtils.and(sysConf.deleted.eq(false), sysConf.perm.goe(1));
            if (query.get("category") != null) {
                predicate = ExpressionUtils.and(predicate, sysConf.category.eq(Integer.parseInt((String) query.get("category"))));
            }
            if (query.get("keyword") != null) {
                String keyword = (String) query.get("keyword");
                String express = "%".concat(keyword).concat("%");
                Predicate or = ExpressionUtils.or(sysConf.name.like(express), sysConf.key.like(express));
                predicate = ExpressionUtils.and(predicate, or);
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "sort"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<SysConf> page = sysConfRepository.findAll(predicate, pagRequest);
            return transform(page);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysConfVO findById(Integer id) throws ServiceException {
        try {
            SysConf entity = sysConfRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return sysConfConvert.convert(entity);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(SysConfForm form) throws ServiceException {
        try {
            SysConf entity = sysConfConvert.convert(form);
            sysConfRepository.save(entity);

            this.refresh();
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Integer id, SysConfForm form) throws ServiceException {
        try {
            SysConf entity = sysConfRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                sysConfRepository.save(entity);

                this.refresh();
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer... ids) throws ServiceException {
        try {
            sysConfRepository.deleteById(ids);
            this.refresh();
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    /* ============================================================================================================ */
    private PageInfo<SysConfVO> transform(Page<SysConf> result) {
        List<SysConfVO> resultList = sysConfConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }


    /*========================================*/

    private void refresh() {
//        ((SysConfService) AopContext.currentProxy()).refreshAsync();
        refreshAsync();
    }

    @Async
    @Override
    public void refreshAsync() {
        Map<String, String> tmpCache = new HashMap<>();

        QSysConf sysConf = QSysConf.sysConf;
        Predicate predicate = ExpressionUtils.and(sysConf.deleted.eq(false), sysConf.status.goe(Constants.STATE_ON));
        this.sysConfRepository.findAll(predicate);
        List<SysConf> list = this.sysConfRepository.findAll();
        for (SysConf obj : list) {
            tmpCache.put(obj.getKey(), obj.getValue());
        }
        this.configureCache = tmpCache;
//        log.debug("configuration refreshed!");
    }

    @Override
    public <T> T getValueByKey(String key, Class<T> clazz) {
        String value = configureCache.get(key);
        if (value != null) {
            try {
                if (clazz == String.class) {
                    return (T) value;
                }
                return (T) ConvertUtils.convert(value, clazz);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public <T> T getValueByKey(String key, T defValue) {
        String value = configureCache.get(key);
        if (value != null) {
            try {
                Class clazz = defValue.getClass();
                if (clazz == String.class) {
                    return (T) value;
                }
                return (T) ConvertUtils.convert(value, clazz);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        return defValue;
    }
}
