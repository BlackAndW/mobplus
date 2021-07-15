package com.yeecloud.adplus.admin.service;


import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdsInsights;
import com.yeecloud.adplus.admin.controller.data.form.FbAccountForm;
import com.yeecloud.adplus.admin.controller.data.vo.FbApiVO;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/14
 */
public class FbApiService {

    /***
     * 请求图谱api
     * @param form
     * @param account
     * @return
     * @throws APIException
     */
    public static APINodeList<AdsInsights> getAdsInsights(FbAccountForm form, AdAccount account) throws APIException {
        List<String> fields = Arrays.asList("clicks", "spend", "impressions",  "actions", form.getLevel() + "_name");
        String filter = "[{field: \"action_type\",operator:\"IN\", value: ['mobile_app_install']}]";
        String date = "";
        if (form.getDateBefore().equals("0")) {
            date = "today";
        } else if (form.getDateBefore().equals("01")) {
            date = "this_month";
        } else {
            date = "last_" + form.getDateBefore() + "d";
        }
        return account.getInsights()
                .setLevel(form.getLevel())
                .setDatePreset(date)
                .setFields(fields)
                .setFiltering(filter)
                .execute();
    }

    /***
     * 数据处理
     * @param insights
     * @param form
     * @param voList
     */
    public static void setVOlist(APINodeList<AdsInsights> insights, FbAccountForm form, List<FbApiVO> voList) {
        for (AdsInsights insight : insights) {
            FbApiVO vo = new FbApiVO();
            // 计算单次成效
            double result = Double.valueOf(insight.getFieldActions().get(0).getFieldValue());
            double spend = Double.valueOf(insight.getFieldSpend());
            DecimalFormat df = new DecimalFormat("0.00");
            String cost_per_result = df.format(spend / result);
            if (form.getLevel().equals("ad")) {
                vo.setName(insight.getFieldAdName());
            } else if (form.getLevel().equals("adset")) {
                vo.setName(insight.getFieldAdsetName());
            } else if (form.getLevel().equals("campaign")) {
                vo.setName(insight.getFieldCampaignName());
            }
            vo.setResult(insight.getFieldActions().get(0).getFieldValue());
            vo.setImpressions(insight.getFieldImpressions());
            vo.setCost_per_result(cost_per_result);
            vo.setSpend("$" + insight.getFieldSpend());
            vo.setClicks(insight.getFieldClicks());
            voList.add(vo);
        }
    }
}
