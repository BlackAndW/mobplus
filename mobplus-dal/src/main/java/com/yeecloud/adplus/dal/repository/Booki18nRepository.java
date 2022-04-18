package com.yeecloud.adplus.dal.repository;


import com.yeecloud.adplus.dal.entity.Booki18n;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2022/2/16
 */
@Repository
public interface Booki18nRepository extends JpaRepository<Booki18n, Integer>, QuerydslPredicateExecutor<Booki18n> {

    @Transactional
    @Modifying
    @Query("update Booki18n obj set obj.deleted = true where obj.id = :id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update Booki18n obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

}
