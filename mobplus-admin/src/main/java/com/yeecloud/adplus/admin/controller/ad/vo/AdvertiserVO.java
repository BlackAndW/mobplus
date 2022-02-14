package com.yeecloud.adplus.admin.controller.ad.vo;

import com.yeecloud.adplus.dal.entity.Advertiser;
import lombok.Data;

/**
 * @author: Huang
 * @create: 2020-12-08 15:38
 */
@Data
public class AdvertiserVO extends Advertiser {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String code;
    private String name;
    private String testId1;
    private String testId2;
    private String testId3;
    private String testId4;
    private String testId5;
    private String testId6;
    private String testId7;
    private String testId8;
    private String remark;
    private Integer createdBy;
    private Integer modifiedBy;
    private Long createdAt;
    private Long modifiedAt;
}