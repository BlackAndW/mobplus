package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.dal.entity.UserAdInfo;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.text.ParseException;

/**
 * @author: Leonard
 * @create: 2021/5/18
 */
public interface UserAdInfoService {
    Page<UserAdInfo> query(Query query) throws ServiceException, ParseException;

    String data2excel(Query query) throws ServiceException, ParseException;
}
