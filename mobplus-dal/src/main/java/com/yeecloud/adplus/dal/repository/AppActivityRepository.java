package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AppActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/1/18
 */

public interface AppActivityRepository extends JpaRepository<AppActivity, Integer>, QuerydslPredicateExecutor<AppActivity> {

    @Transactional
    @Modifying
    @Query("update AppActivity obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

}
