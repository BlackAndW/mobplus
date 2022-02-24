package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.form.BookPriceForm;
import com.yeecloud.adplus.admin.controller.cms.vo.BookPriceVO;
import com.yeecloud.adplus.admin.service.BookPriceService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.BookPrice;
import com.yeecloud.adplus.dal.entity.QBookPrice;
import com.yeecloud.adplus.dal.repository.BookPriceReository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

/**
 * @author: Leonard
 * @create: 2022/2/17
 */
@Service
public class BookPriceServiceImpl implements BookPriceService {

    @Autowired
    BookPriceReository bookPriceReository;

    @Override
    public BookPriceVO query(Integer appId) throws ServiceException {
        QBookPrice qBookPrice = QBookPrice.bookPrice;
        Predicate predicate = qBookPrice.deleted.eq(false);
        if (appId == 0) {
            throw new ServiceException("appId is not exist");
        }
        predicate = ExpressionUtils.and(predicate, qBookPrice.app.id.eq(appId));
        BookPrice bookPrice = bookPriceReository.findOne(predicate).orElse(null);
        BookPriceVO vo = new BookPriceVO();
        if (bookPrice != null) {
            NewBeanUtils.copyProperties(vo, bookPrice, true);
        } else {
            BookPrice bookPricePO = new BookPrice();
            NewBeanUtils.copyProperties(bookPricePO, vo, true);
            App app = new App();
            app.setId(appId);
            bookPricePO.setApp(app);
            bookPriceReository.save(bookPricePO);
        }
        return vo;
    }

    @Override
    public void update(BookPriceForm form) throws ServiceException {
        QBookPrice qBookPrice = QBookPrice.bookPrice;
        Predicate predicate = qBookPrice.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qBookPrice.app.id.eq(form.getAppId()));
        BookPrice bookPrice = bookPriceReository.findOne(predicate).orElse(null);
        NewBeanUtils.copyProperties(bookPrice, form, true);
        bookPriceReository.save(bookPrice);
    }
}
