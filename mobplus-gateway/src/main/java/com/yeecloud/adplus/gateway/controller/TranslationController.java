package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/google")
public class TranslationController {

    @Resource
    TranslateService translateService;

    @RequestMapping("/translate/list")
    public Result translateTextList(@RequestBody TranslateForm form){
        List<String> result = translateService.translationList(form);
        if (result == null) { return Result.FAILURE("translation error!"); }
        return Result.SUCCESS(result);
    }

    @RequestMapping("/translate")
    public Result translateText(@RequestBody TranslateForm form){
        String result = translateService.translation(form);
        if (result == null) { return Result.FAILURE("translation error!"); }
        return Result.SUCCESS(result);
    }
}
