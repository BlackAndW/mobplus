package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TalkRepository extends JpaRepository<Talk, Integer>, QuerydslPredicateExecutor<Talk> {
    @Transactional
    @Modifying
    @Query("update Talk obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
    
}
