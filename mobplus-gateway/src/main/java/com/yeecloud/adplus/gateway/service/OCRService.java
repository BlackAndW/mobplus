package com.yeecloud.adplus.gateway.service;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/25
 */
public interface OCRService {

    String detectText(String imagePath, String isWeb) throws IOException;

    String detectDocumentText(String imagePath, String isWeb) throws IOException;
}
