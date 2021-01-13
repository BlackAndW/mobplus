package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.controller.sys.form.SysPermissionForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysCategoryVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysPermissionVO;
import com.yeecloud.adplus.admin.convert.SysPermissionConvert;
import com.yeecloud.adplus.admin.entity.QSysPermission;
import com.yeecloud.adplus.admin.entity.SysPermission;
import com.yeecloud.adplus.admin.repository.SysPermissionRepository;
import com.yeecloud.adplus.admin.service.SysCategoryService;
import com.yeecloud.adplus.admin.service.SysPermissionService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionConvert sysPermissionConvert;

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Autowired
    private SysCategoryService sysCategoryService;

    @Override
    public List<String> findCodesByUserId(Integer userId) throws ServiceException {
        try {
            return sysPermissionRepository.findCodesByUserId(userId);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Integer> findIdsByUserId(Integer userId) throws ServiceException {
        try {
            return sysPermissionRepository.findIdsByUserId(userId);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageInfo<SysPermissionVO> query(Query query) throws ServiceException {
        try {
            QSysPermission sysPerm = QSysPermission.sysPermission;
            Predicate predicate = sysPerm.deleted.eq(false);
            if (query.get("category") != null) {
                predicate = ExpressionUtils.and(predicate, sysPerm.category.eq(Integer.parseInt((String) query.get("category"))));
            }
            if (query.get("keyword") != null) {
                String keyword = (String) query.get("keyword");
                String express = "%".concat(keyword).concat("%");
                Predicate or = ExpressionUtils.or(sysPerm.code.like(express), sysPerm.name.like(express));
                predicate = ExpressionUtils.and(predicate, or);
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "sort"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<SysPermission> page = sysPermissionRepository.findAll(predicate, pagRequest);
            return transform(page);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysPermissionVO findById(Integer id) throws ServiceException {
        try {
            SysPermission entity = sysPermissionRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return sysPermissionConvert.convert(entity);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(SysPermissionForm form) throws ServiceException {
        try {
            SysPermission entity = sysPermissionConvert.convert(form);
            sysPermissionRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Integer id, SysPermissionForm form) throws ServiceException {
        try {
            SysPermission entity = sysPermissionRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                sysPermissionRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer... ids) throws ServiceException {
        try {
            sysPermissionRepository.deleteById(ids);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<GrantPermVO> findPermissionsWithGroups() throws ServiceException {
        List<GrantPermVO> resultList = Lists.newArrayList();
        List<SysCategoryVO> categories = sysCategoryService.findByParentId(Constants.CATEGORY_PERMISSION);
        categories.stream().forEach(category -> {
            GrantPermVO permGrp = new GrantPermVO();

            QSysPermission sysPerm = QSysPermission.sysPermission;
            Predicate predicate = ExpressionUtils.and(sysPerm.deleted.eq(false), sysPerm.category.eq(category.getId()));
            Iterable<SysPermission> iterable = sysPermissionRepository.findAll(predicate, Sort.by(new Sort.Order(Sort.Direction.ASC, "sort")));
            iterable.forEach(perm -> {
                permGrp.getActionsOptions().add(new GrantPermVO.Action(perm.getName(), perm.getId()));
            });
            permGrp.setName(category.getName());
            resultList.add(permGrp);
        });
        return resultList;
    }

    /* ============================================================================================================ */
    private PageInfo<SysPermissionVO> transform(Page<SysPermission> result) {
        List<SysPermissionVO> resultList = sysPermissionConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
