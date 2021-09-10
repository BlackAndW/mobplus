package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */
@Data
public class ChargeMTypeVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer style;

    private String orderColumn;

    private Integer order;

    private Integer rankOrder;

    private long createdAt;
}
