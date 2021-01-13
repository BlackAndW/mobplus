package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Huang
 * @create: 2020-12-02 10:04
 */
@Repository
public interface AppRepository extends JpaRepository<App, Integer>, QuerydslPredicateExecutor<App> {

    @Transactional
    @Modifying
    @Query("update App obj set obj.deleted = true where obj.id = :id")
    void deleteById(@Param("id") Integer id);

    App findByAppId(@Param("appId") String appId);
}
