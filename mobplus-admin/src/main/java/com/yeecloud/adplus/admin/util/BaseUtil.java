package com.yeecloud.adplus.admin.util;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/3/2
 */
public class BaseUtil {

    /**
     *
     * @param list List<String>
     * @return 默认用"|"隔开
     */
    public static String formatList2String (List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(channel -> {
            if (list.indexOf(channel) != list.size() - 1) {
                stringBuilder.append(channel).append("|");
            } else {
                stringBuilder.append(channel);
            }
        });
        return stringBuilder.toString();
    }

}
