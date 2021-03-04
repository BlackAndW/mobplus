package com.yeecloud.adplus.admin.controller.app.form;

import com.yeecloud.adplus.dal.entity.AdPosition;
import lombok.Data;

/**
 * @author: Huang
 * @create: 2020-12-08 17:23
 */
@Data
public class AppPositionAdPositionForm {
    private AdPosition adPos;
    private String advName;
    private Integer ratio;
    private Boolean ratioFlag;
    private Integer typeRatio;
}
