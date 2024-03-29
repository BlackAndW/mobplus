package com.yeecloud.adplus.dal.repository;


import com.yeecloud.adplus.dal.entity.TalkContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TalkContentRepository extends JpaRepository<TalkContent, Integer>, QuerydslPredicateExecutor<TalkContent> {
    @Transactional
    @Modifying
    @Query("delete from TalkContent obj  where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);
    
}
