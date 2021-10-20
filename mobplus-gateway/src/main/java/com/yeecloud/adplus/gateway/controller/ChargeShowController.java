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
     * @param toLang 语言简称,例如:zh, en
     */
    @ApiDoc
    @GetMapping("type")
    public Result<List<ChargeMTypeVO>> getType(@RequestParam Integer style, @RequestParam(required = false) String toLang) {
        List<ChargeMTypeVO> result = chargeService.queryTypeList(style, toLang);
        return Result.SUCCESS(result);
    }

    /**
     * 获取素材列表
     * @param pageNo 【body参数】页码 起始页为0
     * @param type 【body参数】类型id ('全部'类型的typeId为1, 壁纸的为100)
     * @param style 【body参数】素材类型 1:视频(默认), 2:壁纸
     * @return
     * @throws ServiceException
     */
    @ApiDoc
    @PostMapping("materialList")
    public Result<List<ChargeMaterialVO>> getMaterialList(@RequestBody ChargeShowForm form,
                                                          @RequestParam(required = true, defaultValue = "0") Integer pageNo,
                                                          @RequestParam(required = true, defaultValue = "0") Integer type,
                                                          @RequestParam(required = true, defaultValue = "0") Integer style) throws ServiceException {
        return Result.SUCCESS(chargeService.queryMaterial(form));
    }

    /**
     * 更新素材数据
     * @param id 【body参数】素材id
     * @param showNum 【body参数】展示次数 传0或1
     * @param useNum 【body参数】使用次数 传0或1
     * @return
     */
    @ApiDoc
    @PostMapping("uploadData")
    public Result<String> uploadData(@RequestBody ChargeShowForm form,
                                     @RequestParam(required = true, defaultValue = "0") Integer id,
                                     @RequestParam(required = true, defaultValue = "0") Integer showNum,
                                     @RequestParam(required = true, defaultValue = "0") Integer useNum) {
        return chargeService.uploadDataV(form);
    }

}
