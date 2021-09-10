package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeWallpaperVO {

    private Integer id;

    private String typeName;

    private String imgUrl;

    private String thumbUrl;

    private String useNumFake;

    private Integer useLimit;
}
