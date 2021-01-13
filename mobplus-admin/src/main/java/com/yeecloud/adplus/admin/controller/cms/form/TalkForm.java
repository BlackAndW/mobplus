package com.yeecloud.adplus.admin.controller.cms.form;

import com.yeecloud.adplus.dal.entity.TalkCategory;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TalkForm {

    private TalkCategory category;

    @NotBlank(message = "标题不能为空！")
    private String title;

}
