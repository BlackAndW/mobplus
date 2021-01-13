package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TalkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer categoryId;

    private String title;


}
