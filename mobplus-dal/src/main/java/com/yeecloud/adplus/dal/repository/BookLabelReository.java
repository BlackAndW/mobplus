package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.BookLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Leonard
 * @create: 2022/4/7
 */

@Repository
public interface BookLabelReository extends JpaRepository<BookLabel, Integer>, QuerydslPredicateExecutor<BookLabel> {

    @Transactional
    @Modifying
    @Query("update BookLabel obj set obj.deleted = true where obj.id = :id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update BookLabel obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    BookLabel findByZhNameAndDeleted(String zhName, Integer deleted);
}
