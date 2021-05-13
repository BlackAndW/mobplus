package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.QUserAdInfo;
import com.yeecloud.adplus.dal.entity.UserAdInfo;
import com.yeecloud.adplus.dal.repository.UserAdInfoRepository;
import com.yeecloud.adplus.gateway.controller.form.UserAdInfoForm;
import com.yeecloud.adplus.gateway.service.UserAdInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */

@Service
@Slf4j
public class UserAdInfoServiceImpl implements UserAdInfoService {

    @Autowired
    UserAdInfoRepository userAdInfoRepository;

    static final long DAY = 1000*60*60*24;

    @Async
    @Override
    @Transactional
    public synchronized void createOrUpdateInfo(UserAdInfoForm userForm, String userIp) {
        log.info("用户广告统计：" + userForm.toString());
        try {
            //临时去除为null的数据
            if (userForm.getAppId() == null) {
                return;
            }
            QUserAdInfo userAdInfo = QUserAdInfo.userAdInfo;
            Predicate predicate = userAdInfo.uuid.eq(userForm.getUuid());
            if (userForm.getAppPositionCode() != null) {
                predicate = ExpressionUtils.and(predicate, userAdInfo.appPositionCode.eq(userForm.getAppPositionCode()));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(0, 1, sort);
            List<UserAdInfo> UserAdInfoList = userAdInfoRepository.findAll(predicate, pagRequest).getContent();

            //判断是否需要新增一条记录
            UserAdInfo userAdInfoPO = setCountValue(isNewRecord(UserAdInfoList, userForm), userForm, userIp);

            userAdInfoRepository.save(userAdInfoPO);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    private UserAdInfo isNewRecord(List<UserAdInfo> userAdInfoList, UserAdInfoForm form) {
        UserAdInfo userAdInfo = new UserAdInfo();
        if (userAdInfoList != null && userAdInfoList.size() > 0 )
            userAdInfo = userAdInfoList.get(0);

        if (!form.getUuid().equals(userAdInfo.getUuid()))
            return new UserAdInfo();

        if (form.getAppPositionCode() != null  && !form.getAppPositionCode().equals(userAdInfo.getAppPositionCode()))
            return new UserAdInfo();

        //超过一天的记录会清零，新增
        if (form.isClear())
            return new UserAdInfo();
        else if (System.currentTimeMillis() - userAdInfo.getCreatedAt() > DAY)
            return new UserAdInfo();

        return userAdInfo;
    }

    /***
     * 统计的是总次数，后台无需计算
     * createdAt采用app传过来的时间
     * @param userAdInfo
     * @param userForm
     * @param userIp
     * @return
     */
    private UserAdInfo setCountValue(UserAdInfo userAdInfo, UserAdInfoForm userForm, String userIp) {
        NewBeanUtils.copyProperties(userAdInfo, userForm);
        userAdInfo.setUserIp(userIp);
        return userAdInfo;
    }
}
