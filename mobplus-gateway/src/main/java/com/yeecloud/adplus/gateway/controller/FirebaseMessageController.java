package com.yeecloud.adplus.gateway.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 此部分暂时用不到
 * @author: Leonard
 * @create: 2021/7/30
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/firebase/msg")
public class FirebaseMessageController {

    @RequestMapping("send")
    public void sendMsg() throws FirebaseMessagingException, IOException {

        InputStream serviceAccount = this.getClass().getClassLoader().getResourceAsStream("cleaner.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp app;
        if (FirebaseApp.getApps().size() == 0) {
            app = FirebaseApp.initializeApp(options);
        } else {
            app = FirebaseApp.getApps().get(0);
        }


        List<String> registrationTokens = Arrays.asList(
                "dJz2OIUdT8ympMez0g_ruK:APA91bGPZuaa4XSMHAwQsL_xmN9eJmmedugcV56n7LyF7F8h0iDhZ4wtptdPSpKeVQHGYMVEDP1_Rcts32LzRepFGzMpqusayCsIoaKa88uRZdWpnEREL9bgK-nTXRLyqxvYXkwPsCkT"
        );

        MulticastMessage message = MulticastMessage.builder()
                .setAndroidConfig(AndroidConfig.builder()
                        .setNotification(AndroidNotification.builder()
                                .setClickAction("abc")
                                .setTitle("aha")
                                .setBody("oho")
                                .build())
                        .build())
                .putData("score", "850")
                .putData("time", "2:45")
                .addAllTokens(registrationTokens)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance(app).sendMulticast(message);

        System.out.println(response.getSuccessCount() + " messages were sent successfully");
    }
}
