package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TalkCategoryForm {

    @NotNull
    private Integer parentId;

    @NotBlank(message = "话术类别名称不能为空！")
    private String name;

    @NotNull
    private Integer sort;

    @NotNull
    private Integer status;
}
