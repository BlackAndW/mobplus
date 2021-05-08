package com.yeecloud.adplus.admin.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.HttpUtils;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/2/22
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/app/vpn")
public class AppVPNController {

    @GetMapping
    @RequiresPermissions("app:config:query")
    public Result serverList(@RequestParam Integer type) throws ServiceException, IOException {
        final Request request = new Request.Builder().url(OkHttpUtils.VPN_URL + "/app/api/v1/c03/list?type=" + type).get().build();
        return Result.SUCCESS(OkHttpUtils.getGETResponseData(request));
    }

    @PostMapping
    @RequiresPermissions("app:config:create")
    public Result create(@RequestBody Map<String, Object> params) throws ServiceException, IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder().url(OkHttpUtils.VPN_URL + "/app/api/v1/c03/create")
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        if (Integer.valueOf(JSON.parseObject(result).get("code").toString()) != 2000) {
            return Result.FAILURE(result);
        }
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:config:edit")
    public Result update(@PathVariable Integer id, @RequestBody Map<String, Object> params) throws ServiceException, IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder().url(OkHttpUtils.VPN_URL + "/app/api/v1/c03/update/" + id)
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        if (Integer.valueOf(JSON.parseObject(result).get("code").toString()) != 2000) {
            return Result.FAILURE(result);
        }
        return Result.SUCCESS();
    }

}
