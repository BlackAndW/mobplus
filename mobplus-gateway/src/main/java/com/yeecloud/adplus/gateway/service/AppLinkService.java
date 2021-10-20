package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.vo.AppLinkVO;
import com.yeecloud.adplus.gateway.util.Result;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
public interface AppLinkService {

    AppLinkVO query(AppLinkForm form);

    Result updateData(AppLinkForm form);
}
