package cn.itcast.service.controller;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

/**
 * 各地图API坐标转换;
 * WGS84坐标系：国际上通用的坐标系;
 * GCJ02坐标系：即火星坐标系。
 * BD-09： 百度坐标系
 */
public class PositionUtil {

    public static double pi = 3.1415926535897932384626;
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
    public static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat,mgLon};
    }
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
     * */
    public static double[] gcj02_To_Gps84(double lat, double lon) {
        double[] gps = transform(lat, lon);
        double lontitude = lon * 2 - gps[1];
        double latitude = lat * 2 - gps[0];
        return new double[]{latitude, lontitude};
    }
    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**将gps84转为bd09
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_bd09(double lat,double lon){
        double[] gcj02 = gps84_To_Gcj02(lat,lon);
        double[] bd09 = gcj02_To_Bd09(gcj02[0],gcj02[1]);
        return bd09;
    }
    public static double[] bd09_To_gps84(double lat,double lon){
        double[] gcj02 = bd09_To_Gcj02(lat, lon);
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
        //保留小数点后六位
        gps84[0] = retain6(gps84[0]);
        gps84[1] = retain6(gps84[1]);
        return gps84;
    }

    /**保留小数点后六位
     * @param num
     * @return
     */
    private static double retain6(double num){
        String result = String .format("%.6f", num);
        return Double.valueOf(result);
    }

    public static void main(String[] args) {
       double[] jd= new double[]{
               121.173444444444,
               121.173388888889,
               121.172972222222,
               121.173027777778,
               121.174055555556,
               121.174166666667,
               121.175305555556,
               121.175333333333,
               121.174361111111,
               121.174361111111,
               121.174972222222,
               121.174972222222,
               121.174361111111,
               121.174166666667,
               121.174222222222,
               121.174305555556,
               121.17525,
               121.175194444444,
               121.174472222222,
               121.174472222222,
               121.175972222222,
               121.176,
               121.177222222222,
               121.177194444444,
               121.177194444444,
               121.177222222222,
               121.177611111111,
               121.177611111111,
               121.177194444444,
               121.177055555556,
               121.177166666667,
               121.17725,
               121.177138888889,
               121.177083333333,
               121.177138888889,
               121.177194444444,
               121.177972222222,
               121.177916666667,
               121.176944444444,
               121.176972222222,
               121.178611111111,
               121.178694444444,
               121.179166666667,
               121.179194444444,
               121.180055555556,
               121.179972222222,
               121.179638888889,
               121.179666666667,
               121.18,
               121.180083333333,
               121.179694444444,
               121.179611111111,
               121.180388888889,
               121.180361111111,
               121.180722222222,
               121.180833333333,

       };
        System.out.println(jd.length);
        double[] wd= new double[]{
                31.2861388888889,
                31.2860555555556,
                31.2861111111111,
                31.2862222222222,
                31.2859444444444,
                31.2861388888889,
                31.2858611111111,
                31.28575,
                31.2859166666667,
                31.2860277777778,
                31.2859444444444,
                31.2858333333333,
                31.2860277777778,
                31.2860833333333,
                31.2861388888889,
                31.2861388888889,
                31.2858888888889,
                31.2857777777778,
                31.2858888888889,
                31.2860277777778,
                31.2855833333333,
                31.2858055555556,
                31.2856388888889,
                31.2854722222222,
                31.2855,
                31.2856111111111,
                31.2855555555556,
                31.2854444444444,
                31.2856944444444,
                31.2857222222222,
                31.2861111111111,
                31.2861111111111,
                31.2857222222222,
                31.2857222222222,
                31.2858888888889,
                31.2858611111111,
                31.2854722222222,
                31.2854166666667,
                31.2855,
                31.2856388888889,
                31.2852777777778,
                31.2853888888889,
                31.2853055555556,
                31.2851944444444,
                31.2851944444444,
                31.2850833333333,
                31.2851666666667,
                31.28525,
                31.2850555555556,
                31.2850277777778,
                31.2846666666667,
                31.2846944444444,
                31.2853055555556,
                31.2853888888889,
                31.2857777777778,
                31.2856944444444,

        };
        System.out.println(wd.length);
        if(jd.length==wd.length) {
            for (int i = 0; i < jd.length; i++) {
                double[] doubles = PositionUtil.gps84_To_bd09(wd[i], jd[i]);
                double jd1 = PositionUtil.retain6(doubles[0]);
                double wd1 = PositionUtil.retain6(doubles[1]);
                System.out.println(wd1 + "," + jd1);
            }
        }
//        double[] doubles = PositionUtil.gps84_To_bd09(31.28222222, 121.1605278);
//        System.out.println(doubles[0]);
//        System.out.println(doubles[1]);
//
  }
}



