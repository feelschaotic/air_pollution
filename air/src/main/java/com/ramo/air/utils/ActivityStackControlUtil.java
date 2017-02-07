package com.ramo.air.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Description: activity栈管理类，每当新产生一个activity时就加入，finish掉一个activity时就remove，这样到最后需要
 * 完全退出程序时就只要调用finishProgram方法就可以将程序完全结束。
 * @author ramo
 * @time 2015-12-05
 */
public class ActivityStackControlUtil {
	// 存放Activity的数组
	private static ArrayList<Activity> activityList = new ArrayList<Activity>();
	/**
	 * Description: finish掉一个activity时就将该Activity从activity栈中移除
	 * @param activity
	 */
	public static void remove(Activity activity){
		activityList.remove(activity);
	}
	/**
	 * Description: 向activity栈中加入Activity
	 * @param activity 
	 */
	public static void add(Activity activity){
		activityList.add(activity);
	}
	/**
	 * Description: 结束整个程序
	 */
	public static void finishProgram(){
		// 结束所有未关闭的Activity
		for(Activity activity : activityList){
			activity.finish();
		}
		// 结束当前进程
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
