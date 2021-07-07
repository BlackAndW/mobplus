package com.yeecloud.adplus.admin.controller.cms.vo;

import com.yeecloud.adplus.dal.entity.ChargeMType;
import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeMaterialVO {

    private Integer id;

    private String name;

    private String type;

    private String videoPath;

    private String videoName;

    private String videoIntroduce;

    private String videoIntroduceName;

    private String videoCover;

    private long useNum;

    private long showNum;

    private String useNumFake;

    private Integer useLimit;

    private long createdAt;
}
