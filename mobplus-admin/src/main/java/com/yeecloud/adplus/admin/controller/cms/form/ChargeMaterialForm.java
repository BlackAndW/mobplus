package com.yeecloud.adplus.admin.controller.cms.form;

import com.yeecloud.adplus.dal.entity.ChargeMType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeMaterialForm {

//    @NotBlank(message = "名称不能为空")
//    private String name;
    @NotNull(message = "appId不能为空")
    private Integer appId;

    private ChargeMType type;

    private String videoName;

    private String videoIntroduceName;

    private String videoCover;

    private String videoCoverThumb;

    private String imgUrl;

    private String thumbUrl;

    private Integer useLimit;

    private Integer needReward;
}
