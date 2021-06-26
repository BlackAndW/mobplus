package com.yeecloud.adplus.gateway.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
@Slf4j
public class GoogleApiUtil {

    public static GoogleCredentials getCredential(File credential) {
        GoogleCredentials credentials = null;

        try (FileInputStream serviceAccountStream = new FileInputStream(credential))
        {
            return ServiceAccountCredentials.fromStream(serviceAccountStream);
        } catch (IOException e) {
            log.error("get GoogleCredentials failed: ", e);
        }
        return null;
    }
}
