package com.yeecloud.adplus.gateway.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.service.impl.BaiduTransApi;
import com.yeecloud.meeto.common.exception.ServiceException;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: Leonard
 * @create: 2022/4/12
 */
public class BaiduTransUtil {

    private static String appId = "20220401001153477";

    private static String securityKey = "e4ZYoLIDAeEzo7N5No6E";

    public static String translateByDomain(TranslateForm form, String domain) throws Exception {
        BaiduTransApi baiduTransApi = new BaiduTransApi(appId, securityKey);
        String result = baiduTransApi.getTransResultByPOST(form.getSourceString(),
                "zh", form.getToLang(), domain);
        JSONObject object = JSON.parseObject(result);
        if (object == null) {
            throw new ServiceException("百度翻译请求报错");
        }
        JSONArray trans_result = object.getJSONArray("trans_result");
        StringBuilder resultStr = new StringBuilder();
        if (trans_result != null && trans_result.size() > 0) {
            for (int i = 0; i < trans_result.size(); i++) {
                resultStr.append(trans_result.getJSONObject(i).getString("dst"));
                resultStr.append("\r\n");
            }
        }
        return resultStr.toString();
    }

    public static String Hanzi2Pinyin(String Hanzi) {
        char[] p1 = Hanzi.toCharArray();
        HanyuPinyinOutputFormat p3 = new HanyuPinyinOutputFormat();
        p3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        p3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        p3.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder p4 = new StringBuilder();
        int t0 = p1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(p1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    String[] p2 = PinyinHelper.toHanyuPinyinStringArray(p1[i], p3);
                    p4.append(p2[0]);
                } else
                    p4.append(p1[i]);
            }
            return p4.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return p4.toString();
    }
}
