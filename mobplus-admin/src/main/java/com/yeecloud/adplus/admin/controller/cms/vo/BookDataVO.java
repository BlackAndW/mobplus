package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Data
public class BookDataVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String author;

    private String cover;

    private String description;

    private Integer status;

    private Integer isVip;

    private Integer isFree;

    private long readCount;

    private long fakeReadCount;

    private long lockCount;

    private long collection;

    private long avgCount;

    private long size;
}
