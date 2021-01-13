package com.yeecloud.adplus.gateway.controller.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备信息
 *
 * @author: Huang
 * @create: 2020-11-30 11:27
 */
@Data
public class DeviceForm implements Serializable {

    private static final long serialVersionUID = 0L;
    protected String appId;
    protected String projectCode;
    protected String channel;

    protected String uuid;
    protected String os;
    protected String osv;
    @JSONField(name = "devType")
    protected boolean tablet;
    protected String devBrand;
    protected String devModel;
    protected String devMake;
    @JSONField(name = "fp")
    protected String fingerPrint;
    protected String gaid;
    protected String deviceId;
    protected String androidId;
    protected int width;
    protected int height;
    @JSONField(name = "orientation")
    protected int orien;
    protected int network;
    protected String mac;
    protected String bssid;
    @JSONField(name = "extra")
    protected String networkExtra;
    protected String imei;
    protected String imsi;
    protected String iccid;
    @JSONField(name = "sdv")
    protected String sdkVersion;
    protected String lac;
    protected String pkgVersion;
    protected String pkgName;
    protected String appName;
    @JSONField(name = "lat")
    protected double gpsLat;
    @JSONField(name = "lng")
    protected double gpsLng;
    @JSONField(name = "timestamp")
    protected long gpsTimestamp;
    protected int perm;
    protected int screenLock;

    protected String remoteIp;
}

