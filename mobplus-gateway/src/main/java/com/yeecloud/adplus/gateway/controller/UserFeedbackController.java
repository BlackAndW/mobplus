package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.yeecloud.adplus.gateway.controller.form.UserFeedbackForm;
import com.yeecloud.adplus.gateway.service.UserFeedbackService;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Leonard
 * @create: 2021/9/3
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user/feedback")
public class UserFeedbackController {

    @Autowired
    UserFeedbackService userFeedbackService;

    @PostMapping("commit")
    public String commitUserFeedback(@RequestParam(value = "m", required = false) String m,
                                     @RequestBody(required = false) String body) {
        boolean needCodec = m == null || m.trim().length() == 0;
        if (body != null && needCodec) {
            body = Codec.decode(body);
        }
        String response = "";
        try {
            UserFeedbackForm form = JSON.parseObject(body, UserFeedbackForm.class);
            userFeedbackService.commit(form);
        }catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return needCodec ? Codec.encode(response) : response;
    }
}
