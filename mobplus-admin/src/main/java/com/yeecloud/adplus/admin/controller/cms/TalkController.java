package com.yeecloud.adplus.admin.controller.cms;


import com.yeecloud.adplus.admin.controller.cms.form.TalkCategoryForm;
import com.yeecloud.adplus.admin.controller.cms.form.TalkContentForm;
import com.yeecloud.adplus.admin.controller.cms.form.TalkForm;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkCategoryVO;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkContentVO;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkVO;
import com.yeecloud.adplus.admin.service.TalkService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cms/talk")
public class TalkController {

    @Autowired
    private TalkService talkService;

    @GetMapping("/category")
    @RequiresPermissions("cms:talk:query")
    public Result<TalkCategoryVO> queryCategory(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<TalkCategoryVO> result = talkService.queryCategory(new Query(params));
        return Result.SUCCESS(result);
    }


    @PostMapping("/category")
    @RequiresPermissions("cms:talk:create")
    public Result createCategory(@RequestBody @Valid TalkCategoryForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        talkService.createCategory(form);
        return Result.SUCCESS();
    }


    @PutMapping("/category/{id}")
    @RequiresPermissions("cms:talk:edit")
    public Result updateCategory(@PathVariable Integer id, @RequestBody @Valid TalkCategoryForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        talkService.updateCategory(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping("/category")
    @RequiresPermissions("cms:talk:delete")
    public Result deleteCategory(@RequestBody Integer[] ids) throws ServiceException {
        talkService.deleteCategory(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/category/sct")
    @RequiresPermissions("cms:talk:query")
    public Result<TalkCategoryVO> queryCategorySct(@RequestParam Map<String, Object> params) throws ServiceException {
        Query query = new Query(params);
        PageInfo<TalkCategoryVO> result = talkService.queryCategory(query);
        return Result.SUCCESS(result);
    }


    @GetMapping
    @RequiresPermissions("cms:talk:query")
    public Result<TalkVO> queryTalk(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<TalkVO> result = talkService.queryTalk(new Query(params));
        return Result.SUCCESS(result);
    }

    @PostMapping
    @RequiresPermissions("cms:talk:create")
    public Result createTalk(@RequestBody @Valid TalkForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        talkService.createTalk(form);
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("cms:talk:edit")
    public Result updateTalk(@PathVariable Integer id, @RequestBody @Valid TalkForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        talkService.updateTalk(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping
    @RequiresPermissions("cms:talk:delete")
    public Result deleteTalk(@RequestBody Integer[] ids) throws ServiceException {
        talkService.deleteTalk(ids);
        return Result.SUCCESS();
    }

    @GetMapping("/content")
    @RequiresPermissions("cms:talk:query")
    public Result<TalkContentVO> queryTalkContent(@RequestParam Map<String, Object> params) throws ServiceException {
        PageInfo<TalkContentVO> result = talkService.queryTalkContent(new Query(params));
        return Result.SUCCESS(result);
    }

    @PostMapping("/content")
    @RequiresPermissions("cms:talk:create")
    public Result createTalk(@RequestBody @Valid TalkContentForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        talkService.createTalkContent(form);
        return Result.SUCCESS();
    }

    @PutMapping("/content/{id}")
    @RequiresPermissions("cms:talk:edit")
    public Result updateTalk(@PathVariable Integer id, @RequestBody @Valid TalkContentForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        talkService.updateTalkContent(id, form);
        return Result.SUCCESS();
    }

    @DeleteMapping("/content")
    @RequiresPermissions("cms:talk:delete")
    public Result deleteTalkContent(@RequestBody Integer[] ids) throws ServiceException {
        talkService.deleteTalkContent(ids);
        return Result.SUCCESS();
    }
}
