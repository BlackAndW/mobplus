package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AppFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2021/1/28
 */

public interface AppFunctionRepository extends JpaRepository<AppFunction, Integer>, QuerydslPredicateExecutor<AppFunction> {

    @Transactional
    @Modifying
    @Query("update AppFunction obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}
