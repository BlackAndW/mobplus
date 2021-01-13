package com.yeecloud.adplus.dal.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Maps;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用
 *
 * @author: Huang
 * @create: 2020-12-02 10:02
 */
@Data
@Entity
@Table(name = "t_app")
public class App extends AuditorEntity {
    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_pkg_name")
    private String pkgName;

    @Column(name = "c_app_id")
    private String appId;

    @Column(name = "n_runtime")
    private Integer runtime;

    @Column(name = "n_type")
    private Integer type;

    @Column(name = "c_icon_path")
    private String iconPath;

    @Column(name = "c_remark")
    private String remark;

    @Column(name = "n_status")
    private Integer status;

    /**
     * 配置 json
     */
    @Column(name = "c_conf")
    private String conf;


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

