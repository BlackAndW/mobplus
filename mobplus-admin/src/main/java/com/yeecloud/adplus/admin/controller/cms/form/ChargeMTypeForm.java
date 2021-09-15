package com.yeecloud.adplus.admin.controller.cms.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeMTypeForm {

    private String name;

    private String enName;

    private Integer style;

    private String orderColumn;

    private Integer order;

    private Integer rankOrder;
}
