package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.gateway.controller.form.DeviceForm;

/**
 * @author: Huang
 * @create: 2020-12-03 14:22
 */
public interface DeviceService {
    void createOrUpdateOpenDevice(DeviceForm form);
}
