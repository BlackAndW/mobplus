package com.yeecloud.adplus.admin.controller.cms.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TalkCategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer parentId;
    private String name;
    private String icon;
    private Integer sort;
    private Integer level;
    private Integer status;
    private Long createdAt;
    private Long modifiedAt;
    private List<TalkCategoryVO> children;
}
