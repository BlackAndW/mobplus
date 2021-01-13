package com.yeecloud.adplus.dal.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author: Huang
 * @create: 2020-12-03 14:26
 */
@Data
@Entity
@Table(name = "t_device")
@EntityListeners(AuditingEntityListener.class)
public class Device extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "c_app_id")
    private String appId;

    /**
     * 设备UUID
     */
    @Column(name = "c_uuid")
    private String uuid;

    /**
     * 操作系统
     */
    @Column(name = "c_os")
    private String os;

    /**
     * 操作系统版本
     */
    @Column(name = "c_osv")
    private String osv;

    /**
     * 1:手机 2:平板
     */
    @Column(name = "n_dev_type")
    private Integer devType;

    /**
     * 设备品牌
     */
    @Column(name = "c_dev_brand")
    private String devBrand;

    /**
     * 设备型号
     */
    @Column(name = "c_dev_model")
    private String devModel;

    /**
     * 设备厂商
     */
    @Column(name = "c_dev_make")
    private String devMake;

    /**
     * FingerPrint
     */
    @Column(name = "c_finger_print")
    private String fingerPrint;

    /**
     * AndroidID
     */
    @Column(name = "c_android_id")
    private String androidId;

    /**
     * GAID
     */
    @Column(name = "c_gaid")
    private String gaid;

    /**
     * IMEI
     */
    @Column(name = "c_imei")
    private String imei;

    /**
     * IMSI
     */
    @Column(name = "c_imsi")
    private String imsi;

    /**
     * ICCID
     */
    @Column(name = "c_iccid")
    private String iccid;

    /**
     * 内置SDK版本
     */
    @Column(name = "c_sdk_version")
    private String sdkVersion;

    /**
     * 应用版本
     */
    @Column(name = "c_pkg_version")
    private String pkgVersion;

    /**
     * 内置应用包名
     */
    @Column(name = "c_pkg_name")
    private String pkgName;

    /**
     * 内置应用名
     */
    @Column(name = "c_app_name")
    private String appName;

    /**
     * 宽x高
     */
    @Column(name = "c_screen")
    private String screen;

    /**
     * 屏幕方向 0:未知；1竖屏；2、横屏
     */
    @Column(name = "c_orien")
    private Integer orien;

    /**
     * BSSID
     */
    @Column(name = "c_bssid")
    private String bssid;

    /**
     * MAC
     */
    @Column(name = "c_mac")
    private String mac;

    /**
     * 网络类型 0:未知 1:WiFi 2:2G 3:3G 4:4G
     */
    @Column(name = "c_network")
    private Integer network;

    /**
     * 网络扩展
     */
    @Column(name = "c_network_extra")
    private String networkExtra;

    /**
     * 基站信息
     */
    @Column(name = "c_lac")
    private String lac;

    /**
     * GPS 纬度
     */
    @Column(name = "n_gps_lat")
    private Double gpsLat;

    /**
     * GPS 经度
     */
    @Column(name = "n_gps_lng")
    private Double gpsLng;

    /**
     * GPS 时间
     */
    @Column(name = "n_gps_timestamp")
    private Long gpsTimestamp;


    @Column(name = "c_country")
    private String country;


    @Column(name = "c_region")
    private String region;


    @Column(name = "c_city")
    private String city;

    /**
     * 权限信息 1:sd 2:安装 4:GPS
     */
    @Column(name = "n_perm")
    private Integer perm;

    /**
     * 是否锁屏 1:锁 0:未锁
     */
    @Column(name = "n_screen_lock")
    private Integer screenLock;

    /**
     * 最近IP
     */
    @Column(name = "c_remote_ip")
    private String remoteIp;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "n_modified_at")
    protected Long modifiedAt;
}
