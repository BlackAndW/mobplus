package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.dal.entity.BookData;
import com.yeecloud.adplus.gateway.controller.form.BookForm;
import com.yeecloud.adplus.gateway.service.BookDataService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: Leonard
 * @create: 2022/2/28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    @Autowired
    BookDataService bookDataService;

    @PostMapping("list")
    public Result getBookList(@RequestBody BookForm form,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookDataService.queryBookList(form));
    }

    @PostMapping("detail")
    public Result getBookDetail(@RequestBody BookForm form,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookDataService.queryBookDetail(form));
    }

    @PostMapping("chapter/list")
    public Result getChapterList(@RequestBody BookForm form,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookDataService.queryChapterList(form));
    }

    @PostMapping("chapter")
    public Result getBookChapter(@RequestBody BookForm form,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return Result.isEncode(apiVersion, bookDataService.queryBookChapterContent(form));
    }

    @PostMapping("upload")
    public Result countBookData(@RequestBody BookForm form,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        bookDataService.countBookData(form);
        return Result.SUCCESS();
    }

    @PostMapping("chapter/upload")
    public Result countChapter(@RequestBody BookForm form,
                               @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        bookDataService.countBookChapter(form);
        return Result.SUCCESS();
    }

    @PostMapping("crawler/book/upload")
    public Result crawlerBookUpload(@RequestParam("book") MultipartFile book) throws Exception {
        bookDataService.crawlerBookUpload(book);
        return Result.SUCCESS();
    }

    @PostMapping("crawler/chapter/upload")
    public Result crawlerChapterUpload(@RequestParam(value = "chapters", required = false) MultipartFile chapters) throws Exception {
        bookDataService.crawlerChapterUpload(chapters);
        return Result.SUCCESS();
    }
}
