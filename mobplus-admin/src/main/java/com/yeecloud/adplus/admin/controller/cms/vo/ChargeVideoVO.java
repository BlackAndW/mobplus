package com.yeecloud.adplus.admin.controller.cms.vo;

import com.yeecloud.adplus.dal.entity.ChargeMType;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeVideoVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer typeId;

    private String typeName;

    private String videoPath;

    private String videoName;

    private String videoIntroduce;

    private String videoIntroduceName;

    private String videoCover;

    private String videoCoverThumb;

    private long useNum;

    private long showNum;

    private String useNumFake;

    private Integer useLimit;

    private long createdAt;

}
