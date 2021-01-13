package com.yeecloud.adplus.admin.controller.sys.vo;

import com.yeecloud.adplus.admin.common.vo.BaseVO;

import lombok.Data;

import java.util.List;

/**
 * 数据分类
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
public class SysCategoryVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private String name;

    private String nameEn;

    private Integer status;

    private Integer sort;

    private String remark;

    private List<SysCategoryVO> children;
}