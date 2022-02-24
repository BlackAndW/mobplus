package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Data
public class BookChapterVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String content;

    private Integer chapterNo;

    private Integer isLock;

    private long readCount;

    private long fakeReadCount;

    private long lockCount;

    private long exitCount;

    private long exitCountPer;

    private long createdAt;
}
