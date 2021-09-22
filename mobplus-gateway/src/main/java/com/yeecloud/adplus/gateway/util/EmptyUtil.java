package com.yeecloud.adplus.gateway.util;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/8/31
 */
public class EmptyUtil {

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }
}