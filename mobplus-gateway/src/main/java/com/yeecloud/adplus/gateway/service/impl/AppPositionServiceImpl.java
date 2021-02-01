package com.yeecloud.adplus.gateway.service.impl;

import com.alibaba.fastjson.*;
import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.gateway.controller.form.DeviceForm;
import com.yeecloud.adplus.gateway.controller.vo.AdPositionVO;
import com.yeecloud.adplus.gateway.controller.vo.AppFunctionVO;
import com.yeecloud.adplus.gateway.controller.vo.AppPosCfgVO;
import com.yeecloud.adplus.gateway.service.AppPositionService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/1/27
 */
@Slf4j
@Service
public class AppPositionServiceImpl implements AppPositionService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Override
    public List getAdTypeConfList(DeviceForm form) throws ServiceException {
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null || app.isDeleted()) {
            throw new ServiceException("应用不存在，请重新配置应用");
        }
        QAppConfig appConfig = QAppConfig.appConfig;
        Predicate predicate = appConfig.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, appConfig.status.eq(2));
        predicate = ExpressionUtils.and(predicate, appConfig.app.appId.eq(form.getAppId()));
        predicate = ExpressionUtils.and(predicate, appConfig.appVersion.code.eq(form.getPkgVersion()));
        predicate = ExpressionUtils.and(predicate, appConfig.channel.code.eq(form.getChannel()));
        AppConfig appConfigPO = jpaQueryFactory.selectFrom(appConfig).where(predicate).fetchOne();
        List resultList = new ArrayList();

        if (null != appConfigPO) {
            log.info("conf: " + appConfigPO.getConf());
            resultList = JSONArray.parseArray(appConfigPO.getConf());
        }
        return resultList;
    }

    @Override
    public List getAdTypeConfListNew(DeviceForm form) throws ServiceException {
        App app = appRepository.findByAppId(form.getAppId());
        if (app == null || app.isDeleted()) {
            throw new ServiceException("应用不存在，请重新配置应用");
        }
        QAppFunction appFunction = QAppFunction.appFunction;
        Predicate predicate = appFunction.deleted.eq(false);

        predicate = ExpressionUtils.and(predicate, appFunction.app.id.eq(app.getId()));
        predicate = ExpressionUtils.and(predicate, appFunction.channel.code.eq(form.getChannel()));
        predicate = ExpressionUtils.and(predicate, appFunction.appVersion.code.eq(form.getPkgVersion()));
        List<AppFunctionVO> appFunctionVOList = new ArrayList<>();
        List<AppFunction> resultList = jpaQueryFactory.selectFrom(appFunction).where(predicate).fetch();

        resultList.forEach(resultListItem ->{
            AppFunctionVO appFunctionVO = new AppFunctionVO();
            NewBeanUtils.copyProperties(appFunctionVO, resultListItem);
            List adTypeList = JSONArray.parseArray(resultListItem.getAdTypeConf());
            appFunctionVO.setAdTypeList(adTypeList);
            appFunctionVOList.add(appFunctionVO);
        });
        log.info("result: " + appFunctionVOList.toString());
        return appFunctionVOList;
    }
}
