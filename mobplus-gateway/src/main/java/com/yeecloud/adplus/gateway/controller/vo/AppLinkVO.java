package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Data
public class AppLinkVO {

    /** 链接id */
    private Integer id;

    /** 链接名 */
    private String name;

    /** 图片路径 */
    private String imgPath;

//    private Integer imgType;

    /** 点击跳转url */
    private String imgUrl;

    /** 点击次数 */
    private Integer clickNum;

    /** 展示次数 */
    private Integer showNum;

    /** 链接描述 */
    private String description;
}
