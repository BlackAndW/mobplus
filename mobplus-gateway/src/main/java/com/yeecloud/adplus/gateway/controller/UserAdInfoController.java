package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.UserAdInfoForm;
import com.yeecloud.adplus.gateway.service.UserAdInfoService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.ParamUtils;
import com.yeecloud.meeto.common.util.StringUtils;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * app广告数据统计
 * @author: Leonard
 * @create: 2021/3/23
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserAdInfoController {

    @Autowired
    UserAdInfoService userAdInfoService;

    @Autowired
    HttpServletRequest request;

    /**
     * 数据更新
     * @param userForm
     * @return
     * @throws ServiceException
     */
    @PostMapping("/count")
    public Result setCounting(@RequestBody UserAdInfoForm userForm,
                              @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        String ipAddress = ParamUtils.getIpAddr(request);
        if (StringUtils.isEmpty(ipAddress)) {
            return Result.FAILURE("ipAddress is empty!");
        }
        String result = userAdInfoService.createOrUpdateInfo(userForm, ipAddress);
        return Result.isEncode(apiVersion, result);
    }

}
