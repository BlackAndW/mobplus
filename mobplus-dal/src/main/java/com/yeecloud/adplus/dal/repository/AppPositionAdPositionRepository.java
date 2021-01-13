package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AppPosition;
import com.yeecloud.adplus.dal.entity.AppPositionAdPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Huang
 * @create: 2020-12-08 17:36
 */
@Repository
public interface AppPositionAdPositionRepository extends JpaRepository<AppPositionAdPosition, Integer>, QuerydslPredicateExecutor<AppPositionAdPosition> {

    void deleteByAppPosition(AppPosition appPosition);

    @Override
    @Modifying
    @Query("update AppPositionAdPosition obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update AppPositionAdPosition obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}