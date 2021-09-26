package com.yeecloud.adplus.admin.controller.data;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/9/26
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data/gamezop")
public class GamezopController {

    @GetMapping
    public Result getApiResult(@RequestParam Map<String, Object> params) throws IOException {
        int pageNo = Integer.valueOf(params.get("pageNo").toString());
        int pageSize = Integer.valueOf(params.get("pageSize").toString());

        JSONObject apiForm = new JSONObject();
        if (params.get("startDate") != null && params.get("endDate") != null) {
            apiForm.put("start_date", params.get("startDate").toString());
            apiForm.put("end_date", params.get("endDate").toString());
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            apiForm.put("start_date", simpleDateFormat.format(new Date()));
            apiForm.put("end_date", simpleDateFormat.format(new Date()));
        }
        apiForm.put("report_config", "{\"dimensions\": {\"ad_unit\": true,\"country\": true,\"date\": true},\"metrics\": {\"total_impressions\": true,\"total_average_ecpm_usd\": true,\"total_clicks\": true,\"total_average_ctr\": true,\"total_revenue_usd\": true,\"partner_revenue_usd\": true}}");
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder()
                .addHeader("id", "WYe3c14jd")
                .addHeader("auth_token", "seDQGhXmJZbzM9gsCevJF4XPeqxMqjWM")
                .url("https://arnab.gamezop.com/revenue/v2/reports")
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(apiForm)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.get("statusCode") != null && Integer.valueOf(jsonObject.get("statusCode").toString()) != 200) {
            return Result.FAILURE("请求失败");
        }
        List resultList = jsonObject.getJSONArray("report");
        return Result.SUCCESS(OkHttpUtils.dataPage(resultList, pageNo, pageSize));
    }
}
