package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.AppActivity;

import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AppActivityVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author: Leonard
 * @create: 2021/1/20
 */
public interface AppActivityService {

    List<AppActivityVO> getAppActivityList(DeviceForm form) throws ServiceException;

}
