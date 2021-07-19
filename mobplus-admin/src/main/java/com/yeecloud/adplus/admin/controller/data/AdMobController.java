package com.yeecloud.adplus.admin.controller.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.data.convert.AdMobConvert;
import com.yeecloud.adplus.admin.controller.data.form.AdMobForm;
import com.yeecloud.adplus.admin.controller.ad.vo.AdAccountVO;
import com.yeecloud.adplus.admin.service.AdAccountService;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.AdAccount;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/ad/account")
public class AdMobController {

    @Resource
    AdAccountService adAccountService;

    @Resource
    AdMobConvert adMobConvert;

    @GetMapping("list")
    public Result getAccountList() {
        Page<AdAccount> adAccounts = adAccountService.queryList();
        List<AdAccountVO> resultList = adMobConvert.convert(adAccounts.getContent());
        PageInfo<AdAccountVO> result = new PageInfo<>(adAccounts.getNumber() + 1, adAccounts.getSize(), (int) adAccounts.getTotalElements(), resultList);
        return Result.SUCCESS(result);
    }

    @PostMapping("/report")
    public Result getReportData(@RequestBody AdMobForm form) throws IOException {
        delMtcByDms(form);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder().url(form.getDomain() + "/data/admob/reportInfo")
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(form)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        if (Integer.valueOf(JSON.parseObject(result).get("code").toString()) != 2000) {
            return Result.FAILURE(JSON.parseObject(result).get("message").toString());
        }
        JSONArray resultArray = JSON.parseObject(result).getJSONArray("result");
        // 去除header和footer数据列
        List dataArray = resultArray.subList(1, resultArray.size() - 1);
        PageInfo pageInfo = adAccountService.dataPage(dataArray, form);
        return Result.SUCCESS(pageInfo);
    }

    private void delMtcByDms(AdMobForm form) {
        form.getDimensions().forEach( dimension -> {
            if (dimension.equals("AD_TYPE")) {
                form.getMetrics().remove("AD_REQUESTS");
                form.getMetrics().remove("MATCH_RATE");
                form.getMetrics().remove("IMPRESSION_RPM");
            }
        });
    }
}