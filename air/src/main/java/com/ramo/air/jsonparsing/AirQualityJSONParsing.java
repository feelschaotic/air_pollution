package com.ramo.air.jsonparsing;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

public class AirQualityJSONParsing  implements Serializable{
	static String textJson="{"+
    "'resultcode': '200',"+
    "'reason': 'SUCCESSED!',"+
    "'error_code': 0,"+
    "'result': ["+
        "{"+
           " 'citynow': {"+
              "  'city': '苏州',"+
               " 'AQI': '73',"+
                "'quality': '良',"+
                "'date': '2016-01-10 19:00'"+
            "},"+
            "'lastTwoWeeks': {"+
                "'1': {"+
                   " 'city': '苏州',"+
                   " 'AQI': '107',"+
                   " 'quality': '轻度污染',"+
                    "'date': '2015-12-12'"+
               " },"+
                "'2': {"+
                    "'city': '苏州',"+
                   " 'AQI': '172',"+
                   " 'quality': '中度污染',"+
                   " 'date': '2016-01-09'"+
                "}"+
            "},"+
            "'lastMoniData': {"+
                "'1': {"+
                   " 'city': '苏州工业园区',"+
                    "'AQI': '78',"+
                   " 'America_AQI': '134',"+
                   " 'quality': '良',"+
                  "  'PM2.5Hour': '57',"+
                   " 'PM2.5Day': '57',"+
                   " 'PM10Hour': '88',"+
                   " 'lat': '31.309722',"+
                   " 'lon': '120.669167'"+
                "},"+
               " '2': {"+
                  "  'city': '相城区',"+
                   " 'AQI': '65',"+
                  "  'America_AQI': '114',"+
                   " 'quality': '良',"+
                  "  'PM2.5Hour': '47',"+
                  "  'PM2.5Day': '47',"+
                  "  'PM10Hour': '75',"+
                   " 'lat': '31.370833',"+
                  "  'lon': '120.640556'"+
               " }"+
           " }"+
       " }"+
   " ]"+
"}";


	public static void parsingAirQuality(String jsonStr) {
		/*
		 * 为了避免使用Gson时遇到locale影响Date格式的问题，
		 * 使用GsonBuilder来创建Gson对象，
		 * 在创建过程中调用GsonBuilder.setDateFormat(String)指定一个固定的格式即可。
		 */
		Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		textJson = textJson.replaceAll("\"", "\\\\\"");
		textJson = textJson.replaceAll("'", "\'");
		
		AirParseBean airParse = gson.fromJson(jsonStr, AirParseBean.class);
		/*if(airParse.getResultcode()=="200"){
			
		}*/
		Log.e("juhe0!!!!",airParse.toString());
		List<AirResultParseBean> list = airParse.getResult();
		for(AirResultParseBean a:list){
		Log.e("juhe1!!!!",a.getCitynow().getCity()+":"+a.getCitynow().getAQI());
		for(Integer aq:a.getLastTwoWeeks().keySet())
		Log.e("juhe2!!!!",a.getLastTwoWeeks().get(aq).getCity()+":"+a.getLastTwoWeeks().get(aq).getAQI());
		for(Integer aq:a.getLastMoniData().keySet())
			Log.e("juhe3!!!!",a.getLastMoniData().get(aq).getCity()+":"+a.getLastMoniData().get(aq).getPM10Hour());
		}
		Log.e("juhe4!!!!",airParse.getResultcode());
	}
}
