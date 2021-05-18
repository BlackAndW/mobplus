package com.yeecloud.adplus.admin.service.impl;

import com.google.common.collect.Maps;
import com.querydsl.core.types.ExpressionUtils;
import com.yeecloud.adplus.admin.service.UserAdInfoService;
import com.yeecloud.adplus.admin.util.ProcessUtils;
import com.yeecloud.adplus.dal.entity.App;
import com.yeecloud.adplus.dal.entity.QUserAdInfo;
import com.yeecloud.adplus.dal.entity.UserAdInfo;
import com.yeecloud.adplus.dal.repository.AppRepository;
import com.yeecloud.adplus.dal.repository.UserAdInfoRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.DateUtils;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Leonard
 * @create: 2021/5/18
 */
@Slf4j
@Service
public class UserAdInfoServiceImpl implements UserAdInfoService {

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    UserAdInfoRepository userAdInfoRepository;

    @Autowired
    AppRepository appRepository;

    @Override
    public Page<UserAdInfo> query(Query query) throws ServiceException, ParseException {
        QUserAdInfo userAdInfo = QUserAdInfo.userAdInfo;
        Predicate predicate = userAdInfo.deleted.eq(false);
        Integer appId = query.get("appId", Integer.class);
        App app = appRepository.findById(appId).orElse(null);
        if (app != null) {
            predicate = ExpressionUtils.and(predicate, userAdInfo.appId.eq(app.getAppId()));
        }
        String userIp = query.get("userIp", String.class);
        if (userIp != null && userIp.length() > 0) {
            predicate = ExpressionUtils.and(predicate, userAdInfo.userIp.eq(userIp));
        }
        String appPositionCode = query.get("appPositionCode", String.class);
        if (appPositionCode != null && appPositionCode.length() > 0) {
            predicate = ExpressionUtils.and(predicate, userAdInfo.appPositionCode.eq(appPositionCode));
        }
        String adChannel = query.get("adChannel", String.class);
        if (adChannel != null && adChannel.length() > 0) {
            predicate = ExpressionUtils.and(predicate, userAdInfo.adChannel.eq(adChannel));
        }
        String startTimeStr = query.get("startTimeStr", String.class);
        String endTimeStr = query.get("endTimeStr", String.class);
        if (startTimeStr != null && startTimeStr.length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(startTimeStr).getTime();
            long endTime = simpleDateFormat.parse(endTimeStr).getTime();
            predicate = ExpressionUtils.and(predicate, userAdInfo.createdAt.between(startTime, endTime));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return userAdInfoRepository.findAll(predicate, pagRequest);
    }

    @Override
    public String data2excel(Query query) throws ServiceException, ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("mysql -uroot -p").append(password).append(" -e \"select ").
                append("n_user_ip as 用户ip, ").
                append("n_uuid as 设备号, ").
                append("n_app_position_code as 展示位id, ").
                append("n_ad_channel as 广告平台, ").
                append("n_ad_request_count as 请求次数, ").
                append("n_ad_show_count as 展示次数, ").
                append("n_ad_click_count as 点击次数, ").
                append("from_unixtime(n_created_at/1000, '%Y-%m-%d %H:%i:%s') as 日期 ").
                append("from t_user_ad_info ").
                append("where 1=1 ");
        Integer appId = query.get("appId", Integer.class);
        App app = appRepository.findById(appId).orElse(null);
        String appName = "";
        if (app != null){
            sb.append(" and n_app_id='").append(app.getAppId()).append("'");
            appName = app.getName();
        }
        if (query.containsKey("userIp")) {
            sb.append(" and n_user_ip=").append(query.get("userIp", String.class));
        }
        if (query.containsKey("appPositionCode")) {
            sb.append(" and n_app_position_code='").append(query.get("appPositionCode")).append("'");
        }
        if (query.containsKey("adChannel")) {
            sb.append(" and n_ad_channel='").append(query.get("adChannel")).append("'");
        }
        if (query.containsKey("startTimeStr") && query.containsKey("endTimeStr")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(query.get("startTimeStr", String.class)).getTime();
            long endTime = simpleDateFormat.parse(query.get("endTimeStr", String.class)).getTime();
            sb.append(" and n_created_at between ").append(startTime).append(" and ").append(endTime);
        }
        final String rootPath = "/www/wwwroot/admin.siteps.cn/";
        final String filePath = "output/";
        final String fileName = appName + "-" + DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + ".xls";
        sb.append(" \" mobplus_db > ").append(rootPath).append(filePath).append(fileName);
        ProcessUtils.execute(sb.toString());
        return filePath+fileName;
    }
}
