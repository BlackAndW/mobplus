package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

/**
 * @author: Huang
 * @create: 2020-12-10 17:10
 */
public interface MemberRepository extends JpaRepository<Member, Integer>, QuerydslPredicateExecutor<Member> {

    List<Member> findByApp(App app);


}
