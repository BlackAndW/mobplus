package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.AppPosition;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppPosCfgVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import org.apache.commons.collections.map.HashedMap;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/1/27
 */
public interface AppPositionService {

    List getAppPositionConfList(DeviceForm form) throws ServiceException;
}
