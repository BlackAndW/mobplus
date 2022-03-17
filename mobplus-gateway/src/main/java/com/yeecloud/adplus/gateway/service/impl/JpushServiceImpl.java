package com.yeecloud.adplus.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.gateway.controller.form.JpushMsg;
import com.yeecloud.adplus.gateway.service.JpushService;
import com.yeecloud.adplus.gateway.util.JpushUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: Leonard
 * @create: 2022/3/16
 */
@Service
public class JpushServiceImpl implements JpushService {

    @Override
    public void pushMsg() {
        HashMap<String, String> iosExtra = new HashMap<>();
        int randomIndex = new Random().nextInt(12);
        for (JpushMsg msg : JpushMsg.values()) {
            if (msg.getIndex() == randomIndex) {
                iosExtra.put("route", String.valueOf(randomIndex));
                JpushUtil.push("all", "", msg.getContent(), msg.getTitle(), iosExtra);
                break;
            }
        }
    }
}
