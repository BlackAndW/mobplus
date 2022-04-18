package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.BookChapter;
import com.yeecloud.adplus.dal.entity.BookData;
import com.yeecloud.adplus.dal.entity.QBookChapter;
import com.yeecloud.adplus.dal.entity.QBookData;
import com.yeecloud.adplus.dal.repository.BookChapterRepository;
import com.yeecloud.adplus.dal.repository.BookDataRepository;
import com.yeecloud.adplus.gateway.controller.form.BookForm;
import com.yeecloud.adplus.gateway.controller.vo.BookChapterContentVO;
import com.yeecloud.adplus.gateway.controller.vo.BookChapterVO;
import com.yeecloud.adplus.gateway.controller.vo.BookDetailVO;
import com.yeecloud.adplus.gateway.controller.vo.BookVO;
import com.yeecloud.adplus.gateway.service.BookDataService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/3/1
 */
@Service
public class BookDataServiceImpl implements BookDataService {

    @Autowired
    BookDataRepository bookDataRepository;

    @Autowired
    BookChapterRepository bookChapterRepository;

    @Override
    public List<BookVO> queryBookList(BookForm form) throws ServiceException {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        String appId = form.getAppId();
        if (StringUtils.isEmpty(appId)) {
            throw new ServiceException("appId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookData.app.appId.eq(appId));
        Integer pageNo = form.getPageNo() == null ? 0 : form.getPageNo();
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "readCount"));
        PageRequest pageRequest = PageRequest.of(pageNo, 10, sort);
        List<BookData> bookDataList = bookDataRepository.findAll(predicate, pageRequest).getContent();
        List<BookVO> bookVOList = new ArrayList<>();
        bookDataList.forEach(BookData::fakeData);
        for(BookData bookData: bookDataList) {
            BookVO vo = new BookVO();
            NewBeanUtils.copyProperties(vo, bookData);
            vo.setBookId(bookData.getId());
            bookVOList.add(vo);
        }
        return bookVOList;
    }

    @Override
    public BookDetailVO queryBookDetail(BookForm form) throws ServiceException {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        Integer bookId = form.getBookId();
        if (bookId == null || bookId == 0) {
            throw new ServiceException("bookId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookData.id.eq(bookId));
        BookData bookData = bookDataRepository.findOne(predicate).orElse(null);
        if (bookData == null) {
            throw new ServiceException("cannot find data by bookId: " + bookId);
        }
        bookData.fakeData();
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate2 = qBookChapter.deleted.eq(false);
        predicate2 = ExpressionUtils.and(predicate2, qBookChapter.bookData.id.eq(bookId));
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "chapterNo"), new Sort.Order(Sort.Direction.ASC, "createdAt"));
        List<BookChapter> bookChapterList = (List<BookChapter>) bookChapterRepository.findAll(predicate2, sort);
        List<BookChapterVO> chapterVOList = new ArrayList<>();
        long readCount = 0;
        for (BookChapter chapter : bookChapterList) {
            BookChapterVO chapterVO = new BookChapterVO();
            NewBeanUtils.copyProperties(chapterVO, chapter);
            chapterVO.setChapterId(chapter.getId());
            chapterVOList.add(chapterVO);
            readCount += chapter.getReadCount();
        }
        BookDetailVO detailVO = new BookDetailVO();
        NewBeanUtils.copyProperties(detailVO, bookData);
        detailVO.setBookId(bookData.getId());
        detailVO.setChapterList(chapterVOList);
        detailVO.setReadCount(readCount);
        return detailVO;
    }

    @Override
    public List<BookChapterVO> queryChapterList(BookForm form) throws ServiceException {
        Integer bookId = form.getBookId();
        if (bookId == null || bookId == 0) {
            throw new ServiceException("bookId is empty!");
        }
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qBookChapter.bookData.id.eq(bookId));
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "chapterNo"), new Sort.Order(Sort.Direction.ASC, "createdAt"));
        List<BookChapter> bookChapterList = (List<BookChapter>) bookChapterRepository.findAll(predicate, sort);
        List<BookChapterVO> chapterVOList = new ArrayList<>();
        for (BookChapter chapter : bookChapterList) {
            BookChapterVO chapterVO = new BookChapterVO();
            NewBeanUtils.copyProperties(chapterVO, chapter);
            chapterVO.setChapterId(chapter.getId());
            chapterVOList.add(chapterVO);
        }
        return chapterVOList;
    }

    @Override
    public BookChapterContentVO queryBookChapterContent(BookForm form) throws ServiceException {
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        if (form.getChapterId() == null) {
            throw new ServiceException("chapterId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookChapter.id.eq(form.getChapterId()));
        BookChapter chapter = bookChapterRepository.findOne(predicate).orElse(null);
        if (chapter == null) {
            throw new ServiceException("cannot find chapter by chapterId: " + form.getChapterId());
        }
        BookChapterContentVO vo = new BookChapterContentVO();
        NewBeanUtils.copyProperties(vo, chapter);
        return vo;
    }

    @Override
    public void countBookData(BookForm form) throws ServiceException {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        Integer bookId = form.getBookId();
        if (bookId == null || bookId == 0) {
            throw new ServiceException("bookId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookData.id.eq(bookId));
        BookData bookData = bookDataRepository.findOne(predicate).orElse(null);
        if (bookData == null) {
            throw new ServiceException("cannot find data by bookId: " + bookId);
        }
        if (form.getUserCount() != null && form.getUserCount() == 1) {
            bookData.setUserCount(bookData.getUserCount() + 1);
        }
        if (form.getCollection() != null && form.getCollection() == 1) {
            bookData.setCollection(bookData.getCollection() + 1);
        }
        if (form.getLockCount() != null && form.getLockCount() == 1) {
            bookData.setLockCount(bookData.getLockCount() + 1);
        }
        bookDataRepository.save(bookData);
    }

    @Override
    public void countBookChapter(BookForm form) throws ServiceException {
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        if (form.getChapterId() == null) {
            throw new ServiceException("chapterId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookChapter.id.eq(form.getChapterId()));
        BookChapter chapter = bookChapterRepository.findOne(predicate).orElse(null);
        if (chapter == null) {
            throw new ServiceException("cannot find chapter by chapterId: " + form.getChapterId());
        }
        if (form.getReadCount() != null && form.getReadCount() == 1) {
            chapter.setReadCount(chapter.getReadCount() + 1);
        }
        if (form.getExitCount() != null && form.getExitCount() == 1) {
            chapter.setExitCount(chapter.getExitCount() + 1);
        }
        bookChapterRepository.save(chapter);
    }
}
