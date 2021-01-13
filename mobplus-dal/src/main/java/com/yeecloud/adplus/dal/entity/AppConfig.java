package com.yeecloud.adplus.dal.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Maps;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Huang
 * @create: 2020-12-02 17:27
 */
@Data
@Entity
@Table(name = "t_app_conf")
public class AppConfig extends AuditorEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_project_id")
    private AppProject appProject;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_version_id")
    private AppVersion appVersion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_channel_id")
    private Channel channel;

    @Column(name = "c_name")
    private String name;

    @Column(name = "n_ad_switch")
    private Integer adSwitch;

    @Column(name = "n_content_switch")
    private Integer contentSwitch;

    @Column(name = "n_index_switch")
    private Integer indexSwitch;

    @Column(name = "c_conf")
    private String conf;

    @Column(name = "n_status")
    private Integer status;

    @Transient
    public Map<String, Object> getParameters() {
        if (StringUtils.isNotBlank(this.getConf())) {
            try {
                return JSON.parseObject(this.getConf(), HashMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return Maps.newHashMap();
    }
}