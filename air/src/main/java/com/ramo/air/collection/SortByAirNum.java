package com.ramo.air.collection;


import com.ramo.air.bean.AirRank;

import java.util.Comparator;

/**
 * 复写比较 AirRank对象比较 按污染值排序 从小到大
 * 
 * @author ramo
 * 
 */
public class SortByAirNum implements Comparator<AirRank> {

	public int compare(AirRank arg0, AirRank arg1) {
		/*if (arg0.getAirnum() > arg1.getAirnum()) {
			return -1;
		}*/
		return 1;
	}
}
