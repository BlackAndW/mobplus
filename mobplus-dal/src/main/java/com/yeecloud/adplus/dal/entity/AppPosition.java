package com.yeecloud.adplus.dal.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Maps;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-12-08 17:13
 */
@Data
@Entity
@Table(name = "t_app_position")
public class AppPosition extends AuditorEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 所属应用 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    /** 代码  APP通过这个代码拉取配置 */
    @Column(name = "c_code")
    private String code;

    /** 展示位名称 */
    @Column(name = "c_name")
    private String name;

    /**
     * 广告限制展示次数，全局配置开关
     */
    @Column(name = "n_limit_show_config")
    private Integer limitShowConfig = 0;

    /**
     * 广告限制点击次数，全局配置开关
     */
    @Column(name = "n_limit_click_config")
    private Integer limitClickConfig = 0;

    @Column(name = "n_app_version_list")
    private String appVersionCheckList;

    @Column(name = "n_channel_list")
    private String channelCheckList;

    @Column(name = "n_status")
    private Integer status;

    @OneToMany(mappedBy = "appPosition")
    private List<AppPositionAdPosition> adPosList;

    /** 展示位配置 */
    @Column(name = "c_params")
    private String params;


    @Transient
    public Map<String, Object> getParameters() {
        if (StringUtils.isNotBlank(this.params)) {
            try {
                return JSON.parseObject(this.params, HashMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return Maps.newHashMap();
    }
}

