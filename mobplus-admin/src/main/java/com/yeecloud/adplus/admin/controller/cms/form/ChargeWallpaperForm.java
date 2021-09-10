package com.yeecloud.adplus.admin.controller.cms.form;

import com.yeecloud.adplus.dal.entity.ChargeMType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeWallpaperForm {

    private String name;

    private ChargeMType type;

    @NotBlank(message = "图片未上传")
    private String imgUrl;

    private String thumbUrl;

    private Integer useLimit;
}
