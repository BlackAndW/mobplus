package com.yeecloud.adplus.admin.controller.cms.form;

import com.yeecloud.adplus.dal.entity.ChargeMType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeMaterialForm {

//    @NotBlank(message = "名称不能为空")
//    private String name;

    private ChargeMType type;

    @NotBlank(message = "源视频未上传")
    private String videoName;

    @NotBlank(message = "预览视频未上传")
    private String videoIntroduceName;

    @NotBlank(message = "封面未上传")
    private String videoCover;

    private Integer useLimit;
}
