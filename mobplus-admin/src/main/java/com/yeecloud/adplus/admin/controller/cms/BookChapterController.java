package com.yeecloud.adplus.admin.controller.cms;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.cms.convert.BookConvert;
import com.yeecloud.adplus.admin.controller.cms.form.BookChapterForm;
import com.yeecloud.adplus.admin.controller.cms.vo.BookChapterVO;
import com.yeecloud.adplus.admin.service.BookChapterService;
import com.yeecloud.adplus.admin.service.BookChapterService;
import com.yeecloud.adplus.admin.util.FileUtil;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.BookChapter;
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
import java.io.File;
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
@RequestMapping("/api/cms/book/chapter")
public class BookChapterController {

    @Autowired
    BookChapterService bookChapterService;

    @Autowired
    BookConvert bookConvert;

    @GetMapping
    @RequiresPermissions("cms:book:query")
    public Result queryBook(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        Page<BookChapter> page = bookChapterService.query(new Query(params));
        PageInfo<BookChapterVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    private PageInfo<BookChapterVO> convert(Page<BookChapter> page) {
        List<BookChapter> pageList = page.getContent();
        for (BookChapter chapter : pageList) {
            chapter.fakeData();
            if (chapter.getReadCount() != 0) {
                chapter.calExitPer();
            }
        }
        List<BookChapterVO> resultList = bookConvert.convertBookChapter(pageList);
        return new PageInfo<>(page.getNumber() + 1, page.getSize(), (int) page.getTotalElements(), resultList);
    }

    @PostMapping
    @RequiresPermissions("cms:book:create")
    public Result create(@RequestBody @Valid BookChapterForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        bookChapterService.create(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("cms:book:edit")
    public Result updateBookChapter(@PathVariable Integer id, @RequestBody @Valid BookChapterForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        bookChapterService.update(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("cms:book:delete")
    public Result deleteBanner(@RequestBody Integer[] ids) throws ServiceException {
        bookChapterService.delete(ids);
        return Result.SUCCESS();
    }

    @PostMapping("/crawler/upload")
    public Result crawlerUpload(@RequestParam(value = "chapters", required = false) MultipartFile chapters) throws IOException {
        okhttp3.RequestBody fileBody = okhttp3.RequestBody.create(FileUtil.toFile(chapters), MediaType.parse("multipart/form-data"));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chapters", chapters.getOriginalFilename(), fileBody)
                .build();
        final Request request = new Request.Builder()
                .url("http://localhost:9091" + "/api/v1/book/crawler/chapter/upload")
                .post(requestBody)
                .build();
        return Result.SUCCESS(OkHttpUtils.Response(request));
    }
}
