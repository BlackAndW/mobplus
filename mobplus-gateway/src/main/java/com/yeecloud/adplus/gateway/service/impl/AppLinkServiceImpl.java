package com.yeecloud.adplus.gateway.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.AppLink;
import com.yeecloud.adplus.dal.entity.QAppLink;
import com.yeecloud.adplus.dal.repository.AppLinkRepository;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.gateway.controller.form.AppLinkForm;
import com.yeecloud.adplus.gateway.controller.vo.AppLinkVO;
import com.yeecloud.adplus.gateway.service.AppLinkService;
import com.yeecloud.meeto.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/7/16
 */
@Service
public class AppLinkServiceImpl implements AppLinkService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    AppLinkRepository appLinkRepository;

    @Override
    public List<AppLinkVO> query(AppLinkForm form) {
        QAppLink qAppLink = QAppLink.appLink;
        Predicate predicate = qAppLink.deleted.eq(false);
        if (form.getAppId() != null && form.getAppId().length() > 0) {
            App app = appRepository.findByAppId(form.getAppId());
            predicate = ExpressionUtils.and(predicate, qAppLink.app.eq(app));
        }
        if (form.getId() != null && form.getId() > 0) {
            predicate = ExpressionUtils.and(predicate, qAppLink.id.eq(form.getId()));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pageRequest = PageRequest.of(0, 100, sort);
        List<AppLink> appLinks = appLinkRepository.findAll(predicate, pageRequest).getContent();
        List<AppLinkVO> voList = new ArrayList<>();
        appLinks.forEach(appLink -> {
            AppLinkVO vo = new AppLinkVO();
            NewBeanUtils.copyProperties(vo, appLink, true);
            voList.add(vo);
        });
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public synchronized Result updateData(AppLinkForm form) {
        if (form.getId() == null || form.getId() == 0) {
            return Result.FAILURE("id is null");
        }
        AppLink appLink = appLinkRepository.getOne(form.getId());
        if (form.getClickNum() != null && form.getClickNum() > 0) {
            appLink.setClickNum(appLink.getClickNum() + 1);
        }
        appLinkRepository.save(appLink);
        return Result.SUCCESS("upload data success!");
    }
}
