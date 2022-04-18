package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.BookForm;
import com.yeecloud.adplus.gateway.service.BookDataService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Leonard
 * @create: 2022/2/28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    @Autowired
    BookDataService bookService;

    @PostMapping("list")
    public Result getBookList(@RequestBody BookForm form,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookService.queryBookList(form));
    }

    @PostMapping("detail")
    public Result getBookDetail(@RequestBody BookForm form,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookService.queryBookDetail(form));
    }

    @PostMapping("chapter/list")
    public Result getChapterList(@RequestBody BookForm form,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookService.queryChapterList(form));
    }

    @PostMapping("chapter")
    public Result getBookChapter(@RequestBody BookForm form,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookService.queryBookChapterContent(form));
    }

    @PostMapping("upload")
    public Result countBookData(@RequestBody BookForm form,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        bookService.countBookData(form);
        return Result.SUCCESS();
    }

    @PostMapping("chapter/upload")
    public Result countChapter(@RequestBody BookForm form,
                               @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        bookService.countBookChapter(form);
        return Result.SUCCESS();
    }
}
