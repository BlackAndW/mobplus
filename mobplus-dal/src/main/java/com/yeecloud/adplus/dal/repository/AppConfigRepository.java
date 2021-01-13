package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AppConfig;
import com.yeecloud.adplus.dal.entity.AppProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Huang
 * @create: 2020-12-02 17:29
 */
@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Integer>, QuerydslPredicateExecutor<AppConfig> {

    @Transactional
    @Modifying
    @Query("update AppConfig obj set obj.adSwitch = :status where obj.id in :ids")
    void controlAd(@Param("ids") Integer[] ids, @Param("status") Integer status);

    AppConfig findByAppProject(@Param("appProject") AppProject appProject);

}
