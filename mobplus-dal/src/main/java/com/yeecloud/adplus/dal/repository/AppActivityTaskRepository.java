package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.AppActivityTask;
import com.yeecloud.adplus.dal.entity.AppPosition;
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

public interface AppActivityTaskRepository extends JpaRepository<AppActivityTask, Integer>, QuerydslPredicateExecutor<AppActivityTask> {

    @Transactional
    @Modifying
    @Query("update AppActivityTask obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    List<AppActivityTask> findByAppActivity(@Param("appActivity") AppActivity appActivity);
}
