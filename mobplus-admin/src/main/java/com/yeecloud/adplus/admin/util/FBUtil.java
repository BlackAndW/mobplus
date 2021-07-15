package com.yeecloud.adplus.admin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: Leonard
 * @create: 2021/7/8
 */
public class FBUtil {

    public static String getToken() {
        String path = FBUtil.class.getClassLoader().getResource("fbAccessToken.txt").getPath();
        File file = new File(path);
        String access_token = "";
        if (file.exists()) {
            try(BufferedReader br = new BufferedReader(new FileReader(path))) {
                access_token = br.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return access_token;
    }
}
