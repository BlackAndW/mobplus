package com.yeecloud.adplus.admin.controller.ad.form;

import lombok.Data;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/11
 */
@Data
public class AdAccountForm {

    private String accountId;

    private List<String> dimensions;

    private List<String> metrics;

    private String domain;

    private String dateBefore;

    private String[] dateRange;

    private int pageNo;

    private int pageSize;
}
