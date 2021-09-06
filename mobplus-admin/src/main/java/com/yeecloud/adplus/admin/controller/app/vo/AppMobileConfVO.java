package com.yeecloud.adplus.admin.controller.app.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-15 09:46
 */
@Data
public class AppMobileConfVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer appId;
    private String name;
    private String key;
    private String value;
    private String remark;
    private Integer status;
    private Long createdAt;
    private Long modifiedAt;

    /** 应用版本 */
    private List<String> appVersionCheckList;

    /** 应用渠道 */
    private List<String> channelCheckList;
}
