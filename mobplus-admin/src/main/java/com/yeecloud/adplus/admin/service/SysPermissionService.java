package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.sys.form.SysPermissionForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysPermissionVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

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
public interface SysPermissionService {

    PageInfo<SysPermissionVO> query(Query query) throws ServiceException;

    SysPermissionVO findById(Integer id) throws ServiceException;

    void create(SysPermissionForm form) throws ServiceException;

    void update(Integer id, SysPermissionForm form) throws ServiceException;

    void delete(Integer... ids) throws ServiceException;

    /*==================================================*/
    List<String> findCodesByUserId(Integer userId) throws ServiceException;

    List<Integer> findIdsByUserId(Integer userId) throws ServiceException;

    List<GrantPermVO> findPermissionsWithGroups() throws ServiceException;
}