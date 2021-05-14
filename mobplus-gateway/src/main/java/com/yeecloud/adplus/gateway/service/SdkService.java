package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.SdkCfgVO;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * @author: Huang
 * @create: 2020-12-09 14:18
 */
public interface SdkService {

    SdkCfgVO getSdkCfg(DeviceForm form)throws ServiceException;

    SdkCfgVO getSdkCfgEn(DeviceForm form)throws ServiceException;

    SdkCfgVO getPosList(DeviceForm form)throws ServiceException;
}
