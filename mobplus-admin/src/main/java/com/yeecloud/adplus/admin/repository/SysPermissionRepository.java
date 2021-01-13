package com.yeecloud.adplus.admin.repository;

import com.yeecloud.adplus.admin.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Integer>, QuerydslPredicateExecutor<SysPermission> {

    @Override
    @Modifying
    @Query("update SysPermission obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update SysPermission obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    @Query(value = "select distinct a.n_id from sys_permission a left join sys_role_permission b on a.n_id=b.n_permission_id where a.n_status=2 and a.n_deleted = 0 and n_role_id in (select distinct n_role_id from sys_role a left join sys_user_role b on a.n_id = b.n_role_id where a.n_status=2 and a.n_deleted = 0 and b.n_user_id = :uid )", nativeQuery = true)
    List<Integer> findIdsByUserId(@Param("uid") Integer userId);

    @Query(value = "select distinct a.c_code from sys_permission a left join sys_role_permission b on a.n_id=b.n_permission_id where a.n_status=2 and a.n_deleted = 0 and n_role_id in (select distinct n_role_id from sys_role a left join sys_user_role b on a.n_id = b.n_role_id where a.n_status=2 and a.n_deleted = 0 and b.n_user_id = :uid )", nativeQuery = true)
    List<String> findCodesByUserId(@Param("uid") Integer userId);
}