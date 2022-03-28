package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/3/23
 */
@Data
public class ChargeLabelVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String enName;

    private Integer type;

    private long createdAt;

}
