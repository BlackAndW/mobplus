package com.yeecloud.adplus.admin.controller.app.form;

import com.yeecloud.adplus.dal.entity.AppActivity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/1/21
 */

@Data
public class AppActivityAwardForm {

    private AppActivity appActivity;

    /** 奖品类别 */
    @NotNull(message = "请输入奖品类别")
    private Integer awardType;

    /** 奖品名称 */
    @NotBlank(message = "请输入奖品名称")
    private String awardName;

    /** 奖品图片 */
    @NotBlank(message = "请选择奖品图片")
    private String awardImgPath;

    /** 奖品碎片 */
    @NotBlank(message = "请输入奖品碎片")
    private String awardPiece;

    private String remark;

    private Integer status;
}
