package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSONArray;
import com.yeecloud.adplus.dal.entity.Feedback;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
import com.yeecloud.adplus.gateway.service.ScannerService;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.adplus.gateway.util.Base64Util;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
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

    @RequestMapping("/translate")
    public Result translateText(@RequestBody ScannerForm form){
        String result = translateService.translation(form);
        if (result == null) { return Result.FAILURE("translation error!"); }
        return Result.SUCCESS(result);
    }


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
        JSONArray result = scannerService.getResultArr(formTest);
        return Result.SUCCESS(result);
    }


    @RequestMapping("/imageScan")
    public Result imageScan(@RequestBody ScannerForm form) throws IOException, ServiceException {
        try {
            System.out.println(form.toString());
            String imgParam = URLEncoder.encode(form.getImageBase64(), "UTF-8");
            form.setImageBase64(imgParam);
            JSONArray result = scannerService.getResultArr(form);
            return Result.SUCCESS(result);
        } catch (Exception e) {
            return Result.FAILURE(e.getMessage());
        }
    }

    @RequestMapping("/feedback")
    public Result feedback(@RequestBody Feedback form) {
        try {
            scannerService.insertFeedbackLog(form);
            return Result.SUCCESS();
        } catch (Exception e) {
            return Result.FAILURE(e.getMessage());
        }
    }
}
