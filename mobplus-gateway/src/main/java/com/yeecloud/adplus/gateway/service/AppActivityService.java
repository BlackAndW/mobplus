package com.yeecloud.adplus.gateway.service;

import com.yeecloud.adplus.dal.entity.AppActivity;

import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * @author: Leonard
 * @create: 2021/1/20
 */
public interface AppActivityService {

    Page<AppActivity> getAppActivityPage(Query query);

    Optional<AppActivity> getAppActivityList(Query query);
}
