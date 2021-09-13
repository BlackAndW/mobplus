package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeWallpaperVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer typeId;

    private String typeName;

    private String imgUrl;

    private String thumbUrl;

    private long useNum;

    private long showNum;

    private String useNumFake;

    private Integer useLimit;

    private long createdAt;

}
