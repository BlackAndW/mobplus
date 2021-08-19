package com.yeecloud.adplus.gateway.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
import com.yeecloud.adplus.gateway.controller.form.ScannerType;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.service.ScannerRequestService;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.adplus.gateway.util.OkHttpUtils;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Leonard
 * @create: 2021/8/16
 */
@Service
@Slf4j
public class ScannerRequestServiceImpl implements ScannerRequestService {

    @Resource
    private CacheManager cacheManager;

    @Resource
    TranslateService translateService;

    @Override
    public String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "Y9Cc4GciVkPYiPkbWoscuqL9";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "uaxlhhescuQs6GzOGuUmaAgbCL66DSYi";
        return getAuth(clientId, clientSecret);
    }

    private String getAuth(String ak, String sk) {
        Cache tokenCache = cacheManager.getCache("tokenCache");
        if (tokenCache.get("tokenCache") != null) {
            return (String) tokenCache.get("tokenCache").get();
        } else {
            // 获取token地址
            String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
            String getAccessTokenUrl = authHost
                    // 1. grant_type为固定参数
                    + "grant_type=client_credentials"
                    // 2. 官网获取的 API Key
                    + "&client_id=" + ak
                    // 3. 官网获取的 Secret Key
                    + "&client_secret=" + sk;
            try {
                final Request request = new Request.Builder()
                        .url(getAccessTokenUrl)
                        .get().build();
                String token = OkHttpUtils.getGETResponseJSON(request).getString("access_token");
                tokenCache.put("tokenCache", token);
                return token;
            } catch (Exception e) {
                log.info(e.getMessage());
                return "error";
            }
        }
    }

    @Override
    public JSONArray resultArray(ScannerForm form) throws IOException, ServiceException {
        String image = "image=" + form.getImageBase64();
        String topNum = "&top_num=3";
        String typeName = ScannerType.getType(form.getType());
        System.out.println(form.toString());
        String resUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v1/" + typeName + "?access_token=" + this.getAuth();
        if (form.getType() == 0) {
            resUrl = resUrl.replaceAll("v1", "v2");
        }
        final Request request = new Request.Builder()
                .url(resUrl)
                .post(okhttp3.RequestBody.create(MediaType.parse("Content-Type:application/x-www-form-urlencoded; charset=utf-8"), image + topNum))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        System.out.println(result);
        JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("result");
        if (resultArray != null && resultArray.size() > 0) {
            for (int i = 0;i < resultArray.size(); i++) {
                JSONObject imageInfo = resultArray.getJSONObject(i);
                String imageName = "";
                if (form.getType() == 0) {
                    imageName = imageInfo.getString("keyword");
                } else {
                    imageName = imageInfo.getString("name");
                }
                form.setSourceString(imageName);
                String imageNameTrans = translateName(form);
                imageInfo.put("name", imageNameTrans);
                String description = getWikiInfo(form.getToLang(), imageNameTrans);
                description = processWikiText(description);
                imageInfo.put("des", description);
            }
        } else {
            throw new ServiceException("no result!");
        }
        return resultArray;
    }

    private String getWikiInfo(String lang, String imageNameTrans) throws IOException {
        String wikiUrl = "https://" +
                lang +
                ".wikipedia.org/w/api.php" +
                "?action=query&format=json" +
                "&prop=revisions&rvprop=content&rvslots=main&rvsection=0" +
                "&titles=" +
                imageNameTrans +
                "&redirects=1&utf8=1&formatversion=2";
        final Request request = new Request.Builder()
                .url(wikiUrl)
                .get().build();
        JSONObject resultObject = OkHttpUtils.getGETResponseJSON(request);
        if (resultObject != null) {
            String text = "";
            JSONArray revisions = resultObject.getJSONObject("query")
                    .getJSONArray("pages")
                    .getJSONObject(0)
                    .getJSONArray("revisions");
            if (revisions != null && revisions.size() > 0) {
                text = revisions.getJSONObject(0)
                        .getJSONObject("slots")
                        .getJSONObject("main")
                        .getString("content");
            }
            if (text != null && text.length() > 0) {
                return text;
            }
        }
        return "sorry, there is no info about this item!";
    }

    private String processWikiText(String text) {
        String regex = "'''|(<ref.*?</ref>)|\\[|]|.*?(?='''.*''')|\\{\\{.*?}}|「|」";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        String text2 = StringUtils.trim(matcher.replaceAll(""));
        return text2.replaceAll("\\|", ")");
    }

    @Async
    synchronized String translateName(ScannerForm form) {
        return translateService.translation(form);
    }
}
