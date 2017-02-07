package com.ramo.air.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @time 2015.12.04
 * @author ramo
 *
 * @param <T>
 */
public class JsonUtils<T> {
	public  static Gson gson;
	static{
		gson=new Gson();
	}
	/**
	 * json转对象
	 * @param json
	 * @param classz
	 * @return
	 */
	public T JsonToObj(String json,Class classz){
		T obj=(T) gson.fromJson(json, classz);
		return obj;
	}
	/**
	 * json转对象数组
	 * @param json
	 * @param classz 
	 * @param t
	 * @return
	 */
	public List<T> JsonToArray(String json,Class classz){
		TypeToken<ArrayList<T>> typeToken = new TypeToken<ArrayList<T>>() {};
		List<T> list=gson.fromJson(json, typeToken.getType());
		return list;
	}
}
