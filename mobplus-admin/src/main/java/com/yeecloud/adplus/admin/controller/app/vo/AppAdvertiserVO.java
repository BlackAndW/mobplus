package com.yeecloud.adplus.admin.controller.app.vo;

import com.yeecloud.adplus.admin.common.vo.BaseVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Huang
 * @create: 2020-12-09 10:52
 */
@Data
public class AppAdvertiserVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer appAppId;
    private Integer advId;
    private String advName;
    private String appId;
    private String appKey;
    private String appName;
    private Long createdAt;
    private Long modifiedAt;
}
