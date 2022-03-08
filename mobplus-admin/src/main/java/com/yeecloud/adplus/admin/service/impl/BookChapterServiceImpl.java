package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.form.BookChapterForm;
import com.yeecloud.adplus.admin.service.BookChapterService;
import com.yeecloud.adplus.dal.entity.BookChapter;
import com.yeecloud.adplus.dal.entity.BookData;
import com.yeecloud.adplus.dal.entity.QBookChapter;
import com.yeecloud.adplus.dal.entity.QBookData;
import com.yeecloud.adplus.dal.repository.BookChapterRepository;
import com.yeecloud.adplus.dal.repository.BookDataRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Service
public class BookChapterServiceImpl implements BookChapterService {

    @Autowired
    BookChapterRepository bookChapterRepository;

    @Autowired
    BookDataRepository bookDataRepository;

    @Autowired
    CacheManager cacheManager;

    private static final Integer SERIAL_STATUS = 1;

    private static final Integer UPDATE_STATUS = 2;

    /** 3天 */
    private static final long CHECK_TIME = 1000 * 60 * 60 * 24 * 3;

    @Override
    public Page<BookChapter> query(Query query) throws ServiceException, ParseException {
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        Integer bookId = query.get("bookId", Integer.class);
        if (bookId == null || bookId == 0) {
            bookId = 0;
        }
        String startTimeStr = query.get("startTimeStr", String.class);
        String endTimeStr = query.get("endTimeStr", String.class);
        if (startTimeStr != null && startTimeStr.length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(startTimeStr).getTime();
            long endTime = simpleDateFormat.parse(endTimeStr).getTime();
            predicate = ExpressionUtils.and(predicate, qBookChapter.createdAt.between(startTime, endTime));
        }
        predicate = ExpressionUtils.and(predicate, qBookChapter.bookData.id.eq(bookId));
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pageRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return bookChapterRepository.findAll(predicate, pageRequest);
    }

    @Override
    public BookChapter findById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void create(BookChapterForm form) throws ServiceException {
        Cache shortCache = cacheManager.getCache("nameCaches");
        assert shortCache != null;
        if (shortCache.get(form.getChapterNo() + form.getName()) != null) {
            return;
        } else {
            shortCache.put(form.getChapterNo() + form.getName(), 1);
        }
        BookChapter bookChapter = new BookChapter();
        NewBeanUtils.copyProperties(bookChapter, form, true);
        BookData bookData = bookDataRepository.getOne(form.getBookId());
        bookChapter.setBookData(bookData);
        updateSize(form.getContent(),bookData, bookChapter);
        bookChapterRepository.save(bookChapter);
        bookData.setStatus(UPDATE_STATUS);
        bookDataRepository.save(bookData);
    }

    @Override
    public void update(Integer id, BookChapterForm form) throws ServiceException {
        BookChapter bookChapter = bookChapterRepository.getOne(id);
        NewBeanUtils.copyProperties(bookChapter, form, true);
        BookData bookData = bookDataRepository.getOne(form.getBookId());
        updateSize(form.getContent(), bookData, bookChapter);
        bookChapterRepository.save(bookChapter);
    }

    @Override
    public void delete(Integer[] ids) throws ServiceException {
        bookChapterRepository.deleteById(ids);
    }

    @Override
    public void checkBookStatus() {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qBookData.status.eq(UPDATE_STATUS));
        List<BookData> bookDataList = (List<BookData>) bookDataRepository.findAll(predicate);
        for (BookData bookData : bookDataList) {
            long modifiedAt = bookData.getModifiedAt();
            long nowTime = System.currentTimeMillis();
            if (nowTime - modifiedAt > CHECK_TIME) {
                bookData.setStatus(SERIAL_STATUS);
                bookDataRepository.save(bookData);
            }
        }
    }

    private void updateSize(String content, BookData bookData, BookChapter bookChapter) {
        long new_size = content.replace(" ","").length();
        long old_size = bookChapter.getSize();
        bookChapter.setSize(new_size);
        long changeSize = new_size - old_size;
        bookData.setSize(bookData.getSize() + changeSize);
        bookDataRepository.save(bookData);
    }
}
