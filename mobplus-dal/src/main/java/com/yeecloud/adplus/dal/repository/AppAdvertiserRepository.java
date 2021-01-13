package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.Advertiser;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppAdvertiser;
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
 * @create: 2020-12-09 10:42
 */
@Repository
public interface AppAdvertiserRepository extends JpaRepository<AppAdvertiser, Integer>, QuerydslPredicateExecutor<AppAdvertiser> {
    List<AppAdvertiser> findByApp(App app);

    AppAdvertiser findByAppAndAdvertiser(App app, Advertiser advertiser);

    @Transactional
    @Modifying
    void deleteByApp(App app);

    @Override
    @Modifying
    @Query("delete from AppAdvertiser obj  where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("delete from AppAdvertiser obj  where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}
