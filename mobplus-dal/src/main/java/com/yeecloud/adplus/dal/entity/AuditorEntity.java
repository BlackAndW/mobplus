package com.yeecloud.adplus.dal.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Title
 *
 * Date: 2019-11-06 16:56:19
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditorEntity extends BaseEntity {

    /**
     * 是否删除
     */
    @JSONField(serialize = false)
    @Column(name = "n_deleted")
    protected boolean deleted = false;

    /** 创建人 */
    @CreatedBy
    @Column(name = "n_created_by", updatable = false)
    protected Integer createdBy;

    /** 更新人 */
    @LastModifiedBy
    @Column(name = "n_modified_by")
    protected Integer modifiedBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "n_modified_at")
    protected Long modifiedAt;
}
