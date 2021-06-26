package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
@Data
public class TranslateForm implements Serializable {

    private static final long serialVersionUID = 0L;

    String sourceString;

    List<String> sourceList;

    String fromLang;

    String toLang;
}
