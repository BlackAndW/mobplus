package com.yeecloud.adplus.gateway.controller.form;

import io.github.yedaxia.apidocs.Ignore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
@Data
@RequiredArgsConstructor
public class TranslateForm implements Serializable {

    private static final long serialVersionUID = 0L;

    /** 源文本（字符串） */
    @Ignore
    String sourceString;

    /** 源文本（列表） */
    @Ignore
    List<String> sourceList;

    /** 源语言 非必传*/
    @Ignore
    String fromLang;

    /** ---目标语言 */
    String toLang;

    /** 针对简单字符串文本的构造器 */
    public TranslateForm(String sourceString, String toLang) {
        this.sourceString = sourceString;
        this.toLang = toLang;
    }
}
