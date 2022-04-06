package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.BookData;
import com.yeecloud.adplus.gateway.controller.form.BookForm;
import com.yeecloud.adplus.gateway.controller.vo.BookChapterContentVO;
import com.yeecloud.adplus.gateway.controller.vo.BookChapterVO;
import com.yeecloud.adplus.gateway.controller.vo.BookDetailVO;
import com.yeecloud.adplus.gateway.controller.vo.BookVO;
import com.yeecloud.meeto.common.exception.ServiceException;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/3/1
 */
public interface BookService {

    List<BookVO> queryBookList(BookForm form) throws ServiceException;

    BookDetailVO queryBookDetail(BookForm form) throws ServiceException;

    List<BookChapterVO> queryChapterList(BookForm form) throws ServiceException;

    BookChapterContentVO queryBookChapterContent(BookForm form) throws ServiceException;

    void countBookData(BookForm form) throws ServiceException;

    void countBookChapter(BookForm form) throws ServiceException;
}
