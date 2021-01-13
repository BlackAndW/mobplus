package com.yeecloud.adplus.admin.controller.app.form;

import com.yeecloud.adplus.dal.entity.Advertiser;
import com.yeecloud.adplus.dal.entity.App;
import lombok.Data;

/**
 * @author: Huang
 * @create: 2020-12-09 11:10
 */
@Data
public class AppAdvertiserForm {
    private App app;
    private Advertiser advertiser;
    private String appId;
    private String appKey;
    private String appName;
}
