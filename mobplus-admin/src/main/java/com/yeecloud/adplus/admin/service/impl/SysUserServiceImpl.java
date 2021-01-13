package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.controller.sys.form.SysPasswdForm;
import com.yeecloud.adplus.admin.controller.sys.form.SysUserForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysUserVO;
import com.yeecloud.adplus.admin.convert.SysUserConvert;
import com.yeecloud.adplus.admin.entity.QSysUser;
import com.yeecloud.adplus.admin.entity.SysRole;
import com.yeecloud.adplus.admin.entity.SysUser;
import com.yeecloud.adplus.admin.repository.SysRoleRepository;
import com.yeecloud.adplus.admin.repository.SysUserRepository;
import com.yeecloud.adplus.admin.service.SysUserService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserConvert sysUserConvert;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Override
    public List<SysUserVO> findByRoleCode(String code) throws ServiceException {
        SysRole role = sysRoleRepository.findByCode(code);
        if (role != null && !role.isDeleted()) {
            List<SysUser> userList = role.getUsers().stream().filter(user -> user.getStatus() == Constants.STATE_ON && !user.isDeleted()).collect(Collectors.toList());
            return sysUserConvert.convert(userList);
        }
        return null;
    }

    @Override
    public SysUserVO findByUserName(String userName) throws ServiceException {
        try {
            SysUser entity = sysUserRepository.findTopByUserName(userName);
            if (entity != null && !entity.isDeleted()) {
                return transform(entity, false);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysUserVO findWithRolesByUserName(String userName) throws ServiceException {
        try {
            SysUser entity = sysUserRepository.findTopByUserName(userName);
            if (entity != null && !entity.isDeleted()) {
                return transform(entity, true);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageInfo<SysUserVO> query(Query query) throws ServiceException {
        try {
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize());
            QSysUser sysUser = QSysUser.sysUser;
            Predicate predicate = sysUser.deleted.eq(false);

            String keyword = query.get("keyword", String.class);
            if (StringUtils.isNotBlank(keyword)) {
                String express = "%".concat(keyword).concat("%");
                Predicate or = ExpressionUtils.or(sysUser.userName.like(express), sysUser.name.like(express));
                or = ExpressionUtils.or(or, sysUser.displayName.like(express));
                or = ExpressionUtils.or(or, sysUser.mobile.like(express));
                or = ExpressionUtils.or(or, sysUser.email.like(express));
                predicate = ExpressionUtils.and(predicate, or);
            }
            Page<SysUser> page = sysUserRepository.findAll(predicate, pagRequest);
            return transform(page, query.get("withRoles") != null && (boolean) query.get("withRoles") == true);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysUserVO findById(Integer id) throws ServiceException {
        try {
            SysUser entity = sysUserRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return sysUserConvert.convert(entity);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(SysUserForm form) throws ServiceException {
        try {
            SysUser entity = sysUserConvert.convert(form);
            entity.setSalt(StringUtils.genRandom(6, true));
            if (StringUtils.isBlank(entity.getName())) {
                entity.setName(entity.getDisplayName());
            }
            List<SysRole> roles = Lists.newArrayList();
            if (form.getRoles() != null) {
                for (Integer roleId : form.getRoles()) {
                    SysRole role = new SysRole();
                    role.setId(roleId);
                    roles.add(role);
                }
                entity.setRoles(roles);
                form.setRoles(null);
            }
            sysUserRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Integer id, SysUserForm form) throws ServiceException {
        try {
            SysUser entity = sysUserRepository.findById(id).orElse(null);
            if (entity != null) {
                List<SysRole> roles = Lists.newArrayList();
                if (form.getRoles() != null) {
                    for (Integer roleId : form.getRoles()) {
                        SysRole role = new SysRole();
                        role.setId(roleId);
                        roles.add(role);
                    }
                    entity.setRoles(roles);
                    form.setRoles(null);
                }
                NewBeanUtils.copyProperties(entity, form, true);
                if (StringUtils.isBlank(entity.getName())) {
                    entity.setName(entity.getDisplayName());
                }
                sysUserRepository.saveAndFlush(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer... ids) throws ServiceException {
        try {
            sysUserRepository.deleteById(ids);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updatePasswd(Integer id, SysPasswdForm form) throws ServiceException {
        try {
            SysUser entity = sysUserRepository.findById(id).orElse(null);
            if (entity != null) {
                if (StringUtils.isNotBlank(form.getOldPasswd()) && !StringUtils.equals(entity.getPasswd(), form.getOldPasswd())) {
                    throw new ServiceException("旧密码不正确!");
                }
                entity.setPasswd(form.getPasswd());
                sysUserRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    /* ============================================================================================================ */
    private PageInfo<SysUserVO> transform(Page<SysUser> result, boolean withRoles) {
        final List<SysUserVO> resultList = Lists.newArrayList();

        result.getContent().forEach(entity -> {
            SysUserVO vo = transform(entity, withRoles);
            resultList.add(vo);
        });

        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private SysUserVO transform(SysUser user, boolean withRoles) {
        SysUserVO vo = sysUserConvert.convert(user);
        if (withRoles) {
            user.getRoles().forEach(role -> {
                vo.getRoles().add(role.getId());
                vo.getRoleCodes().add(role.getCode());
            });
        }
        return vo;
    }
}
