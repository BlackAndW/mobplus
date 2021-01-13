package com.yeecloud.adplus.dal.repository;


import com.yeecloud.adplus.dal.entity.TalkCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface TalkCategoryRepository extends JpaRepository<TalkCategory, Integer>, QuerydslPredicateExecutor<TalkCategory> {


    @Transactional
    @Modifying
    @Query("update TalkCategory obj set obj.deleted = true where obj.id = :id")
    void deleteById(@Param("id") Integer id);


    @Transactional
    @Modifying
    @Query("update TalkCategory obj set obj.deleted = true where obj.parentId = :parentId")
    void deleteByParentId(@Param("parentId") Integer parentId);

    List<TalkCategory> findAllByParentId(@Param("parentId") Integer parentId);
}
