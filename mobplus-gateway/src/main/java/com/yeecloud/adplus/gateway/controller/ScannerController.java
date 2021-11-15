package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSONArray;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.yeecloud.adplus.dal.entity.Feedback;
import com.yeecloud.adplus.gateway.controller.form.FeedbackForm;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.controller.vo.ScannerVO;
import com.yeecloud.adplus.gateway.service.ScannerService;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.adplus.gateway.util.Base64Util;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能识图
 * @author: Leonard
 * @create: 2021/6/22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/scanner")
public class ScannerController {

    @Resource
    TranslateService translateService;

    @Resource
    ScannerService scannerService;

    @RequestMapping("/test")
    public Result test() throws IOException, ServiceException {
        File file = new File("C:\\Users\\Admin\\Pictures\\3cbd83a0a2195d06363b306e4fe63fd9.jpeg");
        byte[] data = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imgStr = Base64Util.encode(data);
        String imgParam = URLEncoder.encode(imgStr, "UTF-8");
        ScannerForm formTest = new ScannerForm();
        formTest.setType(0);
        formTest.setToLang("en");
        formTest.setImageBase64(imgParam);
        List<ScannerVO> result = scannerService.getResult(formTest);
        return Result.SUCCESS(result);
    }

    /**
     * 图像识别
     * @param imageBase64 base64编码后的图片
     * @param type 图片类型 0: 通用; 1: 动物; 2: 植物; 3: 食物; 4: 货币; 5: 地标风景
     * @param toLang 翻译成的语言简称 如:zh,en
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    @ApiDoc
    @RequestMapping("/imageScan")
    public Result imageScan(@RequestBody ScannerForm form,
                                             @RequestParam(required = true, defaultValue = "") String imageBase64,
                                             @RequestParam(required = true, defaultValue = "0") Integer type,
                                             @RequestParam(required = true, defaultValue = "") String toLang,
                                             @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws IOException, ServiceException {
        try {
            String imgParam = URLEncoder.encode(form.getImageBase64(), "UTF-8");
            form.setImageBase64(imgParam);
            List<ScannerVO> result = scannerService.getResult(form);
            return Result.isEncode(apiVersion, result);
        } catch (Exception e) {
            return Result.FAILURE(e.getMessage());
        }
    }

    /**
     * 用户反馈
     * @param form
     * @return
     */
    @ApiDoc
    @RequestMapping("/feedback")
    public Result feedback(@RequestBody FeedbackForm form) {
        scannerService.insertFeedbackLog(form);
        return Result.SUCCESS();
    }

    /**
     * 文本翻译（字符串）
     * @param sourceString  源字符串
     * @param fromLang      源语言简称
     * @param toLang        目标语言简称
     * @return
     */
    @ApiDoc
    @RequestMapping("/translate")
    public Result translateText(@RequestBody TranslateForm form,
                                @RequestParam(required = true, defaultValue = "") String sourceString,
                                @RequestParam(required = false, defaultValue = "") String fromLang,
                                @RequestParam(required = true, defaultValue = "") String toLang,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion){
        String result = translateService.translation(form);
        if (result == null) { return Result.FAILURE("translation error!"); }
        return Result.isEncode(apiVersion, result);
    }

    /**
     * 文本翻译（列表）
     * @param sourceList  源列表
     * @param fromLang      源语言简称
     * @param toLang        目标语言简称
     * @return
     */
    @ApiDoc
    @RequestMapping("/translateList")
    public Result translateTextList(@RequestBody TranslateForm form,
                                @RequestParam(required = true, defaultValue = "") String sourceList,
                                @RequestParam(required = false, defaultValue = "") String fromLang,
                                @RequestParam(required = true, defaultValue = "") String toLang,
                                                  @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion){
        List<String> result = translateService.translationList(form);
        if (result == null) { return Result.FAILURE("translation error!"); }
        return Result.isEncode(apiVersion, result);
    }
}
