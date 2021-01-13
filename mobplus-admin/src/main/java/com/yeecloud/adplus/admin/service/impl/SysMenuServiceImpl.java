package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.controller.sys.form.SysMenuForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysMenuTreeVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysMenuVO;
import com.yeecloud.adplus.admin.convert.SysMenuConvert;
import com.yeecloud.adplus.admin.entity.QSysMenu;
import com.yeecloud.adplus.admin.entity.SysMenu;
import com.yeecloud.adplus.admin.entity.SysPermission;
import com.yeecloud.adplus.admin.repository.SysMenuRepository;
import com.yeecloud.adplus.admin.service.SysMenuService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 菜单信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuConvert sysMenuConvert;

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    @Transactional(readOnly = true)
    public List<SysMenuTreeVO> findMenusByUserId(Integer userId) throws ServiceException {
        List<Integer> permissionList = sysPermissionService.findIdsByUserId(userId);
        return findSubMenus(permissionList);
    }

    @Override
    public PageInfo<SysMenuVO> query(Query query) throws ServiceException {
        try {
            if (query.get("keyword") == null) {
                List<SysMenuVO> resultList = findSubMenus();
                return new PageInfo<>(1, resultList.size(), resultList.size(), resultList);
            }
            QSysMenu sysMenu = QSysMenu.sysMenu;

            Predicate predicate = sysMenu.deleted.eq(false);
            if (query.get("parentId") != null) {
                Integer parentId = (Integer) query.get("parentId");
                predicate = ExpressionUtils.and(predicate, sysMenu.parentId.eq(parentId));
            }
            if (query.get("keyword") != null) {
                String name = (String) query.get("keyword");
                String express = "%".concat(name).concat("%");
                Predicate or = ExpressionUtils.or(sysMenu.titleEn.like(express), sysMenu.title.like(express));
                or = ExpressionUtils.or(or, sysMenu.path.like(express));
                or = ExpressionUtils.or(or, sysMenu.remark.like(express));

                predicate = ExpressionUtils.and(predicate, or);
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "sort"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<SysMenu> page = sysMenuRepository.findAll(predicate, pagRequest);
            return transform(page);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysMenuVO findById(Integer id) throws ServiceException {
        try {
            SysMenu entity = sysMenuRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return sysMenuConvert.convert(entity);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(Integer parentId, SysMenuForm form) throws ServiceException {
        try {
            if (parentId != null && parentId > 0) {
                SysMenu parent = sysMenuRepository.findById(parentId).orElse(null);
                if (parent != null && !parent.isDeleted()) {
                    parentId = parent.getId();
                } else {
                    //parentId不存在或己删除
                    parentId = Constants.MENU_ROOT;
                }
            }
            SysMenu entity = sysMenuConvert.convert(form);
            entity.setParentId(parentId == null ? Constants.MENU_ROOT : parentId);
            sysMenuRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Integer id, SysMenuForm form) throws ServiceException {
        try {
            SysMenu entity = sysMenuRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                sysMenuRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer... ids) throws ServiceException {
        try {
            sysMenuRepository.deleteById(ids);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    /* ============================================================================================================ */
    @Override
    public List<GrantPermVO> findGrantPerms(Integer id) throws ServiceException {
        SysMenu menu = sysMenuRepository.findById(id).orElse(null);
        if (menu != null) {
            Set<Integer> menuPerms = Sets.newHashSet();
            menu.getPermissions().stream().forEach(perm -> {
                menuPerms.add(perm.getId());
            });
            List<GrantPermVO> list = sysPermissionService.findPermissionsWithGroups();
            list.stream().forEach(perm -> {
                perm.getActionsOptions().stream().forEach(act -> {
                    if (menuPerms.contains(act.getValue())) {
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
        SysMenu menu = sysMenuRepository.findById(id).orElse(null);
        if (menu != null) {
            menu.getPermissions().clear();
            permissions.stream().forEach(perm -> {
                SysPermission entity = new SysPermission();
                entity.setId(perm);
                //        entity.getMenus().add(menu);
                menu.getPermissions().add(entity);
            });
            sysMenuRepository.saveAndFlush(menu);
        }
    }

    /* ============================================================================================================ */
    private List<SysMenuTreeVO> findSubMenus(List<Integer> permissionList) {
        List<SysMenuTreeVO> resultList = Lists.newArrayList();
        if(permissionList.isEmpty()){
            return resultList;
        }
        List<SysMenu> list = sysMenuRepository.findByParentIdAndDeletedAndStatusOrderBySort(0, false, Constants.STATE_ON);
        for (SysMenu entity : list) {
            if (!entity.isDeleted() && entity.getStatus().intValue() == Constants.STATE_ON) {
                SysMenuTreeVO vo = findSubMenu(entity, permissionList);
                if (vo != null) {
                    resultList.add(vo);
                }
            }
        }
        return resultList;
    }

    private SysMenuTreeVO findSubMenu(SysMenu menu, List<Integer> permissionList) {
        List<SysMenuTreeVO> resultList = Lists.newArrayList();
        for (SysMenu entity : menu.getChildren()) {
            if (!entity.isDeleted() && entity.getStatus() == Constants.STATE_ON) {
                SysMenuTreeVO vo = findSubMenu(entity, permissionList);
                if (vo != null) {
                    resultList.add(vo);
                }
            }
        }
        if (!resultList.isEmpty()) {
            //具有有权限的叶子节点, 本节点不需检查权限, 直接放行
            SysMenuTreeVO result = convert(menu);
            result.setChildren(resultList);
            return result;
        }
        //叶子节点, 或者无可用叶子节点的非叶子节点
//        boolean isSuperAdmin = SecurityUtils.getSubject().hasRole(Constants.ROLE_SUPER_ADMIN);
//        if (isSuperAdmin) {
//            //超级管理员, 不检查权限    ==>  因为多色系统, 有些菜单 专用于其它角色, 怕有超管所检查权限
//            return convert(menu);
//        }

        // 检查该节点权限
        List<SysMenu> list = sysMenuRepository.findByMenuIdAndPermissions(menu.getId(), permissionList);
        if (!list.isEmpty()) {
            return convert(menu);
        }
        return null;
    }

    private List<SysMenuVO> findSubMenus() {
        List<SysMenuVO> resultList = Lists.newArrayList();
        QSysMenu sysMenu = QSysMenu.sysMenu;
        Predicate predicate = sysMenu.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, sysMenu.parentId.eq(0));

        Iterable<SysMenu> iterable = sysMenuRepository.findAll(predicate);
        iterable.forEach((entity) -> {
            if (!entity.isDeleted()) {
                SysMenuVO vo = findSubMenu(entity);
                if (vo != null) {
                    resultList.add(vo);
                }
            }
        });
        if (resultList.size() == 1) {
            //所有菜单都是以ROOT为根的, UI不显示ROOT菜单
            return resultList.get(0).getChildren();
        }
        return resultList;
    }

    private SysMenuVO findSubMenu(SysMenu menu) {
        List<SysMenuVO> resultList = Lists.newArrayList();
        for (SysMenu entity : menu.getChildren()) {
            if (!entity.isDeleted()) {
                SysMenuVO vo = findSubMenu(entity);
                if (vo != null) {
                    resultList.add(vo);
                }
            }
        }
        SysMenuVO result = sysMenuConvert.convert(menu);
        if (!resultList.isEmpty()) {
            result.setChildren(resultList);
        } else {
            result.setChildren(null);
        }
        return result;
    }

    /* ============================================================================================================ */
    private PageInfo<SysMenuVO> transform(Page<SysMenu> result) {
        List<SysMenuVO> resultList = sysMenuConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private SysMenuTreeVO convert(SysMenu entity) {
        SysMenuTreeVO vo = new SysMenuTreeVO();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setIcon(entity.getIcon());
        vo.setName(entity.getName());
        vo.setPath(entity.getPath());
        vo.setRedirect(entity.getRedirect());
        vo.setComponent(entity.getComponent());
        vo.setHidden(entity.getHidden());
        vo.setKeepAlive(entity.getKeepAlive());
        return vo;
    }
}
