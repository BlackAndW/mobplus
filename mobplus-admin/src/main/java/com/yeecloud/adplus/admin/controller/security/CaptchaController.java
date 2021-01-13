package com.yeecloud.adplus.admin.controller.security;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * Title
 *
 * Date: 2019-11-09 01:51:37
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class CaptchaController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/captcha.png")
    public void process() {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        try {
            String capText = generateCaptchaText();
//            log.debug("Captcha: [{}]", capText);
            BufferedImage bi = defaultKaptcha.createImage(capText);
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            try {
                out.flush();
            } finally {
                out.close();
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    private String generateCaptchaText() {
        String text = defaultKaptcha.createText();
        SecurityUtils.getSubject().getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
//        log.debug("Put Captcha:[{}] SessionId:[{}]", text, SecurityUtils.getSubject().getSession().getId());
        return text;
    }

    public static boolean validate(String code) {
        String tmp = (String) SecurityUtils.getSubject().getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
//        log.debug("Valid: [{}] VS [{}] SessionId:[{}]", code, tmp, SecurityUtils.getSubject().getSession().getId());
        return StringUtils.equals(code, tmp);
    }
}
