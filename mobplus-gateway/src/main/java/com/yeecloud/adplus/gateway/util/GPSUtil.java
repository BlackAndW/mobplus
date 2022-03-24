package com.yeecloud.adplus.gateway.util;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author: Leonard
 * @create: 2021/6/23
 */
public class GPSUtil {

    /**
     * 经纬度转化成弧度
     * @param d 经度/纬度
     * @return
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 和百度地图出入有点大，暂时放弃
     * 计算两个坐标点之间的距离
     * @param firstLatitude   第一个坐标的纬度
     * @param firstLongitude  第一个坐标的经度
     * @param secondLatitude  第二个坐标的纬度
     * @param secondLongitude 第二个坐标的经度
     * @return 返回两点之间的距离，单位：公里/千米
     */
    public static double getDistanceGG(double firstLatitude, double firstLongitude,
                                     double secondLatitude, double secondLongitude) {
        double firstRadLat = rad(firstLatitude);
        double firstRadLng = rad(firstLongitude);
        double secondRadLat = rad(secondLatitude);
        double secondRadLng = rad(secondLongitude);

        double a = Math.abs(firstRadLat - secondRadLat);
        double b = Math.abs(firstRadLng - secondRadLng);
        /**
         * 地球半径
         */
        double earthRadius = 6378.137;
        double cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(firstRadLat)
                * Math.cos(secondRadLat) * Math.pow(Math.sin(b / 2), 2))) * earthRadius;
        double result = Math.round(cal * 10000d) / 10000d;
        return result;
    }

    /***
     *
     * @param firstPoint 第一坐标点（源点）
     * @param secondPoint 第二坐标点（目标点）
     * @return
     */
    public static double GetPointDistance(String firstPoint, String secondPoint) {
        String[] firstArray = firstPoint.split(",");
        String[] secondArray = secondPoint.split(",");
        if (firstArray[0].matches("\\d+\\.\\d+") && firstArray[1].matches("\\d+\\.\\d+") &&
                secondArray[0].matches("\\d+\\.\\d+") && secondArray[1].matches("\\d+\\.\\d+")) {
            double firstLatitude = Double.valueOf(firstArray[0].trim());
            double firstLongitude = Double.valueOf(firstArray[1].trim());
            double secondLatitude = Double.valueOf(secondArray[0].trim());
            double secondLongitude = Double.valueOf(secondArray[1].trim());
            double distance = getDistance(firstLatitude, firstLongitude, secondLatitude, secondLongitude);
            // 将单位/米 转换为公里/千米，并保留三位小数
            DecimalFormat df = new DecimalFormat("0.000");
            distance = Double.parseDouble(df.format(distance/1000d));
            return distance;
        }
        return 0;
    }


    public static double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo) {
        GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
        GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);

        return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }
}
