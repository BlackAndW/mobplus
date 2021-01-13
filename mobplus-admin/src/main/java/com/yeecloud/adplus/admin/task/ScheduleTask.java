package com.yeecloud.adplus.admin.task;

import com.yeecloud.adplus.admin.service.SysConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Title
 * Description:
 * Date: 2019/3/14 15:44
 * Copyright (c) 2019-2099 YYSKYS YeeCloud
 *
 * @author ybbk
 */
@Component
public class ScheduleTask {

    @Autowired
    private SysConfService sysConfService;

    @Scheduled(fixedDelay = 10000)
    public void refreshConfiguration() {
        sysConfService.refreshAsync();
    }
}