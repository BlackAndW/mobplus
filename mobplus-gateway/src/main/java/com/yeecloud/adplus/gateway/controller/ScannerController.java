package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSONArray;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.yeecloud.adplus.dal.entity.Feedback;
import com.yeecloud.adplus.gateway.controller.form.FeedbackForm;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
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
        List<ScannerVO> result = scannerService.getResult(formTest);
        return Result.SUCCESS(result);
    }

    /**
     * 图像识别
     * @param form
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    @ApiDoc
    @RequestMapping("/imageScan")
    public Result<List<ScannerVO>> imageScan(@RequestBody ScannerForm form) throws IOException, ServiceException {
        try {
            String test = "";
            System.out.println(test.substring(2));
            String imgParam = URLEncoder.encode(form.getImageBase64(), "UTF-8");
            form.setImageBase64(imgParam);
            List<ScannerVO> result = scannerService.getResult(form);
            return Result.SUCCESS(result);
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

    // Detects labels in the specified local image.
//    public static void detectLabels(String filePath) throws IOException {
//        List<AnnotateImageRequest> requests = new ArrayList<>();
//
//        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
//
//        Image img = Image.newBuilder().setContent(imgBytes).build();
//        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
//        AnnotateImageRequest request =
//                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
//        requests.add(request);
//
//        // Initialize client that will be used to send requests. This client only needs to be created
//        // once, and can be reused for multiple requests. After completing all of your requests, call
//        // the "close" method on the client to safely clean up any remaining background resources.
//        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
//            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
//            List<AnnotateImageResponse> responses = response.getResponsesList();
//
//            for (AnnotateImageResponse res : responses) {
//                if (res.hasError()) {
//                    System.out.format("Error: %s%n", res.getError().getMessage());
//                    return;
//                }
//
//                // For full list of available annotations, see http://g.co/cloud/vision/docs
//                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
//                    annotation
//                            .getAllFields()
//                            .forEach((k, v) -> System.out.format("%s : %s%n", k, v.toString()));
//                }
//            }
//        }
//    }
}
