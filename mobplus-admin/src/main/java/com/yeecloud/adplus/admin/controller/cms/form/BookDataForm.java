package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Data
public class BookDataForm {

    @NotNull(message = "appId不能为空")
    private Integer appId;

    @NotBlank(message = "书名不能为空")
    private String name;

    @NotBlank(message = "author")
    private String author;

    @NotBlank(message = "封面不能为空")
    private String cover;

    private String description;

    private Integer status;

    private Integer isVip;

    private Integer isFree = 0;
}
