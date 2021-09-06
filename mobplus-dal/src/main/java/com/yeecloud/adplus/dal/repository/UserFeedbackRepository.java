package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Title
 *
 * Date: 2020-06-12 14:27:31
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Repository
public interface UserFeedbackRepository extends JpaRepository<UserFeedback, Integer>, QuerydslPredicateExecutor<UserFeedback> {

    @Override
    @Modifying
    @Query("update UserFeedback obj set obj.deleted = true where obj.id=:id")
    void deleteById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update UserFeedback obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

}
