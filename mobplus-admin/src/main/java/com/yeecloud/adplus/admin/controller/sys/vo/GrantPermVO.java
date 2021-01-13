package com.yeecloud.adplus.admin.controller.sys.vo;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Title
 * Description:
 * Date: 2019/9/15 1:05
 * Copyright (c) 2019-2099 YYSKYS YeeCloud
 *
 * @author ybbk
 */
@Data
public class GrantPermVO {
    private Boolean checkedAll = false;
    private List<Integer> selected = Lists.newArrayList();
    private Boolean indeterminate = false;
    private String name;
    private List<Action> actionsOptions = Lists.newArrayList();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Action {
        private String label;
        private Integer value;
    }
}
