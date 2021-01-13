package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.sys.form.SysCategoryForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysCategoryVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

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
public interface SysCategoryService {

    PageInfo<SysCategoryVO> query(Query query) throws ServiceException;

    SysCategoryVO findById(Integer id) throws ServiceException;

    void create(Integer parentId, SysCategoryForm form) throws ServiceException;

    void update(Integer id, SysCategoryForm form) throws ServiceException;

    void delete(Integer... ids) throws ServiceException;

    /*============================*/
    List<SysCategoryVO> findByParentId(Integer parentId) throws ServiceException;
}