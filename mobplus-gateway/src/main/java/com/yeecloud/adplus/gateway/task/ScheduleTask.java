package com.yeecloud.adplus.gateway.task;

import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.QApp;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Title
 * Description:
 * Date: 2019/3/14 15:44
 * Copyright (c) 2019-2099 YYSKYS YeeCloud
 *
 * @author ybbk
 */
@Slf4j
@Component
public class ScheduleTask {

    @Autowired
    AppRepository appRepository;

    private static final String BASE_DIR_NAME = "/www/wwwroot/cdn/";

    private static final String FILE_NAME = "result.json";

    private void getAppConfigCfg() {
        QApp app = QApp.app;
        Predicate predicate = app.deleted.eq(Boolean.FALSE);
        List<App> appList = (List<App>) appRepository.findAll(predicate);
    }

    private void genConfigFile(String dirName, Result result) {
        File dirs = new File(dirName);
        if (!dirs.exists() && !dirs.isDirectory()) {
            dirs.mkdirs();
        }
        try(FileOutputStream os = new FileOutputStream(dirName + FILE_NAME);
            OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            writer.write(result.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}