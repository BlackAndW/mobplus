package com.yeecloud.adplus.gateway.controller.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/6/28
 */

@Data
public class ChargeMaterialVO {

    /** video id */
    private Integer id;

    /** 类型id */
    private Integer typeId;

    /** 类型名 */
    private String typeName;

    /** 视频路径 */
    private String videoPath;

    /** 预览视频路径 */
    private String videoIntroduce;

    /** 视频封面 */
    private String videoCover;

    /** 视频封面缩略图 */
    private String videoCoverThumb;

    /** 壁纸图片地址 */
    private String imgUrl;

    /** 壁纸 缩略图 */
    private String thumbUrl;

    /** 虚拟使用次数(前端展示用) */
    private String useNumFake;

    /** 用户使用限制(会员)，1:限制; 2:无限制 */
    private Integer useLimit;

    /** 素材类型 1:视频; 2:壁纸 */
    private Integer style;
}
