package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.ChargeWallpaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */
@Repository
public interface ChargeWallpaperRepository extends JpaRepository<ChargeWallpaper, Integer>, QuerydslPredicateExecutor<ChargeWallpaper> {

    @Transactional
    @Modifying
    @Query("update ChargeWallpaper obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}
