package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Data
public class BookChapterForm {

    @NotNull(message = "bookId不能为空")
    private Integer bookId;

    @NotNull(message = "chapterNo不能为空")
    private Integer chapterNo;

    @NotBlank(message = "章节标题不能为空")
    private String name;

    @NotBlank(message = "章节内容不能为空")
    private String content;

    private Integer isLock;
}
