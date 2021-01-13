package com.yeecloud.adplus.admin.repository;

import com.yeecloud.adplus.admin.entity.SysAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作日志
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Repository
public interface SysAuditLogRepository extends JpaRepository<SysAuditLog, Integer>, QuerydslPredicateExecutor<SysAuditLog> {

    @Override
    @Modifying
    @Query("update SysAuditLog obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update SysAuditLog obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    @Override
    @Modifying
    @Query("update SysAuditLog obj set obj.deleted = true")
    void deleteAll();
}