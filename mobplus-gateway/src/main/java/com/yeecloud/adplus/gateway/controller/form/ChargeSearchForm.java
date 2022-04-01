package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2022/3/25
 */

@Data
public class ChargeSearchForm {

    String labelStr = "";

    String appId = "";

    int style = 1;

    int pageNo = 0;

    int pageSize = 20;
}
