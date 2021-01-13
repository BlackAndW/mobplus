package com.yeecloud.adplus.admin.repository;

import com.yeecloud.adplus.admin.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Integer>, QuerydslPredicateExecutor<SysRole> {

    @Override
    @Modifying
    @Query("update SysRole obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update SysRole obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    @Query(value = "select distinct c_code from sys_role a left join sys_user_role b on a.n_id = b.n_role_id where a.n_status=2 and a.n_deleted = 0 and b.n_user_id = :uid ", nativeQuery = true)
    List<String> findCodesByUserId(@Param("uid") Integer userId);

    @Query("select obj from SysRole obj where obj.deleted = false and obj.code=:code")
    SysRole findByCode(@Param("code") String code);
}