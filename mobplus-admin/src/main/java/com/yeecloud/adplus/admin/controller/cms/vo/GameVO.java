package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * Title
 *
 * Date: 2020-06-19 15:37:25
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class GameVO {

    private Integer id;
    private String name;
    private String desc;
    private String thumbUrl;
    private String playUrl;
    private int type;
    private int status;
    private String remark;
    
    private Integer createdBy;
    private Integer modifiedBy;
    private Long createdAt;
    private Long modifiedAt;
}
