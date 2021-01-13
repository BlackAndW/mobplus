package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppProject;
import com.yeecloud.adplus.dal.entity.Device;
import com.yeecloud.adplus.dal.entity.DeviceLog;
import com.yeecloud.adplus.dal.repository.AppProjectRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.DeviceLogRepository;
import com.yeecloud.adplus.dal.repository.DeviceRepository;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.service.DeviceService;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: Huang
 * @create: 2020-12-03 14:22
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppProjectRepository appProjectRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceLogRepository deviceLogRepository;

    @Async
    @Override
    public void createOrUpdateOpenDevice(DeviceForm form) {
        try {
            if (StringUtils.isBlank(form.getAppId())) {
                return;
            }
            if (StringUtils.isBlank(form.getProjectCode())) {
                return;
            }
            App app = appRepository.findByAppId(form.getAppId());
            if (app == null || app.isDeleted()) {
                return;
            }
            if (StringUtils.isBlank(form.getUuid())) {
                return;
            }
            Device entity = deviceRepository.findFirstByUuid(form.getUuid());
            if (entity == null) {
                entity = new Device();
            }
            Integer id = entity.getId();
            NewBeanUtils.copyProperties(entity, form);

            entity.setDevType(form.isTablet() ? 2 : 1);
            entity.setScreen(form.getWidth() + "x" + form.getHeight());
            entity.setId(id);

            //新用户就解析
            DeviceLog log = new DeviceLog();
            NewBeanUtils.copyProperties(log, entity);
            log.setCreatedAt(entity.getModifiedAt());
            log.setId(null);
            if (id == null || id == 0) {
                log.setNewUser(true);
            } else {
                log.setNewUser(false);
            }
            deviceRepository.save(entity);
            deviceLogRepository.save(log);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }
}
