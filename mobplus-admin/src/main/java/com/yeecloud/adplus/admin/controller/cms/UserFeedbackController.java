package com.yeecloud.adplus.admin.controller.cms;

import com.yeecloud.adplus.admin.controller.cms.convert.UserFeedbackConvert;
import com.yeecloud.adplus.admin.controller.cms.form.UserFeedbackForm;
import com.yeecloud.adplus.admin.controller.cms.vo.GameVO;
import com.yeecloud.adplus.admin.controller.cms.vo.UserFeedbackVO;
import com.yeecloud.adplus.admin.service.UserFeedbackService;
import com.yeecloud.adplus.dal.entity.GameLog;
import com.yeecloud.adplus.dal.entity.UserFeedback;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/9/2
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cms/user/feedback")
public class UserFeedbackController {

    @Autowired
    UserFeedbackService userFeedbackService;

    @Autowired
    UserFeedbackConvert userFeedbackConvert;

    @RequestMapping("list")
    @RequiresPermissions("cms:feedback:query")
    public Result userFeedbackList(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        Page<UserFeedback> result = userFeedbackService.query(new Query(params));
        PageInfo<UserFeedbackVO> resultList = convert(result);
        return Result.SUCCESS(resultList);
    }

    @RequestMapping("update/{id}")
    @RequiresPermissions("cms:feedback:edit")
    public Result update(@PathVariable Integer id, @RequestBody UserFeedbackForm form) throws ServiceException {
        userFeedbackService.update(id, form);
        return Result.SUCCESS();
    }

    private PageInfo<UserFeedbackVO> convert(Page<UserFeedback> result) {
        List<UserFeedbackVO> resultList = userFeedbackConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
