package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.AppLink;
import com.yeecloud.adplus.dal.entity.AppLinkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Repository
public interface AppLinkLogRepository extends JpaRepository<AppLinkLog, Integer>, QuerydslPredicateExecutor<AppLinkLog> {

    @Transactional
    @Modifying
    @Query("update AppLinkLog obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
}
