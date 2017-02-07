package com.ramo.air.utils;

import android.util.Log;
import android.widget.Toast;

import com.ramo.air.application.MyApplication;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

public class JuheDataUtils {
	//空气质量id
	public static final int DATANUM = 33;
	//反向地理编码id
	public static final int DATANUM2 = 15;
	private static String respStr;

	/**
	 * 请不要添加key参数.
	 */


	public static String sendRequest(String city) {

		
		Parameters parame = new Parameters();
		parame.add("city", city);
		/**
		 * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
		 * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
		 * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
		 * 
		 */
		JuheData.executeWithAPI(MyApplication.getContext(), DATANUM,
				Constants.JUHE_AQIAPI, JuheData.GET, parame, new DataCallBack() {


					/**
					 * 请求成功时调用的方法 statusCode为http状态码,responseString *为请求返回数据.
					 */
					@Override
					public void onSuccess(int statusCode, String responseString) {
						success(responseString);
					}

					/**
					 * 请求完成时调用的方法,无论成功或者失败都会调用.
					 */
					@Override
					public void onFinish() {
						Log.e("juhe finish","调用结束");
					}

					/**
					 * 请求失败时调用的方法,statusCode为http状态码,throwable为捕获到的异常
					 * statusCode:30002 没有检测到当前网络. 30003 没有进行初始化. 0
					 * 未明异常,具体查看Throwable信息. 其他异常请参照http状态码.
					 */
					@Override
					public void onFailure(int statusCode,
							String responseString, Throwable throwable) {
						Log.e("juhe error",throwable+"");
						if (statusCode == 30002)
							Toast.makeText(MyApplication.getContext(),
									"网络异常，请刷新重试", Toast.LENGTH_SHORT).show();
						else if (statusCode == 30003)
							Toast.makeText(MyApplication.getContext(), "没有初始化",
									Toast.LENGTH_SHORT).show();
					}
				});
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.e("juhe respStr", respStr+"");
		return respStr;
	}
	
	private static void success(String responseString) {
		Log.e("juhe responseString", responseString+"");
		respStr=responseString;
	}
}
