package com.yeecloud.adplus.admin.controller.app.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Data
public class AppLinkForm {

    @NotBlank(message = "名称不能为空")
    private String name;

    private Integer appId;

    @NotBlank(message = "图片未上传")
    private String imgPath;

    private Integer imgType;

    @NotBlank(message = "跳转url不能为空")
    private String imgUrl;

}
