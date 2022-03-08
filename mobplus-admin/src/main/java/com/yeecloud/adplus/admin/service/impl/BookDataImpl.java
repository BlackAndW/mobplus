package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.form.BookDataForm;
import com.yeecloud.adplus.admin.service.BookDataService;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.BookChapter;
import com.yeecloud.adplus.dal.entity.BookData;
import com.yeecloud.adplus.dal.entity.QBookData;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.BookChapterRepository;
import com.yeecloud.adplus.dal.repository.BookDataRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Service
public class BookDataImpl implements BookDataService {

    @Autowired
    BookDataRepository bookDataRepository;

    @Autowired
    BookChapterRepository bookChapterRepository;

    @Autowired
    AppRepository appRepository;

    @Override
    public Page<BookData> query(Query query) throws ServiceException, ParseException {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        Integer appId = query.get("appId", Integer.class);
        if (appId == null || appId <= 0) {
            throw new ServiceException("appId is not exist!");
        }
        Integer isFree = query.get("isFree", Integer.class);
        if (isFree != null && isFree != 2) {
            predicate = ExpressionUtils.and(predicate, qBookData.isFree.eq(isFree));
        }
        predicate = ExpressionUtils.and(predicate, qBookData.app.id.eq(appId));
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pageRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return bookDataRepository.findAll(predicate, pageRequest);
    }

    @Override
    public BookData findById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void create(BookDataForm form) throws ServiceException {
        BookData bookData = new BookData();
        NewBeanUtils.copyProperties(bookData, form, true);
        App app = appRepository.getOne(form.getAppId());
        bookData.setApp(app);
        bookDataRepository.save(bookData);
    }

    @Override
    public void update(Integer bookId, BookDataForm form) throws ServiceException {
        BookData bookData = bookDataRepository.getOne(bookId);
        NewBeanUtils.copyProperties(bookData, form, true);
        bookDataRepository.save(bookData);
    }

    @Override
    public void delete(Integer[] ids) throws ServiceException {
        bookDataRepository.deleteById(ids);
    }

}
