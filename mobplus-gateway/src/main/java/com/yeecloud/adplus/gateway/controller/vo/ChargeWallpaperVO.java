package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeWallpaperVO {

    /** 壁纸 id */
    private Integer id;

    /** 类型id */
    private Integer typeId;

    /** 类型名 */
    private String typeName;

    /** 图片地址 */
    private String imgUrl;

    /** 缩略图地址 */
    private String thumbUrl;

    /** 虚拟使用次数(前端展示用) */
    private String useNumFake;

    /** 用户使用限制(会员)，1:限制; 2:无限制 */
    private Integer useLimit;
}
