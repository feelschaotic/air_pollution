package com.ramo.air.utils;

/**
 * 计算地球上任意两点(经纬度)距离
 * 
 * @param long1
 *            第一点经度
 * @param lat1
 *            第一点纬度
 * @param long2
 *            第二点经度
 * @param lat2
 *            第二点纬度
 * @return 返回距离 单位：米
 */

/*
 * 地球是一个近乎标准的椭球体，它的赤道半径为6378.140千米，极半径为 6356.755千米，
 * 平均半径6371.004千米。如果我们假设地球是一个完美的球体，那么它的半径就是地球的平均半径，
 * 记为R。如果以0度经线为基 准，
 * 那么根据地球表面任意两点的经纬度就可以计算出这两点间的地表距离
 * （这里忽略地球表面地形对计算带来的误差，仅仅是理论上的估算值）。
 * 设第一点A的经 纬度为(LonA, LatA)，第二点B的经纬度为(LonB, LatB)，
 * 按照0度经线的基准，东经取经度的正值(Longitude)，西经取经度负值(-Longitude)，
 * 北纬取90-纬度值(90- Latitude)，南纬取90+纬度值(90+Latitude)，
 * 则经过上述处理过后的两点被计为(MLonA, MLatA)和(MLonB, MLatB)。
 * 那么根据三角推导，可以得到计算两点距离的如下公式：
 * C = sin(MLatA)*sin(MLatB)*cos(MLonA-MLonB) + cos(MLatA)*cos(MLatB)
 * Distance = R*Arccos(C)*Pi/180
 */
public class GeoDistanceUtil {

	public static double Distance(double lon1, double lat1, double lon2,
			double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (lon1 - lon2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2* R* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)* Math.cos(lat2) * sb2 * sb2));
		return d;
	}
}
