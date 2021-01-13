package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AdPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Huang
 * @create: 2020-12-08 16:04
 */
@Repository
public interface AdPositionRepository extends JpaRepository<AdPosition, Integer>, QuerydslPredicateExecutor<AdPosition> {

    @Override
    @Modifying
    @Query("update AdPosition obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update AdPosition obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}