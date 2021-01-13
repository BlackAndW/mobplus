package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.sys.form.SysConfForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysConfVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

/**
 * 系统配置
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
public interface SysConfService {

    PageInfo<SysConfVO> query(Query query) throws ServiceException;

    SysConfVO findById(Integer id) throws ServiceException;

    void create(SysConfForm form) throws ServiceException;

    void update(Integer id, SysConfForm form) throws ServiceException;

    void delete(Integer... ids) throws ServiceException;

    /*=================================*/
    void refreshAsync();

    <T> T getValueByKey(String key, Class<T> clazz);
    <T> T getValueByKey(String key, T defValue);
}