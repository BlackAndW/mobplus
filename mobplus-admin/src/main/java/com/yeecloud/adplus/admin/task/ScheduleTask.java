package com.yeecloud.adplus.admin.task;

import com.yeecloud.adplus.admin.service.BookChapterService;
import com.yeecloud.adplus.admin.service.ChargeService;
import com.yeecloud.adplus.admin.service.SysConfService;
import com.yeecloud.adplus.dal.entity.BookChapter;
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

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private BookChapterService bookChapterService;

    @Scheduled(fixedDelay = 10000)
    public void refreshConfiguration() {
        sysConfService.refreshAsync();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateMaterialWeight() {
        chargeService.updateWeight();
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void checkBookStatus() {
        bookChapterService.checkBookStatus();
    }
}