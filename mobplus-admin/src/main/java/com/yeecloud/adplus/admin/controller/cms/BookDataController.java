package com.yeecloud.adplus.admin.controller.cms;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.convert.BookConvert;
import com.yeecloud.adplus.admin.controller.cms.form.BookDataForm;
import com.yeecloud.adplus.admin.controller.cms.vo.BookDataVO;
import com.yeecloud.adplus.admin.service.BookDataService;
import com.yeecloud.adplus.admin.util.FileUtil;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.BookChapter;
import com.yeecloud.adplus.dal.entity.BookData;
import com.yeecloud.adplus.dal.entity.QBookChapter;
import com.yeecloud.adplus.dal.repository.BookChapterRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cms/book/manager")
public class BookDataController {

    @Autowired
    BookDataService bookDataService;

    @Autowired
    BookConvert bookConvert;

    @Autowired
    BookChapterRepository bookChapterRepository;

    @GetMapping
    @RequiresPermissions("cms:book:query")
    public Result queryBook(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        Page<BookData> page = bookDataService.query(new Query(params));
        PageInfo<BookDataVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    private PageInfo<BookDataVO> convert(Page<BookData> page) {
        List<BookData> result = page.getContent();
        for (BookData bookData : result) {
            bookData.setReadCount(totalReadCount(bookData));
            bookData.fakeData();
        }
        List<BookDataVO> resultList = bookConvert.convertBookData(page.getContent());
        return new PageInfo<>(page.getNumber() + 1, page.getSize(), (int) page.getTotalElements(), resultList);
    }

    @PostMapping
    @RequiresPermissions("cms:book:create")
    public Result create(@RequestBody @Valid BookDataForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        bookDataService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("cms:book:edit")
    public Result updateBookData(@PathVariable Integer id, @RequestBody @Valid BookDataForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        bookDataService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("cms:book:delete")
    public Result deleteBanner(@RequestBody Integer[] ids) throws ServiceException {
        bookDataService.delete(ids);
        return Result.SUCCESS();
    }

    private long totalReadCount(BookData bookData) {
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qBookChapter.bookData.eq(bookData));
        List<BookChapter> chapterList = (List<BookChapter>) bookChapterRepository.findAll(predicate);
        return chapterList.stream().mapToLong(BookChapter::getReadCount).sum();
    }

    @PostMapping("/crawler/upload")
    public Result crawlerUpload(@RequestParam(value = "book", required = false) MultipartFile book) throws IOException {
        okhttp3.RequestBody fileBody = okhttp3.RequestBody.create(FileUtil.toFile(book), MediaType.parse("multipart/form-data"));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("book", book.getOriginalFilename(), fileBody)
                .build();
        final Request request = new Request.Builder()
                .url("https://ap.owlnovel2022.com" + "/v1/book/crawler/book/upload")
                .post(requestBody)
                .build();
        return Result.SUCCESS(OkHttpUtils.Response(request));
    }
}
