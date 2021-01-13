package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.sys.form.SysPasswdForm;
import com.yeecloud.adplus.admin.controller.sys.form.SysUserForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysUserVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

import java.util.List;

/**
 * 系统用户
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
public interface SysUserService {

    List<SysUserVO> findByRoleCode(String code) throws ServiceException;

    SysUserVO findByUserName(String account) throws ServiceException;

    PageInfo<SysUserVO> query(Query query) throws ServiceException;

    SysUserVO findById(Integer id) throws ServiceException;

    void create(SysUserForm form) throws ServiceException;

    void update(Integer id, SysUserForm form) throws ServiceException;

    void delete(Integer... ids) throws ServiceException;

    void updatePasswd(Integer id, SysPasswdForm form) throws ServiceException;

    /*==============================*/
    SysUserVO findWithRolesByUserName(String account) throws ServiceException;
}