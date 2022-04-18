package com.yeecloud.adplus.gateway.service.impl;

import java.util.HashMap;
import java.util.Map;
import com.yeecloud.adplus.gateway.util.HttpRequest;
import com.yeecloud.adplus.gateway.util.MD5;

public class BaiduTransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/fieldtranslate";

    private String appId;
    private String securityKey;

    public BaiduTransApi(String appid, String securityKey) {
        this.appId = appid;
        this.securityKey = securityKey;
    }

//    public String getTransResultByGET(String query, String from, String to, String domain) {
//        Map<String, String> params = buildParams(query, from, to, domain);
//        return HttpRequest.get(TRANS_API_HOST, params);
//    }

    public String getTransResultByPOST(String query, String from, String to, String domain) throws Exception {
        Map<String, String> params = buildParams(query, from, to, domain);
        return HttpRequest.post(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to, String domain) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("domain", domain);

        params.put("appid", appId);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appId + query + salt + domain +  securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
