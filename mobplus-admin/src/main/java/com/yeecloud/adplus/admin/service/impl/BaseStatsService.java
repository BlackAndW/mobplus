package com.yeecloud.adplus.admin.service.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yeecloud.meeto.common.util.Query;

import java.math.BigInteger;
import java.util.*;

/**
 * Title
 *
 * Date: 2019-12-31 19:06:33
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public class BaseStatsService {
    protected Long getParams(Query query, String key, Long defValue) {
        Long value = query.get(key, Long.class);
        if (value != null) {
            return value;
        }
        return defValue;
    }

    protected void process(List<Map<String, Object>> list, Long startAt, Long endAt, Long step, String key, Map<String, Object> def) {
        Set<Object> keySets = Sets.newHashSet();
        list.stream().forEach(ele -> {
            keySets.add(ele.get(key));
        });
        for (long start = startAt; start < endAt; start += step) {
            BigInteger date = BigInteger.valueOf(start);
            if (!keySets.contains(date)) {
                Map<String, Object> point = Maps.newHashMap(def);
                point.put(key, date);
                list.add(point);
            }
        }
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                double v1 = ((Number) o1.get(key)).doubleValue();
                double v2 = ((Number) o2.get(key)).doubleValue();
                if (v1 > v2) {
                    return 1;
                } else if (v1 == v2) {
                    return 0;
                }
                return -1;
            }
        });
    }
}
