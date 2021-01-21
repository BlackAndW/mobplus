package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AppActivity;
import com.yeecloud.adplus.dal.entity.AppActivityAward;
import com.yeecloud.adplus.dal.entity.AppActivityTask;
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

public interface AppActivityAwardRepository extends JpaRepository<AppActivityAward, Integer>, QuerydslPredicateExecutor<AppActivityAward> {

    @Transactional
    @Modifying
    @Query("update AppActivityAward obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    List<AppActivityAward> findByAppActivity(@Param("appActivity") AppActivity appActivity);
}
