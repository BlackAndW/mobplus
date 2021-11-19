package com.yeecloud.adplus.admin.util;

import com.yeecloud.meeto.common.util.Query;
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

    public static List getPageList (Query query, List resultList) {
        int startIndex = (query.getPageNo() - 1) * query.getPageSize();
        int endIndex = query.getPageNo() * query.getPageSize();
        if (endIndex > resultList.size()) endIndex = resultList.size();
        return resultList.subList(startIndex, endIndex);
    }
}
