package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.vo.AppLinkVO;
import com.yeecloud.meeto.common.result.Result;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
public interface AppLinkService {

    AppLinkVO query(AppLinkForm form);

    Result updateData(AppLinkForm form);
}
