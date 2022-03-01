package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.cms.form.BookPriceForm;
import com.yeecloud.adplus.admin.controller.cms.vo.BookPriceVO;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * @author: Leonard
 * @create: 2022/2/17
 */
public interface BookPriceService {

    BookPriceVO query(Integer appId) throws ServiceException;

    void update(BookPriceForm form) throws ServiceException;
}
