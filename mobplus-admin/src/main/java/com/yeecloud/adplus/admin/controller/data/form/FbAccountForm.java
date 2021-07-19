package com.yeecloud.adplus.admin.controller.data.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Leonard
 * @create: 2021/7/14
 */
@Data
public class FbAccountForm {

    @NotBlank(message = "账号为空")
    private String accountId;

    private String level;

    private String dateBefore;

    private String[] dateRange;

    private int pageNo;

    private int pageSize;
}
