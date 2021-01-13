package com.yeecloud.adplus.admin.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Title
 *
 * Date: 2019-11-09 02:01:42
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public abstract class BaseVO implements Serializable {

    protected Integer id;

    protected Integer createdBy;

    protected String createdName;

    protected Long createdAt;

    private Integer modifiedBy;

    protected String modifiedName;

    protected Long modifiedAt;
}
