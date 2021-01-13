package com.yeecloud.adplus.admin.controller.cms.form;

import com.yeecloud.adplus.dal.entity.Talk;
import lombok.Data;

@Data
public class TalkContentForm {
    private Talk talk;

    private Integer key;

    private String value;

    private Integer sort;
}
