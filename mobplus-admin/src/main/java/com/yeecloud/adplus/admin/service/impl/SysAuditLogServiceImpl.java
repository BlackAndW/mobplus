package com.yeecloud.adplus.admin.service.impl;

import com.google.common.collect.Lists;
import com.yeecloud.adplus.admin.controller.sys.vo.SysAuditLogVO;
import com.yeecloud.adplus.admin.convert.SysAuditLogConvert;
import com.yeecloud.adplus.admin.entity.SysAuditLog;
import com.yeecloud.adplus.admin.repository.SysAuditLogRepository;
import com.yeecloud.adplus.admin.service.SysAuditLogService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;

/**
 * 操作日志
 *
 * Date: 2019-11-08 13:49:33
 * Copyright (c) 2019-2099 YeeCloud TECH CO.LTD
 *
 * @author ybbk
 * @version v1.0.0
 */
@Slf4j
@Service
public class SysAuditLogServiceImpl implements SysAuditLogService {

    @Autowired
    private SysAuditLogConvert sysAuditLogConvert;

    @Autowired
    private SysAuditLogRepository sysAuditLogRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PageInfo<SysAuditLogVO> query(Query query) throws ServiceException {
        try {
            List<Object> params = Lists.newArrayList();

            Long startAt = query.get("startAt", Long.class);
            Long endAt = query.get("endAt", Long.class);

            String condition = "";
            if (startAt != null && startAt > 0) {
                condition += " and a.n_created_at>=?";
                params.add(startAt);
            }
            if (endAt != null && endAt > 0) {
                condition += " and a.n_created_at<?";
                params.add(endAt);
            }

            String sql = "select a.n_id as id, a.n_user_id as userId, a.c_action as action, a.c_detail as detail, " +
                    "a.c_parameters as parameter, a.c_request_url as requestUrl, a.c_response as response, a.c_ip_addr as ipAddr," +
                    "a.n_status as status, a.c_remark as remark, a.n_created_at as createdAt, a.n_modified_at as modifiedAt," +
                    "b.c_name as actName, c.c_display_name as userName from sys_audit_log a left join sys_permission b on a.c_action=b.c_code " +
                    "left join sys_user c on a.n_user_id=c.n_id where a.n_deleted=0 " + condition + " order by a.n_created_at desc limit " + query.getOffset() + ", " + query.getPageSize();
            String countSql = "select count(*) from sys_audit_log a where a.n_deleted=0 " + condition;

            List<SysAuditLogVO> list = jdbcTemplate.query(sql, params.toArray(), (ResultSet resultSet, int rowNum) -> {
                SysAuditLogVO vo = new SysAuditLogVO();
                vo.setId(resultSet.getInt("id"));
                vo.setUserId(resultSet.getInt("userId"));
                vo.setAction(resultSet.getString("action"));
                vo.setDetail(resultSet.getString("detail"));
                vo.setParameter(resultSet.getString("parameter"));
                vo.setRequestUrl(resultSet.getString("requestUrl"));
                vo.setResponse(resultSet.getString("response"));
                vo.setIpAddr(resultSet.getString("ipAddr"));
                vo.setStatus(resultSet.getInt("status"));
                vo.setRemark(resultSet.getString("remark"));
                vo.setCreatedAt(resultSet.getLong("createdAt"));
                vo.setModifiedAt(resultSet.getLong("modifiedAt"));
                vo.setActName(resultSet.getString("actName"));
                vo.setUserName(resultSet.getString("userName"));
                return vo;
            });
            Integer totalCount = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);

            return new PageInfo<>(query.getPageNo(), query.getPageSize(), totalCount, list);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SysAuditLogVO findById(Integer id) throws ServiceException {
        try {
            SysAuditLog entity = sysAuditLogRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return sysAuditLogConvert.convert(entity);
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer... ids) throws ServiceException {
        try {
            if (ids != null && ids.length > 0) {
                sysAuditLogRepository.deleteById(ids);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteAll() throws ServiceException {
        try {
            sysAuditLogRepository.deleteAll();
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }
}
