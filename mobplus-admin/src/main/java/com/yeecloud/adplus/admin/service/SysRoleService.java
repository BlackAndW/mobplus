package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.sys.form.SysRoleForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysRoleVO;
import com.yeecloud.adplus.admin.entity.SysRole;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

import java.util.List;

/**
 * 系统角色
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
public interface SysRoleService {

    PageInfo<SysRoleVO> query(Query query) throws ServiceException;

    SysRoleVO findById(Integer id) throws ServiceException;

    void create(SysRoleForm form) throws ServiceException;

    void update(Integer id, SysRoleForm form) throws ServiceException;

    void delete(Integer... ids) throws ServiceException;

    /*================================================*/
    List<String> findCodesByUserId(Integer userId) throws ServiceException;

    List<GrantPermVO> findGrantPerms(Integer id) throws ServiceException;

    void grantPermissions(Integer id, List<Integer> permissions) throws ServiceException;

    SysRole findByCode(String code) throws ServiceException;
}