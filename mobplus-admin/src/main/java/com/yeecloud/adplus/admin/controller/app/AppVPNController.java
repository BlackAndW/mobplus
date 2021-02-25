package com.yeecloud.adplus.admin.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.HttpUtils;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/2/22
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/vpn")
public class AppVPNController {

    private final static String VPN_URL = "https://api.turbovpns.com";
//    private final static String VPN_URL = "http://localhost:9092";
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

    @GetMapping
    @RequiresPermissions("app:config:query")
    public Result list() throws ServiceException, IOException {
        final Request request = new Request.Builder().url(VPN_URL + "/app/api/v1/c03/list").get().build();
        Response response = buildNoVerifyClient().newCall(request).execute();
        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
        return Result.SUCCESS(getDataResult(jsonObject));
    }

    @PostMapping
    @RequiresPermissions("app:config:create")
    public Result create(@RequestBody Map<String, Object> params) throws ServiceException, IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder().url(VPN_URL + "/app/api/v1/c03/create")
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                .build();
        buildNoVerifyClient().newCall(request).execute();
//        HttpUtils.postJson(VPN_URL + "/app/api/v1/c03/create", JSONObject.toJSONString(params));
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:config:edit")
    public Result update(@PathVariable Integer id, @RequestBody Map<String, Object> params) throws ServiceException, IOException {
        System.out.println(params);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder().url(VPN_URL + "/app/api/v1/c03/update/" + id)
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                .build();
        buildNoVerifyClient().newCall(request).execute();
//        HttpUtils.postJson(VPN_URL + "/app/api/v1/c03/update/" + id, JSONObject.toJSONString(params));
        return Result.SUCCESS();
    }

    private JSONObject getDataResult(JSONObject object) {
        if (null == object) {
            return null;
        }
        System.out.println(object);
        JSONObject data = new JSONObject();
        data.put("data", object.getJSONArray("result"));
        System.out.println(data);
        return data;
    }

    private OkHttpClient buildNoVerifyClient () {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        mBuilder.sslSocketFactory(createSSLSocketFactory());
        mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        return mBuilder.build();
    }
}
