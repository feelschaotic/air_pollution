package com.ramo.air.application;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.ramo.air.bean.AirQuality;
import com.ramo.air.utils.HotCityUtils;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    private static Context context;
    private static final String TAG = "JPush";
    public static Map<String, AirQuality> mMainMap;
    private static MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        JuheSDKInitializer.initialize(getApplicationContext());// 调用聚合数据的SDK

        mMainMap = new HashMap<String, AirQuality>();
        HotCityUtils.copyDB(this);// 程序第一次运行将数据库copy过去
        //推送
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        mMyApplication = this;
        //讯飞 语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=568d041f");
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return mMyApplication;
    }
}
