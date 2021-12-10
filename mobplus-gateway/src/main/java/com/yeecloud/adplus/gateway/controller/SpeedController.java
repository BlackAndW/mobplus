package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.yeecloud.adplus.gateway.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * 网速测试
 *
 * @author: Huang
 * @create: 2020-11-30 11:23
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/speed")
public class SpeedController {


    /**
     * 上传速度
     * TODO 等待优化
     *
     * @param request
     * @throws IOException
     */
    @PostMapping
    public void uploadSpeed(HttpServletRequest request) throws IOException {
        InputStream in = null;
        try {
            in = request.getInputStream();
            log.debug("ReqFromApp:{}", in.available());
        } catch (Throwable e) {
            throw new ServiceException(e.getMessage());
        } finally {
            in.close();
        }

    }

    @PostMapping("encode")
    public Result encode(@RequestBody String org) {
        return Result.ENCODE(org);
    }

    @PostMapping("decode")
    public String decode(@RequestBody String org) {
        return org;
    }
}
