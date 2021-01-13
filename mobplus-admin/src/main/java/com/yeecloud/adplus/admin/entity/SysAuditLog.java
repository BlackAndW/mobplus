package com.yeecloud.adplus.admin.entity;

import com.yeecloud.adplus.dal.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 操作日志
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Data
@Entity
@Table(name = "sys_audit_log")
@EntityListeners(AuditingEntityListener.class)
public class SysAuditLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String AUDIT_LOG_ATTRIBUTE_NAME = SysAuditLog.class.getName() + ".AUDIT_LOG";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer id;

    /** 用户 */
    @Column(name = "n_user_id")
    private Integer userId;

    @Column(name = "c_action")
    private String action;

    @Column(name = "c_detail")
    private String detail;

    @Column(name = "c_parameters")
    private String parameters;

    @Column(name = "c_request_url")
    private String requestUrl;

    @Column(name = "c_response")
    private String response;

    @Column(name = "c_ip_addr")
    private String ipAddr;

    /** 状态 */
    @Column(name = "n_status")
    private Integer status;

    /** 备注 */
    @Column(name = "c_remark")
    private String remark;

    /** 是否删除 */
    @Column(name = "n_deleted")
    protected boolean deleted = false;

    /** 创建时间 */
    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;

    /** 修改时间 */
    @LastModifiedDate
    @Column(name = "n_modified_at")
    protected Long modifiedAt;
}
