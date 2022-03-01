package com.yeecloud.adplus.admin.controller.cms;

import com.yeecloud.adplus.admin.controller.cms.form.BookPriceForm;
import com.yeecloud.adplus.admin.controller.cms.vo.BookPriceVO;
import com.yeecloud.adplus.admin.service.BookPriceService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Leonard
 * @create: 2022/2/17
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cms/book/price")
public class BookPriceController {

    @Autowired
    BookPriceService bookPriceService;

    @GetMapping
    @RequiresPermissions("cms:book:query")
    public Result queryPrice(@RequestParam Integer appId) throws ServiceException{
        BookPriceVO vo = bookPriceService.query(appId);
        return Result.SUCCESS(vo);
    }

    @PostMapping
    public Result updatePrice(@RequestBody BookPriceForm form) throws ServiceException {
        bookPriceService.update(form);
        return Result.SUCCESS();
    }
}
