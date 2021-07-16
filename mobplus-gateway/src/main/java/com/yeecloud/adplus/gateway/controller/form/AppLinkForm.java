package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Data
public class AppLinkForm {

    private Integer id;

    private String appId;

    private Integer clickNum;

    private Integer showNum;
}
