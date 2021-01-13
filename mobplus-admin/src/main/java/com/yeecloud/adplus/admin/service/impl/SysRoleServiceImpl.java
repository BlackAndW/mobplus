package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.sys.form.SysRoleForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysRoleVO;
import com.yeecloud.adplus.admin.convert.SysRoleConvert;
import com.yeecloud.adplus.admin.entity.QSysRole;
import com.yeecloud.adplus.admin.entity.SysPermission;
import com.yeecloud.adplus.admin.entity.SysRole;
import com.yeecloud.adplus.admin.repository.SysRoleRepository;
import com.yeecloud.adplus.admin.service.SysRoleService;
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
import java.util.Set;

/**
 * 系统角色
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleConvert sysRoleConvert;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysPermissionServiceImpl sysPermissionService;

    @Override
    public List<String> findCodesByUserId(Integer userId) throws ServiceException {
        try {
            return sysRoleRepository.findCodesByUserId(userId);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageInfo<SysRoleVO> query(Query query) throws ServiceException {
        try {
            QSysRole sysRole = QSysRole.sysRole;
            Predicate predicate = sysRole.deleted.eq(false);

            if (query.get("keyword") != null) {
                String keyword = (String) query.get("keyword");
                String express = "%".concat(keyword).concat("%");
                Predicate or = ExpressionUtils.or(sysRole.code.like(express), sysRole.name.like(express));
                predicate = ExpressionUtils.and(predicate, or);
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "sort"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<SysRole> page = sysRoleRepository.findAll(predicate, pagRequest);
            return transform(page);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysRoleVO findById(Integer id) throws ServiceException {
        try {
            SysRole entity = sysRoleRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return sysRoleConvert.convert(entity);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(SysRoleForm form) throws ServiceException {
        try {
            SysRole entity = sysRoleConvert.convert(form);
            sysRoleRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Integer id, SysRoleForm form) throws ServiceException {
        try {
            SysRole entity = sysRoleRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                sysRoleRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer... ids) throws ServiceException {
        try {
            sysRoleRepository.deleteById(ids);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    /* ============================================================================================================ */

    @Override
    public List<GrantPermVO> findGrantPerms(Integer id) throws ServiceException {
        SysRole role = sysRoleRepository.findById(id).orElse(null);
        if (role != null) {
            Set<Integer> rolePerms = Sets.newHashSet();
            role.getPermissions().stream().forEach(perm -> {
                rolePerms.add(perm.getId());
            });
            List<GrantPermVO> list = sysPermissionService.findPermissionsWithGroups();
            list.stream().forEach(perm -> {
                perm.getActionsOptions().stream().forEach(act -> {
                    if (rolePerms.contains(act.getValue())) {
                        perm.getSelected().add(act.getValue());
                    }
                });
                if (perm.getSelected().size() == perm.getActionsOptions().size()) {
                    perm.setCheckedAll(true);
                } else if (perm.getSelected().size() == 0) {
                    perm.setIndeterminate(false);
                } else {
                    perm.setIndeterminate(true);
                }
            });
            return list;
        }
        return Lists.newArrayList();
    }

    @Override
    @Transactional
    public void grantPermissions(Integer id, List<Integer> permissions) throws ServiceException {
        SysRole role = sysRoleRepository.findById(id).orElse(null);
        if (role != null) {
            role.getPermissions().clear();
            permissions.stream().forEach(perm -> {
                SysPermission entity = new SysPermission();
                entity.setId(perm);
                role.getPermissions().add(entity);
            });
            sysRoleRepository.saveAndFlush(role);
        }
    }

    /* ============================================================================================================ */

    private PageInfo<SysRoleVO> transform(Page<SysRole> result) {
        List<SysRoleVO> resultList = sysRoleConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    @Override
    public SysRole findByCode(String code) throws ServiceException {
        return sysRoleRepository.findByCode(code);
    }
}
