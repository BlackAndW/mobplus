package com.yeecloud.adplus.admin.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.admin.controller.app.vo.AppSettingVO;
import com.yeecloud.adplus.admin.util.OkHttpUtils;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.repository.AppRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    AppRepository appRepository;

    @GetMapping("/{id}")
    @RequiresPermissions("app:vpn:query")
    public Result serverList(@PathVariable Integer id, @RequestParam Integer type) throws ServiceException, IOException {
        String pkgName = getPkgNameById(id);
        final Request request = new Request.Builder()
                .url(OkHttpUtils.VPN_URL + "/app/api/v1/c04/list?type=" + type + "&pkgName=" + pkgName)
                .get().build();
        return Result.SUCCESS(OkHttpUtils.ResponseData(request));
    }

    @GetMapping("/s/{id}")
    @RequiresPermissions("app:vpn:query")
    public Result serverSettingList(@PathVariable Integer id) throws ServiceException, IOException {
        String pkgName = getPkgNameById(id);
        final Request request = new Request.Builder()
                .url(OkHttpUtils.VPN_URL + "/app/api/v1/c04/s/list?&pkgName=" + pkgName)
                .get().build();
        JSONArray response = JSON.parseArray(OkHttpUtils.ResponseJSON(request).getString("result"));
        AppSettingVO vo = new AppSettingVO();
        for (int i = 0; i < response.size(); i++) {
            JSONObject server = response.getJSONObject(i);
            if (server.getString("type").equals("0")) {
                vo.getServerNMList().add(server);
            } else if (server.getString("type").equals("1")) {
                vo.getServerVIPList().add(server);
            } else if (server.getString("type").equals("2")) {
                vo.getServerBKList().add(server);
            }
        }
        return Result.SUCCESS(vo);
    }

    @PostMapping
    @RequiresPermissions("app:vpn:create")
    public Result create(@RequestBody Map<String, Object> params) throws ServiceException, IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder()
                .url(OkHttpUtils.VPN_URL + "/app/api/v1/c04/create")
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        if (Integer.valueOf(JSON.parseObject(result).get("code").toString()) != 2000) {
            return Result.FAILURE(result);
        }
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:vpn:edit")
    public Result update(@PathVariable Integer id, @RequestBody Map<String, Object> params) throws ServiceException, IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder()
                .url(OkHttpUtils.VPN_URL + "/app/api/v1/c04/update/" + id)
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                .build();
        String result = OkHttpUtils.buildNoVerifyClient().newCall(request).execute().body().string();
        if (Integer.valueOf(JSON.parseObject(result).get("code").toString()) != 2000) {
            return Result.FAILURE(result);
        }
        return Result.SUCCESS();
    }

    @GetMapping("setting/{id}")
    @RequiresPermissions("app:vpn:query")
    public Result serverAppConf(@PathVariable Integer id) throws ServiceException, IOException {
        String pkgName = getPkgNameById(id);
        final Request request = new Request.Builder()
                .url(OkHttpUtils.VPN_URL + "/app/api/v1/c04/setting?pkgName=" + pkgName)
                .get()
                .build();
        return Result.SUCCESS(OkHttpUtils.ResponseJSON(request));
    }

    @PutMapping("setting/{id}")
    @RequiresPermissions("app:vpn:edit")
    public Result updateAppConf(@PathVariable Integer id, @RequestBody Map<String, Object> params) throws ServiceException, IOException {
        App app = appRepository.findById(id).orElse(null);
        String pkgName = "";
        if (app != null) {
            pkgName = app.getPkgName();
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder()
                .url(OkHttpUtils.VPN_URL + "/app/api/v1/c04/setting/update?pkgName=" + pkgName)
                .post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(params)))
                .build();
        OkHttpUtils.Response(request);
        return Result.SUCCESS();
    }

    private String getPkgNameById(Integer id) {
        App app = appRepository.findById(id).orElse(null);
        String pkgName = "";
        if (app != null) {
            pkgName = app.getPkgName();
        }
        return pkgName;
    }
}
