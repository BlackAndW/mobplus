package com.yeecloud.adplus.admin.util;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.app.AppVPNController;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author: Leonard
 * @create: 2021/4/28
 */
public class OkHttpUtils {

    public final static String VPN_URL = "https://api.turbovpns.com";
//    public final static String VPN_URL = "http://localhost:9092";

    // 格式化result数据
    public static JSONObject getGETResponseData(Request request) throws IOException {
        Response response = OkHttpUtils.buildNoVerifyClient().newCall(request).execute();
        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
        return getDataResult(jsonObject);
    }

    // 响应转化为JSON对象
    public static JSONObject getGETResponseJSON(Request request) throws IOException {
        Response response = OkHttpUtils.buildNoVerifyClient().newCall(request).execute();
        return JSONObject.parseObject(response.body().string());
    }

    // 直接返回响应字符串
    public static String getGETResponse(Request request) throws IOException {
        Response response = OkHttpUtils.buildNoVerifyClient().newCall(request).execute();
        return response.body().string();
    }

    public static OkHttpClient buildNoVerifyClient() {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        mBuilder.sslSocketFactory(createSSLSocketFactory());
        mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        return mBuilder.build();
    }

    public static JSONObject getDataResult(JSONObject object) {
        if (null == object) {
            return null;
        }
        JSONObject data = new JSONObject();
        data.put("data", object.getJSONArray("result"));
        return data;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
