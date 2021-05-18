package com.yeecloud.adplus.admin.controller.data;

import com.yeecloud.adplus.admin.controller.data.convert.UserAdInfoConvert;
import com.yeecloud.adplus.admin.controller.data.vo.UserAdInfoVO;
import com.yeecloud.adplus.admin.service.UserAdInfoService;
import com.yeecloud.adplus.dal.entity.UserAdInfo;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.yeecloud.meeto.common.result.Result;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/5/18
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data/appStatistics")
public class UserAdInfoController {

    @Autowired
    UserAdInfoService userAdInfoService;

    @Autowired
    UserAdInfoConvert userAdInfoConvert;

    @GetMapping
    public Result getAppStatistics(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        Page<UserAdInfo> userAdInfoPage = userAdInfoService.query(new Query(params));
        PageInfo<UserAdInfoVO> result = convert(userAdInfoPage);
        return Result.SUCCESS(result);
    }

    private PageInfo<UserAdInfoVO> convert(Page<UserAdInfo> result) {
        List<UserAdInfoVO> resultList = userAdInfoConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    @GetMapping("data2excel")
    public Result data2excel(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        String filePath = userAdInfoService.data2excel(new Query(params));
        if (filePath == null || filePath.length() == 0) {
            return Result.FAILURE("get excel file failure!");
        }
        return Result.SUCCESS(filePath);
    }
}
