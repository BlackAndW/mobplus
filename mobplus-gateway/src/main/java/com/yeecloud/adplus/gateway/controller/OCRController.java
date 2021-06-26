package com.yeecloud.adplus.gateway.controller;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.yeecloud.adplus.gateway.service.OCRService;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.cloud.vision.v1.Page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/24
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/google/ocr")
public class OCRController {

    @Autowired
    OCRService ocrService;

    @RequestMapping("text")
    public Result textOCR(@RequestParam("imagePath") String imagePath,
                          @RequestParam("isWeb") String isWeb) throws IOException {
        String result = ocrService.detectText(imagePath, isWeb);
        return Result.SUCCESS(result);
    }

    @RequestMapping("document")
    public Result documentOCR(@RequestParam("imagePath") String imagePath,
                              @RequestParam("isWeb") String isWeb) throws IOException {
        String result = ocrService.detectDocumentText(imagePath, isWeb);
        return Result.SUCCESS(result);
    }

}
