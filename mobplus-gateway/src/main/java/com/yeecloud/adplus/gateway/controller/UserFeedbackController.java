package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.gateway.controller.form.UserFeedbackForm;
import com.yeecloud.adplus.gateway.service.UserFeedbackService;
import com.yeecloud.adplus.gateway.util.OkHttpUtils;
import com.yeecloud.meeto.common.codec.Codec;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.ParamUtils;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    HttpServletRequest request;

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
            String ipAddress = ParamUtils.getIpAddr(request);
            Map<String, Object> params = new HashMap<>();
            params.put("ip", ipAddress);
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            final Request request = new Request.Builder()
                    .url(OkHttpUtils.VPN_URL + "/app/api/v1/c04//query/ip")
                    .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                    .build();
            String area = OkHttpUtils.Response(request);
            // 去除双引号
            area = area.substring(1, area.length() - 1);
            userFeedbackService.commit(form, ipAddress, area);
        }catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        }
        return needCodec ? Codec.encode(response) : Result.SUCCESS().toString();
    }
}
