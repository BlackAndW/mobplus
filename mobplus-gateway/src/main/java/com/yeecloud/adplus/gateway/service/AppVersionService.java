package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.AppVersionForm;
import com.yeecloud.adplus.gateway.controller.vo.AppVersionVO;

/**
 * @author: Leonard
 * @create: 2021/11/29
 */
public interface AppVersionService {
    AppVersionVO detectVersion(AppVersionForm form);
}
