package com.yeecloud.adplus.gateway.service;

import com.alibaba.fastjson.JSONArray;
import com.yeecloud.adplus.dal.entity.Feedback;
import com.yeecloud.adplus.gateway.controller.form.ScannerForm;
import com.yeecloud.adplus.gateway.controller.vo.ScannerVO;
import com.yeecloud.meeto.common.exception.ServiceException;

import java.io.IOException;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/8/16
 */

public interface ScannerService {

    String getAuth();

    List<ScannerVO> getResult(ScannerForm form) throws IOException, ServiceException;

    void insertFeedbackLog(Feedback form);
}
