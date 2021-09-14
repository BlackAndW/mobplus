package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeBannerVO {

    /** 图片路径 */
    private String imgPath;

    /** 图片类型 */
    private Integer imgType;

    /** 点击跳转url */
    private String imgUrl;

}
