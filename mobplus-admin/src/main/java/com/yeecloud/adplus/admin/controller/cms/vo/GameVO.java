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
    private Integer gameId;
    private Integer clickNum;
    private Integer ipNum;
    private Integer playTime;
//    private String desc;
    private String thumbUrl;
//    private String playUrl;
//    private String type;
//    private int status;
//    private String remark;
//    private Integer showNum;
//    private Integer createdBy;
//    private Integer modifiedBy;
    private Long createdAt;
//    private Long modifiedAt;
}
