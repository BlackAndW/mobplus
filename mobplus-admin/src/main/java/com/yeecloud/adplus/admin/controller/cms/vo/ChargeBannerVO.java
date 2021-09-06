package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeBannerVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String imgPath;

    private Integer imgType;

    private String imgUrl;

    private Integer order;
}
