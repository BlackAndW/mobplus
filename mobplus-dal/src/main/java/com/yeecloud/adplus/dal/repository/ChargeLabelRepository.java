package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.ChargeLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/3/23
 */
@Repository
public interface ChargeLabelRepository extends JpaRepository<ChargeLabel, Integer>, QuerydslPredicateExecutor<ChargeLabel> {

    @Transactional
    @Modifying
    @Query("update ChargeLabel obj set obj.deleted = true where obj.id in :ids")
    void deleteById(@Param("ids") Integer[] ids);

    List<ChargeLabel> findAllByTypeAndDeleted(int type, boolean deleted);
}
