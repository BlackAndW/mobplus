package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppProject;
import com.yeecloud.adplus.dal.entity.AppVersion;
import com.yeecloud.adplus.dal.entity.Channel;
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
 * @create: 2020-12-02 15:56
 */
@Repository
public interface AppProjectRepository extends JpaRepository<AppProject, Integer>, QuerydslPredicateExecutor<AppProject> {
    @Transactional
    @Modifying
    @Query("update AppProject obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    @Transactional
    @Modifying
    @Query("update AppProject obj set obj.deleted = true where obj.id = :id")
    void deleteById(@Param("id") Integer id);

    List<AppProject> findAllByAppVersionAndDeleted(@Param("appVersion") AppVersion appVersion, @Param("deleted") Boolean deleted);

    List<AppProject> findAllByChannelAndDeleted(@Param("channel") Channel channel, @Param("deleted") Boolean deleted);

    AppProject findByCode(@Param("code") String code);

    List<AppProject> findAllByApp(@Param("app") App app);

    AppProject findByAppAndChannelAndAppVersionAndDeleted(@Param("app") App app,@Param("channel")Channel channel,@Param("appVersion")AppVersion appVersion,@Param("deleted") Boolean deleted);
}

