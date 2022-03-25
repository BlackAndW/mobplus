package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2022/3/23
 */

@Data
public class ChargeLabelForm {

    @NotBlank(message = "中文名称不能为空")
    private String name;

    @NotBlank(message = "英文名称不能为空")
    private String enName;

    @NotNull(message = "标签类型不能为空")
    private Integer type;

    private String searchText;
}
