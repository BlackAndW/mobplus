package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.gateway.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/3/24
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/virus")
public class AppVirusScan {

    private static final String VIRUS_FILE_PATH = "D://3-Docs//virus_pkg.json";

    @PostMapping("scan")
    public Result getVirusPkg() throws IOException {
        String virus_pkg = FileUtils.readFileToString(new File(VIRUS_FILE_PATH), StandardCharsets.UTF_8);
        JSONObject obj = JSON.parseObject(virus_pkg);
        JSONArray array = obj.getJSONArray("pkg_list");
        List<String> list = new ArrayList<>();
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String pkgName = object.getString("name");
            list.add(pkgName);
        }
        return Result.SUCCESS(list);
    }
}
