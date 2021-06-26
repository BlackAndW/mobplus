package com.yeecloud.adplus.gateway.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.yeecloud.adplus.gateway.service.OCRService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/25
 */
@Service
public class OCRServiceImpl implements OCRService {

    @Value("${google.apiKeyPath.ocr}")
    private String apiKeyPath;

    @Override
    public String detectText(String imagePath, String isWeb) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ImageAnnotatorSettings settings = preProcess(imagePath, isWeb, Feature.Type.TEXT_DETECTION, requests);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
//                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return res.getError().getMessage();
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
//                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//                    System.out.format("Text: %s%n", annotation.getDescription());
//                }
//                System.out.println(res.getFullTextAnnotation().getText());
                return res.getFullTextAnnotation().getText();
            }
        }
        return "";
    }

    @Override
    public String detectDocumentText(String imagePath, String isWeb) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ImageAnnotatorSettings settings = preProcess(imagePath, isWeb, Feature.Type.DOCUMENT_TEXT_DETECTION, requests);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
//                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return res.getError().getMessage();
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
//                for (Page page : annotation.getPagesList()) {
//                    StringBuilder pageText = new StringBuilder();
//                    for (Block block : page.getBlocksList()) {
//                        StringBuilder blockText = new StringBuilder();
//                        for (Paragraph para : block.getParagraphsList()) {
//                            String paraText = "";
//                            for (Word word : para.getWordsList()) {
//                                StringBuilder wordText = new StringBuilder();
//                                for (Symbol symbol : word.getSymbolsList()) {
//                                    wordText.append(symbol.getText());
////                                    System.out.format(
////                                            "Symbol text: %s (confidence: %f)%n",
////                                            symbol.getText(), symbol.getConfidence());
//                                }
////                                System.out.format(
////                                        "Word text: %s (confidence: %f)%n%n", wordText.toString(), word.getConfidence());
//                                paraText = String.format("%s %s", paraText, wordText.toString());
//                            }
//                            // Output Example using Paragraph:
//                            System.out.println("%nParagraph: %n" + paraText);
////                            System.out.format("Paragraph Confidence: %f%n", para.getConfidence());
//                            blockText.append(paraText);
////                            System.out.println(blockText);
//                        }
//                        pageText.append(blockText);
//                        System.out.println(pageText);
//                    }
//                }
                return annotation.getText();
            }
        }
        return "";
    }

    private ImageAnnotatorSettings preProcess(String filePath,
                                             String isWeb,
                                             Feature.Type type,
                                             List<AnnotateImageRequest> requests) throws IOException {
        Image image;
        if (isWeb.equals("web")) {
            ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(filePath).build();
            image = Image.newBuilder().setSource(imgSource).build();
        } else {
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
            image = Image.newBuilder().setContent(imgBytes).build();
        }

        Feature feat = Feature.newBuilder().setType(type).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(image).build();
        requests.add(request);

        File credentialsPath = new File(apiKeyPath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        return settings;
    }
}
