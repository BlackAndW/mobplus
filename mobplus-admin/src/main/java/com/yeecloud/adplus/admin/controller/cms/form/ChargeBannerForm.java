package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeBannerForm {

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotBlank(message = "图片未上传")
    private String imgPath;

    private Integer imgType;

    @NotBlank(message = "跳转url不能为空")
    private String imgUrl;

    private Integer order = 1;
}
