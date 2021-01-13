package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AdType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 广告类型
 *
 * Date: 2020-05-24 13:03:57
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version v1.0.0
 */
@Repository
public interface AdTypeRepository extends JpaRepository<AdType, Integer>, QuerydslPredicateExecutor<AdType> {

    @Override
    @Modifying
    @Query("update AdType obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update AdType obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}