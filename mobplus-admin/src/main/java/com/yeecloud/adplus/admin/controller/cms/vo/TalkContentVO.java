package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TalkContentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer talkId;
    private Integer key;
    private String value;
    private Integer sort;
}
