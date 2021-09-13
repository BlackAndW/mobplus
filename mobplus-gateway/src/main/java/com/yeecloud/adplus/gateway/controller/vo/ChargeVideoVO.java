package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeVideoVO {

    private Integer id;

    private Integer typeId;

    private String typeName;

    private String videoPath;

    private String videoIntroduce;

    private String videoCover;

    private String videoCoverThumb;

    private String useNumFake;

    private Integer useLimit;
}
