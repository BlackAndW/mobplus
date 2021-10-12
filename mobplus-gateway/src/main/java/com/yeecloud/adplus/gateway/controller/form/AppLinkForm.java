package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Data
public class AppLinkForm {

    /** ---链接id */
    private Integer id;

    /** ---appId */
    private String appId;

    /** ---点击次数 ---传0或1 */
    private Integer clickNum;

    /** ---展示次数 ---传0或1 */
    private Integer showNum;
}
