package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.app.form.AppLinkForm;
import com.yeecloud.adplus.dal.entity.AppLink;
import com.yeecloud.adplus.dal.entity.AppLinkLog;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;

import java.text.ParseException;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
public interface AppLinkService extends BaseService<AppLink, AppLinkForm> {

    Page<AppLinkLog> queryLog(Query query) throws ServiceException, ParseException;
}
