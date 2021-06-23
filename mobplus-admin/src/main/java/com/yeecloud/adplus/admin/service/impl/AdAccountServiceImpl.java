package com.yeecloud.adplus.admin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.ad.form.AdAccountForm;
import com.yeecloud.adplus.admin.service.AdAccountService;
import com.yeecloud.adplus.dal.entity.AdAccount;
import com.yeecloud.adplus.dal.entity.QAdAccount;
import com.yeecloud.adplus.dal.repository.AdAccountRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    public PageInfo dataPage(List dataList, AdAccountForm form) {
        if (dataList.size() > 0) {
            int startNum = (form.getPageNo() - 1) * form.getPageSize();
            int endNum = startNum + form.getPageSize();
            int resultEndNum = dataList.size();

            List resultPage = new ArrayList<>();
            if (resultEndNum > 0) {
                if (resultEndNum > endNum) {
                    resultPage = dataList.subList(startNum, endNum);
                } else {
                    resultPage = dataList.subList(startNum, resultEndNum);
                }
            }
            System.out.println(resultPage);
            JSONArray dataArray = new JSONArray();
            dataArray =  dataTransfer(resultPage, form, dataArray);
            PageInfo pageInfo = new PageInfo(form.getPageNo(), form.getPageSize(), resultEndNum, dataArray);
            return pageInfo;
        }
        return null;
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

    private JSONArray dataTransfer(List<JSONObject> dataList, AdAccountForm form, JSONArray dataArray) {

        dataList.forEach( row -> {
            JSONObject dimensionValues = row.getJSONObject("row").getJSONObject("dimensionValues");
            System.out.println(dimensionValues);
            JSONObject metricValues = row.getJSONObject("row").getJSONObject("metricValues");
            System.out.println(metricValues);

            JSONObject dataObj = new JSONObject();
            form.getDimensions().forEach( dimension -> {
                if (dimension.equals("APP") || dimension.equals("AD_UNIT")) {
                    dataObj.put(dimension, dimensionValues.getJSONObject(dimension).get("displayLabel"));
                }

            });

            form.getMetrics().forEach( metric ->{
                DecimalFormat df = new DecimalFormat("0.00");
                if (metric.equals("ESTIMATED_EARNINGS")) {
                    int microsValueL = (int) metricValues.getJSONObject(metric).get("microsValue");
                    String microsValue = df.format(microsValueL/(float)1000000);
                    dataObj.put(metric, microsValue);
                } else if (metric.equals("MATCH_RATE") || metric.equals("SHOW_RATE")
                    || metric.equals("IMPRESSION_CTR") || metric.equals("IMPRESSION_RPM")) {
                    //强转会报错
                    double doubleValueD = Double.valueOf(metricValues.getJSONObject(metric).get("doubleValue").toString());
                    String doubleValue = df.format(doubleValueD*100);
                    dataObj.put(metric, doubleValue);
                } else {
                    int integerValueL = (int) metricValues.getJSONObject(metric).get("integerValue");
                    dataObj.put(metric, integerValueL);
                }
            });

            dataArray.add(dataObj);
        });
        return dataArray;
    }
}
