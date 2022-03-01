package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2022/3/1
 */
@Data
public class BookVO {

    private Integer bookId;

    private String name;

    private String author;

    private String description;

    private String cover;

    private Integer status;
}
