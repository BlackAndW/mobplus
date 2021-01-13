package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.DeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLog, Integer>, QuerydslPredicateExecutor<DeviceLog> {

}