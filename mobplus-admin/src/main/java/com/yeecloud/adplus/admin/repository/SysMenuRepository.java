package com.yeecloud.adplus.admin.repository;

import com.yeecloud.adplus.admin.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单信息
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Integer>, QuerydslPredicateExecutor<SysMenu> {

    @Override
    @Modifying
    @Query("update SysMenu obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update SysMenu obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    List<SysMenu> findByParentIdAndDeletedAndStatusOrderBySort(Integer parentId, boolean delete, int status);

    @Query(value = "select a.* from sys_menu a join sys_menu_permission b on a.n_id = b.n_menu_id where a.n_status=2 and a.n_deleted=0 and a.n_id = :menuId and b.n_permission_id in ( :perms ) ", nativeQuery = true)
    List<SysMenu> findByMenuIdAndPermissions(@Param("menuId") Integer menuId, @Param("perms") List<Integer> permissionList);

}