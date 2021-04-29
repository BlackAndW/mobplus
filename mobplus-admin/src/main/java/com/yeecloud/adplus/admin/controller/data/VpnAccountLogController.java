package com.yeecloud.adplus.admin.controller.data;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.yeecloud.adplus.admin.service.AppService;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/4/28
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data/vpn")
public class VpnAccountLogController {

    @Autowired
    private AppService appService;

    @GetMapping
    @RequiresPermissions("dataManager:query")
    public Result getAccountLog(@RequestParam Map<String, Object> params) throws ServiceException, IOException {
        System.out.println(params);
        HttpUrl.Builder httpBuilder = HttpUrl.parse(OkHttpUtils.VPN_URL + "/app/api/v1/vpn/list").newBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            // 根据字段id查找对应的包名
            if (param.getKey().equals("appId")) {
                App app = appService.findById(Integer.valueOf(param.getValue().toString()));
                param.setValue(app.getPkgName());
            }
            httpBuilder.addQueryParameter(param.getKey(), String.valueOf(param.getValue()));
        }
        final Request request = new Request.Builder().url(httpBuilder.build()).get().build();
        return Result.SUCCESS(OkHttpUtils.getGETResponse(request));
    }
}
