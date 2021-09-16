package com.yeecloud.adplus.gateway.controller;

import com.yeecloud.adplus.gateway.controller.form.ChargeShowForm;
import com.yeecloud.adplus.gateway.controller.vo.ChargeBannerVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMTypeVO;
import com.yeecloud.adplus.gateway.controller.vo.ChargeMaterialVO;
import com.yeecloud.adplus.gateway.service.ChargeService;
import com.yeecloud.adplus.gateway.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充电秀
 * @author: Leonard
 * @create: 2021/7/1
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cs")
public class ChargeShowController {

    @Autowired
    ChargeService chargeService;

    /***
     * 获取banner
     * @throws ServiceException
     */
    @ApiDoc
    @PostMapping("bannerList")
    public Result<List<ChargeBannerVO>> getBannerList() throws ServiceException {
        return Result.SUCCESS(chargeService.queryBanner());
    }

    /**
     * 获取类型列表
     * @param style 1:视频;2:壁纸
     */
    @ApiDoc
    @GetMapping("type")
    public Result<List<ChargeMTypeVO>> getType(@RequestParam Integer style) {
        List<ChargeMTypeVO> result = chargeService.queryTypeList(style);
        return Result.SUCCESS(result);
    }

    /**
     * 获取素材列表
     * @description 入参: [ pageNo, type ,style ] ('全部'类型的typeId为1, 壁纸的为100)
     * @param form
     * @return
     * @throws ServiceException
     */
    @ApiDoc
    @PostMapping("videoList")
    public Result<List<ChargeMaterialVO>> getMaterialList(@RequestBody ChargeShowForm form) throws ServiceException {
        return Result.SUCCESS(chargeService.queryMaterial(form));
    }

    /**
     * 更新素材数据
     * @description 入参: [ vid, showNum, useNum ]
     * @param form
     * @return
     */
    @ApiDoc
    @PostMapping("uploadData")
    public Result<String> uploadData(@RequestBody ChargeShowForm form) {
        return chargeService.uploadDataV(form);
    }

}
