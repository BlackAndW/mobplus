package com.yeecloud.adplus.gateway.controller.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/10/9
 */
@Data
public class FeedbackForm {

    /** ---满意度1(满意)~4(不满意) */
    private Integer evaluate;

    /** ---不准确的类型 */
    private String errType;

    /** ---意见文本 */
    private String suggestion;
}
