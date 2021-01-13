package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author: Huang
 * @create: 2020-12-01 10:38
 */
@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer>, QuerydslPredicateExecutor<Channel> {


    @Transactional
    @Modifying
    @Query("update Channel obj set obj.deleted = true where obj.id = :id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update Channel obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);


    Channel findByCode(@Param("code") String code);
}