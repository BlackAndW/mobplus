package com.yeecloud.adplus.admin.common.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title
 *
 * Date: 2019-11-18 02:32:26
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
public class GeneralConvert {
    public Map<String, Object> str2Map(String content) {
        if (StringUtils.isNotBlank(content)) {
            try {
                return (Map) JSON.parseObject(content, HashMap.class);
            } catch (JSONException e) {
                log.error(e.getMessage(), e);
            }
        }
        return Maps.newHashMap();
    }

    public JSONObject str2Json(String content) {
        try {
            if (content != null && StringUtils.isNotBlank(content)) {
                return JSONObject.parseObject(content);
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public String json2Str(JSONObject content) {
        try {
            if (content != null) {
                content.toJSONString();
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public List<Object> json2List(String content) {
        try {
            if (content != null && StringUtils.isNotBlank(content)) {
                return JSON.parseArray(content, Object.class);
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public String list2Json(List<String> content) {
        try {
            if (content != null && !content.isEmpty()) {
                return JSON.toJSONString(content);
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
