package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.ad.form.AdAccountForm;
import com.yeecloud.adplus.dal.entity.AdAccount;
import com.yeecloud.meeto.common.util.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */

public interface AdAccountService extends BaseService<AdAccount>{

    Page<AdAccount> queryList();

    PageInfo dataPage(List dataList, AdAccountForm form);
}
