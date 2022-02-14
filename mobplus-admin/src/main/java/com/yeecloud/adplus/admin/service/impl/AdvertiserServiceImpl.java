package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.ad.form.AdvertiserForm;
import com.yeecloud.adplus.admin.service.AdvertiserService;
import com.yeecloud.adplus.dal.entity.AdTest;
import com.yeecloud.adplus.dal.entity.Advertiser;
import com.yeecloud.adplus.dal.entity.QAdvertiser;
import com.yeecloud.adplus.dal.repository.AdTestRepository;
import com.yeecloud.adplus.dal.repository.AdvertiserRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Huang
 * @create: 2020-12-08 15:40
 */
@Slf4j
@Service
public class AdvertiserServiceImpl implements AdvertiserService {

    @Autowired
    private AdvertiserRepository advertiserRepository;

    @Autowired
    private AdTestRepository adTestRepository;

    @Override
    public Page<Advertiser> query(Query query) throws ServiceException {
        try {
            QAdvertiser advertiser = QAdvertiser.advertiser;
            Predicate predicate = advertiser.deleted.eq(false);

            String keyword = (String) query.get("keyword");
            if (StringUtils.isNotBlank(keyword)) {
                String express = keyword.concat("%");
                Predicate or = ExpressionUtils.or(advertiser.name.like(express), advertiser.code.like(express));
                predicate = ExpressionUtils.and(predicate, or);
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            return advertiserRepository.findAll(predicate, pagRequest);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Advertiser findById(Integer id) throws ServiceException {
        try {
            Advertiser entity = advertiserRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                return entity;
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Advertiser findByCode(String code) throws ServiceException {
        try {
            Advertiser entity = advertiserRepository.findFirstByCode(code);
            if (entity != null && !entity.isDeleted()) {
                return entity;
            }
            return null;
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(AdvertiserForm form) throws ServiceException {
        try {
            Advertiser advertiser = new Advertiser();
            NewBeanUtils.copyProperties(advertiser, form, true);
            advertiserRepository.save(advertiser);
            if (StringUtils.isNotBlank(form.getTestId1())) {
                saveAdTest(advertiser, 1, form.getTestId1());
            } else if (StringUtils.isNotBlank(form.getTestId2())) {
                saveAdTest(advertiser, 2, form.getTestId2());
            } else if (StringUtils.isNotBlank(form.getTestId3())) {
                saveAdTest(advertiser, 3, form.getTestId3());
            } else if (StringUtils.isNotBlank(form.getTestId4())) {
                saveAdTest(advertiser, 4, form.getTestId4());
            } else if (StringUtils.isNotBlank(form.getTestId5())) {
                saveAdTest(advertiser, 5, form.getTestId5());
            } else if (StringUtils.isNotBlank(form.getTestId6())) {
                saveAdTest(advertiser, 6, form.getTestId6());
            } else if (StringUtils.isNotBlank(form.getTestId7())) {
                saveAdTest(advertiser, 7, form.getTestId7());
            } else if (StringUtils.isNotBlank(form.getTestId8())) {
                saveAdTest(advertiser, 8, form.getTestId8());
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, AdvertiserForm form) throws ServiceException {
        try {
            Advertiser entity = advertiserRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                advertiserRepository.save(entity);
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer[] ids) throws ServiceException {
        advertiserRepository.deleteById(ids);
    }

    private void saveAdTest(Advertiser advertiser, Integer adType, String testId) {
        AdTest adTest = new AdTest();
        adTest.setAdvertiser(advertiser);
        adTest.setAdType(adType);
        adTest.setTestId(testId);
        adTestRepository.save(adTest);
    }
}
