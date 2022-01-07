package com.yeecloud.adplus.gateway.service;


import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVO;
import com.yeecloud.adplus.gateway.controller.vo.AppConfigVOV2;
import com.yeecloud.meeto.common.exception.ServiceException;


import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-11-30 14:49
 */
public interface AppConfigService {
    AppConfigVO getAppProjectConfigV1(DeviceForm form) throws ServiceException;

    AppConfigVOV2 getAppProjectConfigV2(DeviceForm form) throws ServiceException;

    Map<String,String > getAppConfig(DeviceForm form) throws ServiceException;

    Map<String, String> getAppFbNo(DeviceForm form) throws ServiceException;

}
