package com.yeecloud.adplus.admin.util;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.app.AppVPNController;
import com.yeecloud.adplus.admin.controller.data.form.FbAccountForm;
import com.yeecloud.meeto.common.util.PageInfo;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: Leonard
 * @create: 2021/4/28
 */
public class OkHttpUtils {

//    public final static String VPN_URL = "https://api.turbovpns.com";
    public final static String VPN_URL = "http://localhost:9092";
    public final static String FB_GRAPH_API = "https://graph.facebook.com/v10.0/";

    // 格式化result数据,将value添加到key为data中
    public static JSONObject ResponseData(Request request) throws IOException {
        Response response = OkHttpUtils.buildNoVerifyClient().newCall(request).execute();
        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
        return getDataResult(jsonObject);
    }

    // 响应转化为JSON对象
    public static JSONObject ResponseJSON(Request request) throws IOException {
        Response response = OkHttpUtils.buildNoVerifyClient().newCall(request).execute();
        return JSONObject.parseObject(response.body().string());
    }

    // 直接返回响应字符串
    public static String Response(Request request) throws IOException {
        Response response = OkHttpUtils.buildNoVerifyClient().newCall(request).execute();
        return response.body().string();
    }

    public static OkHttpClient buildNoVerifyClient() {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        mBuilder.sslSocketFactory(createSSLSocketFactory());
        mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        mBuilder.connectTimeout(120, TimeUnit.SECONDS);
        mBuilder.readTimeout(120, TimeUnit.SECONDS);
        return mBuilder.build();
    }

    private static JSONObject getDataResult(JSONObject object) {
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

    public static PageInfo dataPage(List dataList, int pageNo, int pageSize) {
        if (dataList.size() > 0) {
            int startNum = (pageNo - 1) * pageSize;
            int endNum = startNum + pageSize;
            int resultEndNum = dataList.size();

            List resultPage = new ArrayList<>();
            if (resultEndNum > 0) {
                if (resultEndNum > endNum) {
                    resultPage = dataList.subList(startNum, endNum);
                } else {
                    resultPage = dataList.subList(startNum, resultEndNum);
                }
            }
            PageInfo pageInfo = new PageInfo(pageNo, pageSize, resultEndNum, resultPage);
            return pageInfo;
        }
        return null;
    }
}
