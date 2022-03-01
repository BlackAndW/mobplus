package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2022/2/28
 */
@Data
public class BookForm {

    private String appId;

    private Integer bookId;

    private Integer chapterId;

    private Integer pageNo;

    private Integer userCount;

    private Integer collection;

    private Integer lockCount;

    private Integer readCount;

    private Integer exitCount;
}
