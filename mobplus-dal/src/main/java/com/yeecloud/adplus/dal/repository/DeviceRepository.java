package com.yeecloud.adplus.dal.repository;

import com.yeecloud.adplus.dal.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>, QuerydslPredicateExecutor<Device> {

    Device findFirstByUuid(String uuid);
}