package com.yeecloud.adplus.admin.controller.ad.vo;

import com.yeecloud.adplus.dal.entity.Advertiser;
import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */
@Data
public class AdAccountVO {

    private String name;

    private String accountId;

    private Integer advId;

    private String domain;
}
