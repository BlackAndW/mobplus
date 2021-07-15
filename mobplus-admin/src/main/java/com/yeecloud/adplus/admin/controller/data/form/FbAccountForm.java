package com.yeecloud.adplus.admin.controller.data.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/7/14
 */
@Data
public class FbAccountForm {

    private String accountId;

    private String level;

    private String dateBefore;

    private int pageNo;

    private int pageSize;
}
