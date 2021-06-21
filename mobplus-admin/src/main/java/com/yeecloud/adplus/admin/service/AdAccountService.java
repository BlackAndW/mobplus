package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.dal.entity.AdAccount;
import org.springframework.data.domain.Page;

/**
 * @author: Leonard
 * @create: 2021/6/10
 */

public interface AdAccountService extends BaseService<AdAccount>{

    Page<AdAccount> queryList();

}
