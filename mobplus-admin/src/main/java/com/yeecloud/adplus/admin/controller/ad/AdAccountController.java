package com.yeecloud.adplus.admin.controller.ad;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.ad.convert.AdAccountConvert;
import com.yeecloud.adplus.admin.controller.ad.form.AdAccountForm;
import com.yeecloud.adplus.admin.controller.ad.vo.AdAccountVO;
import com.yeecloud.adplus.admin.service.AdAccountService;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.AdAccount;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/ad/account")
public class AdAccountController {

    @Resource
    AdAccountService adAccountService;

    @Resource
    AdAccountConvert adAccountConvert;

    @GetMapping("list")
    public Result getAccountList() {
        Page<AdAccount> adAccounts = adAccountService.queryList();
        List<AdAccountVO> resultList = adAccountConvert.convert(adAccounts.getContent());
        PageInfo<AdAccountVO> result = new PageInfo<>(adAccounts.getNumber() + 1, adAccounts.getSize(), (int) adAccounts.getTotalElements(), resultList);
        return Result.SUCCESS(result);
    }

    @PostMapping("/report")
    public Result getReportData(@RequestBody AdAccountForm form) throws IOException {
        System.out.println(form);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder().url(form.getDomain() + "/data/admob/reportInfo")
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(form)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        if (Integer.valueOf(JSON.parseObject(result).get("code").toString()) != 2000) {
            return Result.FAILURE(result);
        }
        JSONArray resultArray = JSON.parseObject(result).getJSONArray("result");
        // 去除header和footer数据列
        List dataArray = resultArray.subList(1, resultArray.size() - 1);
        PageInfo pageInfo = adAccountService.dataPage(dataArray, form);
        return Result.SUCCESS(pageInfo);
    }

}
