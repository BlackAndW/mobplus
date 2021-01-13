package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.sys.form.SysMenuForm;
import com.yeecloud.adplus.admin.controller.sys.vo.GrantPermVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysMenuTreeVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysMenuVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

import java.util.List;

/**
 * 菜单信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
public interface SysMenuService {

    PageInfo<SysMenuVO> query(Query query) throws ServiceException;

    SysMenuVO findById(Integer id) throws ServiceException;

    void create(Integer parentId, SysMenuForm form) throws ServiceException;

    void update(Integer id, SysMenuForm form) throws ServiceException;

    void delete(Integer... ids) throws ServiceException;

    /*=======================*/
    List<SysMenuTreeVO> findMenusByUserId(Integer id) throws ServiceException;

    List<GrantPermVO> findGrantPerms(Integer id) throws ServiceException;

    void grantPermissions(Integer id, List<Integer> permissions) throws ServiceException;
}