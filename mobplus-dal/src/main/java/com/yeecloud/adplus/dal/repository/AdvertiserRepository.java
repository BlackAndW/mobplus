package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.Advertiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 广告平台
 *
 * Date: 2020-05-24 13:03:57
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version v1.0.0
 */
@Repository
public interface AdvertiserRepository extends JpaRepository<Advertiser, Integer>, QuerydslPredicateExecutor<Advertiser> {
    Advertiser findFirstByCode(String code);

    @Override
    @Modifying
    @Query("update Advertiser obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update Advertiser obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}