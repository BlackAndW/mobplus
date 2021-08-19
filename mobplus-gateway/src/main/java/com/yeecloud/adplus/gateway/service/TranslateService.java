package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.TranslateForm;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
public interface TranslateService {

    List<String> translationList(TranslateForm form);

    String translation(TranslateForm form);

}
