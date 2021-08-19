package com.yeecloud.adplus.gateway.service;

import com.alibaba.fastjson.JSONArray;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
import com.yeecloud.meeto.common.exception.ServiceException;

import java.io.IOException;

/**
 * @author: Leonard
 * @create: 2021/8/16
 */

public interface ScannerRequestService {

    String getAuth();

    JSONArray resultArray(ScannerForm form) throws IOException, ServiceException;
}
