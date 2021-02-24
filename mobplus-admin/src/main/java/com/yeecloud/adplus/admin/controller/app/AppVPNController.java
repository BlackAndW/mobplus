package com.yeecloud.adplus.admin.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.admin.controller.app.vo.AppActivityVO;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.HttpUtils;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    private final static String VPN_URL = "http://localhost:9092";

    @GetMapping
    @RequiresPermissions("app:config:query")
    public Result list() throws ServiceException {
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.get(VPN_URL + "/app/api/v1/c03/list"));
        return Result.SUCCESS(getDataResult(jsonObject));
    }

    @PostMapping
    @RequiresPermissions("app:config:create")
    public Result create(@RequestBody Map<String, Object> params) throws ServiceException {
        HttpUtils.postJson(VPN_URL + "/app/api/v1/c03/create", JSONObject.toJSONString(params));
        return Result.SUCCESS();
    }

    @PutMapping("/{id}")
    @RequiresPermissions("app:config:edit")
    public Result update(@PathVariable Integer id, @RequestBody Map<String, Object> params) throws ServiceException {
        HttpUtils.postJson(VPN_URL + "/app/api/v1/c03/update/" + id, JSONObject.toJSONString(params));
        return Result.SUCCESS();
    }

    private JSONObject getDataResult(JSONObject object) {
        if (null == object) {
            return null;
        }
        System.out.println(object);
        JSONObject data = new JSONObject();
        data.put("data", object.getJSONArray("result"));
        System.out.println(data);
        return data;
    }
}
