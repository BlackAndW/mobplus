package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/11/29
 */
@Data
public class AppVersionVO {

    /** 最新版本 */
    private String latestVersion;

    /** 更新方式 */
    private String updateMethod;

    /** gp商店更新链接 */
    private String gpUrl;

    /** 应用内下载链接 */
    private String localUrl;

    /** 更新内容 */
    private String content;
}
