package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Data
public class AppLinkVO {

    private Integer id;

    private String name;

    private String imgPath;

//    private Integer imgType;

    private String imgUrl;

    private Integer clickNum;

    private Integer showNum;

    private String description;
}
