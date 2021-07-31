package com.yeecloud.adplus.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.yeecloud.adplus.dal.repository.ChargeBannerRepository;
import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.service.ChargeService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Leonard
 * @create: 2021/7/1
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/cs")
public class ChargeShowController {

    @Autowired
    ChargeService chargeService;

    @PostMapping("bannerList")
    public Result getBannerList() throws ServiceException {
        return Result.SUCCESS(chargeService.queryBanner());
    }

    @PostMapping("videoList")
    public Result getVideoList(@RequestBody String queryMap) throws ServiceException {
        JSONObject jsonMap = JSONObject.parseObject(queryMap);
        return Result.SUCCESS(chargeService.queryMaterial(new Query(jsonMap)));
    }

    @PostMapping("uploadData")
    public Result uploadData(@RequestBody ChargeShowForm form) {
        return chargeService.uploadData(form);
    }
}
