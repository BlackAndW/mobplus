package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.UserAdInfoForm;
import com.yeecloud.adplus.gateway.service.UserAdInfoService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.ParamUtils;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
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

    @PostMapping("/count")
    public Result setCounting(@RequestBody UserAdInfoForm userForm) throws ServiceException {
        String ipAddress = ParamUtils.getIpAddr(request);
        if (StringUtils.isEmpty(ipAddress)) {
            return Result.FAILURE("ipAddress is empty!");
        }
        userAdInfoService.createOrUpdateInfo(userForm, ipAddress);
        return Result.SUCCESS();
    }

}
