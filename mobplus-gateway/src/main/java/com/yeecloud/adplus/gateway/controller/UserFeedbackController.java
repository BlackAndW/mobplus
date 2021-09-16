package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.UserFeedbackForm;
import com.yeecloud.adplus.gateway.service.UserFeedbackService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.result.Result;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户反馈
 * @author: Leonard
 * @create: 2021/9/3
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user/feedback")
public class UserFeedbackController {

    @Autowired
    UserFeedbackService userFeedbackService;

    /**
     * 用户反馈信息提交
     * @param m 加密标识
     * @param form 用户表单
     * @return
     */
    @ApiDoc(stringResult = "{ code:2000, message:'ok' }")
    @PostMapping("commit")
    public String commitUserFeedback(@RequestParam(value = "m") String m,
                                     @RequestBody UserFeedbackForm form) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (form != null && needCodec) {
            String body = Codec.decode(form.toString());
        }
        String response = "";
        try {
//            UserFeedbackForm form = JSON.parseObject(body, UserFeedbackForm.class);
            userFeedbackService.commit(form);
        }catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return needCodec ? Codec.encode(response) : Result.SUCCESS().toString();
    }
}
