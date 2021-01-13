package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.sys.form.SysCategoryForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysCategoryVO;
import com.yeecloud.adplus.admin.convert.SysCategoryConvert;
import com.yeecloud.adplus.admin.entity.QSysCategory;
import com.yeecloud.adplus.admin.entity.SysCategory;
import com.yeecloud.adplus.admin.repository.SysCategoryRepository;
import com.yeecloud.adplus.admin.service.SysCategoryService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据分类
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@Service
public class SysCategoryServiceImpl implements SysCategoryService {

    @Autowired
    private SysCategoryConvert sysCategoryConvert;

    @Autowired
    private SysCategoryRepository sysCategoryRepository;

    @Override
    public PageInfo<SysCategoryVO> query(Query query) throws ServiceException {
        try {
            QSysCategory sysCategory = QSysCategory.sysCategory;
            Predicate predicate = sysCategory.deleted.eq(false);

            if (query.get("keyword") != null) {
                String name = (String) query.get("keyword");
                String express = "%".concat(name).concat("%");
                Predicate or = ExpressionUtils.or(sysCategory.name.like(express), sysCategory.nameEn.like(express));
                predicate = ExpressionUtils.and(predicate, or);
            }
            if (query.get("parentId") != null) {
                Integer parentId = (Integer) query.get("parentId");
                predicate = ExpressionUtils.and(predicate, sysCategory.parentId.eq(parentId));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "sort"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<SysCategory> page = sysCategoryRepository.findAll(predicate, pagRequest);
            return transform(page, query.get("recursion") != null && (boolean) query.get("recursion") == true);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysCategoryVO findById(Integer id) throws ServiceException {
        try {
            SysCategory entity = sysCategoryRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return sysCategoryConvert.convert(entity);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public void create(Integer parentId, SysCategoryForm form) throws ServiceException {
        try {
            if (parentId != null && parentId > 0) {
                SysCategory parent = sysCategoryRepository.findById(parentId).orElse(null);
                if (parent != null && !parent.isDeleted()) {
                    parentId = parent.getId();
                } else {
                    //parentId不存在或己删除
                    parentId = 0;
                }
            }
            SysCategory entity = sysCategoryConvert.convert(form);
            entity.setParentId(parentId == null ? 0 : parentId);
            sysCategoryRepository.save(entity);
            return;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Integer id, SysCategoryForm form) throws ServiceException {
        try {
            SysCategory entity = sysCategoryRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                sysCategoryRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer... ids) throws ServiceException {
        try {
            sysCategoryRepository.deleteById(ids);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SysCategoryVO> findByParentId(Integer parentId) throws ServiceException {
        QSysCategory sysCategory = QSysCategory.sysCategory;
        Predicate predicate = sysCategory.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, sysCategory.parentId.eq(parentId));

        List<SysCategoryVO> resultList = Lists.newArrayList();
        Iterable<SysCategory> list = sysCategoryRepository.findAll(predicate, Sort.by(new Sort.Order(Sort.Direction.ASC, "sort")));
        list.forEach(entity -> {
            if (!entity.isDeleted()) {
                SysCategoryVO vo = sysCategoryConvert.convert(entity);
                resultList.add(vo);
            }
        });
        return resultList;
    }

    /* ============================================================================================================ */
    private PageInfo<SysCategoryVO> transform(Page<SysCategory> result, boolean recursion) {
        if (recursion) {
            List<SysCategoryVO> resultList = convert(result.getContent());
            return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
        }
        List<SysCategoryVO> resultList = sysCategoryConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private List<SysCategoryVO> convert(List<SysCategory> list) {
        List<SysCategoryVO> resultList = Lists.newArrayList();
        for (SysCategory entity : list) {
            if (entity.isDeleted()) {
                continue;
            }
            List<SysCategoryVO> subList = convert(entity.getChildren());
            SysCategoryVO vo = sysCategoryConvert.convert(entity);
            if (!subList.isEmpty()) {
                vo.setChildren(subList);
            } else {
                vo.setChildren(null);
            }
            resultList.add(vo);
        }
        return resultList;
    }
}
