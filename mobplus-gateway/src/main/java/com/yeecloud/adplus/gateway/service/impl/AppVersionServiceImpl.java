package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeecloud.adplus.dal.entity.AppVersion;
import com.yeecloud.adplus.dal.entity.QAppVersion;
import com.yeecloud.adplus.dal.repository.AppVersionRepository;
import com.yeecloud.adplus.gateway.controller.form.AppVersionForm;
import com.yeecloud.adplus.gateway.controller.vo.AppVersionVO;
import com.yeecloud.adplus.gateway.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Leonard
 * @create: 2021/11/29
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    AppVersionRepository appVersionRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Override
    public AppVersionVO detectVersion(AppVersionForm form) {
        QAppVersion qAppVersion = QAppVersion.appVersion;
        Predicate predicate = qAppVersion.deleted.eq(false)
                .and(qAppVersion.status.eq(2))
                .and(qAppVersion.updateMethod.ne(0));
        if (form.getAppId() != null) {
            predicate = ExpressionUtils.and(predicate, qAppVersion.app.appId.eq(form.getAppId()));
            // 取最新创建的版本号
            AppVersion appVersion = jpaQueryFactory
                    .selectFrom(qAppVersion)
                    .where(predicate)
                    .orderBy(qAppVersion.createdAt.desc())
                    .fetchOne();
            if (appVersion != null) {
                AppVersionVO vo = new AppVersionVO();
                String latestVersion = appVersion.getCode();
                String[] latestVerList = latestVersion.split("\\.");
                String[] formVerList = form.getVersion().split("\\.");
                // 为避免越界，取最小长度进行比较
                int indexLength = latestVerList.length <= formVerList.length ? latestVerList.length : formVerList.length;
                for (int i = 0; i < indexLength; i++) {
                    if (Integer.valueOf(latestVerList[i]) > Integer.valueOf(formVerList[i])) {
                        NewBeanUtils.copyProperties(vo, appVersion, true);
                        vo.setLatestVersion(appVersion.getCode());
                        return vo;
                    }
                }
            }
        }
        return null;
    }
}
