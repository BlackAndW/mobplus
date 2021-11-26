package com.yeecloud.adplus.gateway.task;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppMobileConfRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.AppVersionRepository;
import com.yeecloud.adplus.dal.repository.ChannelRepository;
import com.yeecloud.adplus.gateway.controller.AppConfigController;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    AppVersionRepository appVersionRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    AppConfigController appConfigController;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    private static final String BASE_DIR_NAME = "/www/wwwroot/cdn/";

    private static final String FILE_NAME = "result.json";

//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    private void getAppConfigCfg() {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.deleted.eq(Boolean.FALSE);
        predicate = ExpressionUtils.and(predicate, qApp.status.eq(2));
        List<App> appList = (List<App>)appRepository.findAll(predicate);

        QAppVersion qAppVersion = QAppVersion.appVersion;

        QChannel qChannel = QChannel.channel;
        // 获取所有渠道code
        List<String> allChannel = jpaQueryFactory.select(qChannel.code).from(qChannel).
                where(qChannel.deleted.eq(Boolean.FALSE).
                        and(qChannel.status.eq(2))).
                fetch();

        appList.forEach(app ->  {
            // 根据id获取该app下所有版本
            List<String> appVersionList = jpaQueryFactory.
                    select(qAppVersion.code).
                    from(qAppVersion).
                    where(qAppVersion.deleted.eq(Boolean.FALSE).
                            and(qAppVersion.status.eq(2)).
                            and(qAppVersion.app.id.eq(app.getId()))).
                    fetch();
            appVersionList.forEach(appVersion -> {
                allChannel.forEach(channel -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("appId", app.getAppId());
                    jsonObject.put("pkgVersion", appVersion);
                    jsonObject.put("channel", channel);
                    String body = jsonObject.toJSONString();

                    String dirName = BASE_DIR_NAME +
                            "v1/app/mobile/conf/" +
                            app.getAppId() + "/" +
                            appVersion + "/" +
                            channel + "/";

                    genConfigFile(dirName, appConfigController.getAppConfig(body,"", "1.1"));
                });
            });
        });
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