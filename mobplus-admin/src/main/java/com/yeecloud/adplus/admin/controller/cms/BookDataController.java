package com.yeecloud.adplus.admin.controller.cms;

import com.yeecloud.adplus.admin.controller.cms.convert.BookConvert;
import com.yeecloud.adplus.admin.controller.cms.form.BookDataForm;
import com.yeecloud.adplus.admin.controller.cms.vo.BookDataVO;
import com.yeecloud.adplus.admin.service.BookDataService;
import com.yeecloud.adplus.dal.entity.BookData;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping
    @RequiresPermissions("cms:book:query")
    public Result queryBook(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        Page<BookData> page = bookDataService.query(new Query(params));
        PageInfo<BookDataVO> result = convert(page);
        return Result.SUCCESS(result);
    }

    private PageInfo<BookDataVO> convert(Page<BookData> page) {
        List<BookData> result = page.getContent();
        result.forEach(BookData::fakeData);
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
}
