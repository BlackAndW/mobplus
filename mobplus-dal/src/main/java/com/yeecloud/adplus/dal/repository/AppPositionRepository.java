package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-08 17:17
 */
@Repository
public interface AppPositionRepository extends JpaRepository<AppPosition, Integer>, QuerydslPredicateExecutor<AppPosition> {

    AppPosition findByCode(@Param("code") String code);

    @Transactional
    @Modifying
    @Query("update AppPosition obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    List<AppPosition> findByApp(@Param("app") App app);
}