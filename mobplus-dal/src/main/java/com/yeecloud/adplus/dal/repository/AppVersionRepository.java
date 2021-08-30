package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppVersion;
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
 * @create: 2020-12-02 14:39
 */
@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Integer>, QuerydslPredicateExecutor<AppVersion> {

    @Transactional
    @Modifying
//    @Query("update AppVersion obj set obj.deleted = true where obj.id = :id")
    void deleteById(@Param("id") Integer id);

    List<AppVersion> findAllByAppAndDeleted(@Param("app") App app, @Param("deleted") Boolean deleted);

    AppVersion findByAppAndCode(@Param("app")App app, @Param("code") String code);

}

