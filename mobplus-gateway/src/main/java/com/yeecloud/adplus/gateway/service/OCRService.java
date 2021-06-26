package com.yeecloud.adplus.gateway.service;

import java.io.IOException;

/**
 * @author: Leonard
 * @create: 2021/6/25
 */
public interface OCRService {

    String detectText(String imagePath, String isWeb) throws IOException;

    String detectDocumentText(String imagePath, String isWeb) throws IOException;
}
