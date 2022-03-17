package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.service.JpushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Leonard
 * @create: 2022/3/16
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/Jpush")
public class JpushController {

    @Autowired
    JpushService jpushService;

    @GetMapping("test")
    public void testJpush() {
        jpushService.pushMsg();
        System.out.println("test msg has been pushed!");
    }

}
