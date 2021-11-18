package com.yeecloud.adplus.gateway.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apache.commons.beanutils.NewBeanUtils;
import com.yeecloud.adplus.dal.entity.Feedback;
import com.yeecloud.adplus.dal.repository.FeedbackRepository;
import com.yeecloud.adplus.gateway.controller.form.FeedbackForm;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
import com.yeecloud.adplus.gateway.controller.form.ScannerType;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.controller.vo.ScannerVO;
import com.yeecloud.adplus.gateway.service.ScannerService;
import com.yeecloud.adplus.gateway.service.TranslateService;
import com.yeecloud.adplus.gateway.util.OkHttpUtils;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Leonard
 * @create: 2021/8/16
 */
@Service
@Slf4j
public class ScannerServiceImpl implements ScannerService {

    @Resource
    private CacheManager cacheManager;

    @Resource
    TranslateService translateService;

    @Resource
    FeedbackRepository feedbackRepository;

    @Override
    public String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "uYH6GUrqLbmFpr4NQNTdKi80";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "VM1IKjxGCsaO4U2BbLQUZg6kzDRasEqa";
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
                String token = OkHttpUtils.ResponseJSON(request).getString("access_token");
                tokenCache.put("tokenCache", token);
                return token;
            } catch (Exception e) {
                log.info(e.getMessage());
                return "error";
            }
        }
    }

    @Override
    public List<ScannerVO> getResult(ScannerForm form) throws IOException, ServiceException {
        long startTime = System.currentTimeMillis();
        String image = "image=" + form.getImageBase64();
        String baike_num = "&baike_num=6";
        String typeName = ScannerType.getType(form.getType());
        String resUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v1/" + typeName + "?access_token=" + this.getAuth();
        if (form.getType() == 0) {
            resUrl = resUrl.replaceAll("/v1/", "/v2/");
        }
        final Request request = new Request.Builder()
                .url(resUrl)
                .post(okhttp3.RequestBody.create(MediaType.parse("Content-Type:application/x-www-form-urlencoded; charset=utf-8"), image + baike_num))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        long endTime = System.currentTimeMillis();
        log.info("百度api用时：" + (endTime - startTime));
        System.out.println(result);
        JSONArray responseArray = JSONObject.parseObject(result).getJSONArray("result");
        List<ScannerVO> vos = new ArrayList<>();

        long forStartTime = System.currentTimeMillis();
        if (responseArray != null && responseArray.size() > 0) {
            for (int i = 0;i < responseArray.size(); i++) {
                JSONObject imageInfo = responseArray.getJSONObject(i);
                ScannerVO vo = new ScannerVO();
                if (imageInfo.getBigDecimal("score").compareTo(new BigDecimal("0.05")) < 0) {
                    responseArray.remove(i--);
                    continue;
                }
                String imageName = "";
                if (form.getType() == 0) {
                    imageName = imageInfo.getString("keyword");
                } else {
                    imageName = imageInfo.getString("name");
                }
                form.setSourceString(imageName);
                String imageNameTrans = translateService.translation(form);
                vo.setName(imageNameTrans);
                vo.setScore(imageInfo.getBigDecimal("score"));
                String description = getDescription(form.getToLang(), imageNameTrans, imageInfo);
                vo.setDes(description);
                vo.setImgUrl(getImgUrl(form.getToLang(), imageNameTrans));
                vos.add(vo);
            }
        } else {
            throw new ServiceException("no result!");
        }
        long forEndTime = System.currentTimeMillis();
        log.info("loopTime: " + (forEndTime - forStartTime));
        return vos;
    }

    /***
     * 先查wiki，查不到翻译百度百科
     * @param lang
     * @param imageNameTrans
     * @param imageInfo
     * @return
     * @throws IOException
     */
    private synchronized String getDescription(String lang, String imageNameTrans, JSONObject imageInfo) throws IOException {
        long startTime = System.currentTimeMillis();
        String wikiUrl = "https://" +
                lang +
                ".wikipedia.org/w/api.php" +
                "?action=parse&format=json" +
                "&page=" +
                imageNameTrans +
                "&section=0&redirects=1&utf8=1&formatversion=2";
        final Request request = new Request.Builder()
                .url(wikiUrl)
                .get().build();
        JSONObject resultObject = OkHttpUtils.ResponseJSON(request);
        long wikiApiTime = System.currentTimeMillis();
        log.info("wikiApi: " + (wikiApiTime - startTime));
        if (resultObject.get("parse") != null) {
            String textHtml = "";
            JSONObject parse = resultObject.getJSONObject("parse");
            if (parse != null) {
                textHtml = parse.getString("text");
            }
            if (textHtml != null && textHtml.length() > 0) {
                Document document = Jsoup.parse(textHtml);
                Elements elements1 = document.select("div.mw-parser-output > p");
                String text = elements1.text();
                text = text.replaceAll("\\[\\d*?]", "");
                return text;
            }
        } else {
            String baikeDes = imageInfo.getJSONObject("baike_info").getString("description");
            if (baikeDes != null && baikeDes.length() > 0) {
                TranslateForm formDes = new TranslateForm(baikeDes, lang);
                return translateService.translation(formDes);
            }
        }
        TranslateForm form = new TranslateForm("非常抱歉，暂时无法获取此物品的信息", lang);
        return translateService.translation(form);
    }

    private synchronized String getImgUrl(String lang, String title) throws IOException {
        String wikiUrl = "https://" +
                lang +
                ".wikipedia.org/w/api.php" +
                "?action=query&format=json" +
                "&titles=" +
                title +
                "&prop=pageimages&piprop=original&utf8=1&formatversion=2";
        final Request request = new Request.Builder()
                .url(wikiUrl)
                .get().build();
        JSONObject resultObject = OkHttpUtils.ResponseJSON(request);
        if (resultObject.get("query") != null) {
            JSONObject object = resultObject.getJSONObject("query").getJSONArray("pages").getJSONObject(0);
            JSONObject org = object.getJSONObject("original");
            if (org != null) {
                return org.getString("source");
            }
        }
        return "";
    }

    @Override
    public void insertFeedbackLog(FeedbackForm form) {
        if (form != null) {
            Feedback entity = new Feedback();
            NewBeanUtils.copyProperties(entity, form, true);
            feedbackRepository.save(entity);
        }
    }
}
