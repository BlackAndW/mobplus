package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeMaterialVO {

    private Integer id;

    private String type;

    private String videoPath;

    private String videoIntroduce;

    private String videoCover;

    private String useNumFake;

    private Integer collection;

    private Integer useLimit;
}
