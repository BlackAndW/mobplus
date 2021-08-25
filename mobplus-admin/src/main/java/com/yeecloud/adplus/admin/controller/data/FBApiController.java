package com.yeecloud.adplus.admin.controller.data;

import com.alibaba.fastjson.JSONArray;
import com.facebook.ads.sdk.*;
import com.yeecloud.adplus.admin.controller.data.form.FbAccountForm;
import com.yeecloud.adplus.admin.controller.data.vo.FbApiVO;
import com.yeecloud.adplus.admin.service.AdAccountService;
import com.yeecloud.adplus.admin.service.FbApiService;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/5/25
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data/fb")
public class FBApiController {

    @Value("${fb.token}")
    String ACCESS_TOKEN;

    @Value("${fb.account_id}")
    String ACCOUNT_ID;

    @Value("${fb.app_secret}")
    String APP_SECRET;

    @Resource
    AdAccountService adAccountService;

    @RequestMapping("list")
    public Result list() throws IOException {
        final long ACCOUNT_ID_L = Long.valueOf(ACCOUNT_ID);
        HttpUrl.Builder httpBuilder = HttpUrl.parse(OkHttpUtils.FB_GRAPH_API + ACCOUNT_ID_L + "/adaccounts").newBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", ACCESS_TOKEN);
        params.put("fields", "id,name,account_status");
        final Request request = getRequest(httpBuilder, params);
        JSONArray array = OkHttpUtils.ResponseJSON(request).getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            if (array.getJSONObject(0).get("name").equals("吴子仲")) {
                array.remove(i);
                break;
            }
        }
        return Result.SUCCESS(array);
    }

    @RequestMapping("info")
    public Result getInfo(@RequestBody @Valid FbAccountForm form, BindingResult bindingResult) throws APIException {
        if (bindingResult.hasErrors()){
            return Result.FAILURE(String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage()));
        }
        final APIContext context = new APIContext(ACCESS_TOKEN, APP_SECRET).enableDebug(false);
        AdAccount account = new AdAccount(form.getAccountId(), context);
        APINodeList<AdsInsights> insights = FbApiService.getAdsInsights(form, account);

        List<FbApiVO> voList = new ArrayList<>();
        FbApiService.setVOlist(insights, form, voList);
        if (voList.size() == 0) return Result.FAILURE("sorry, data is empty!");
        PageInfo pageInfo = adAccountService.dataFBPage(voList, form);
        return Result.SUCCESS(pageInfo);
    }

    private Request getRequest(HttpUrl.Builder httpBuilder, Map<String, Object> params) {
        for (Map.Entry<String, Object> param : params.entrySet()) {
            httpBuilder.addQueryParameter(param.getKey(), String.valueOf(param.getValue()));
        }
        return new Request.Builder().url(httpBuilder.build()).get().build();
    }
}
