package com.yeecloud.adplus.admin.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.service.AdAccountService;
import com.yeecloud.adplus.dal.entity.AdAccount;
import com.yeecloud.adplus.dal.entity.QAdAccount;
import com.yeecloud.adplus.dal.repository.AdAccountRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */

@Service
public class AdAccountServiceImpl implements AdAccountService {

    @Resource
    AdAccountRepository adAccountRepository;

    @Override
    public Page<AdAccount> queryList() {
        QAdAccount adAccount = QAdAccount.adAccount;
        Predicate predicate = adAccount.deleted.eq(false);
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pageRequest = PageRequest.of(0, 20, sort);
        return adAccountRepository.findAll(predicate, pageRequest);
    }

    @Override
    public Page<AdAccount> query(Query query) throws ServiceException {
        return null;
    }

//    @Override
//    public AdAccount queryById(String accountId) {
//        QAdAccount adAccount = QAdAccount.adAccount;
//        Predicate predicate = adAccount.deleted.eq(false);
//        predicate = ExpressionUtils.and(predicate, adAccount.accountId.eq(accountId));
//        return adAccountRepository.findOne(predicate).orElse(null);
//    }

    @Override
    public AdAccount findById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void create(AdAccount form) throws ServiceException {

    }

    @Override
    public void update(Integer id, AdAccount form) throws ServiceException {

    }

    @Override
    public void delete(Integer[] ids) throws ServiceException {

    }
}
