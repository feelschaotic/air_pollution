package com.ramo.air.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.ramo.air.bean.AirQuality;
import com.ramo.air.utils.L;
import com.ramo.air.utils.MyPreferenceUtils;
import com.ramo.air.utils.VoiceUtils;

import java.util.Date;


public class VoiceAirService extends Service {

	static StringBuffer voiceText;
	static AirQuality air_composition;
	static{
		L.e("static初始化");
		voiceText=new StringBuffer();
		air_composition = (AirQuality) MyPreferenceUtils
				.readObject("air_composition");
		
		voiceText.append("您所在的城市:");
		voiceText.append(air_composition.getCity());
		voiceText.append(",今天的空气指数:");
		voiceText.append(air_composition.getAQI());
		voiceText.append(",空气质量等级:");
		voiceText.append(air_composition.getQuality());
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				L.e("定时任务开始"+voiceText.toString());
				if(new Date().getHours()==8){//早上八点播报
					VoiceUtils.beginSpeak(voiceText.toString());
				}
			}
		}).start();
		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour=60*60*1000;//这是一小时的毫秒数
		//int anHour=20*1000;//这是一小时的毫秒数
		
		long triggerAtMillis= SystemClock.elapsedRealtime()+anHour;
		Intent i=new Intent(this,VoiceAirReceiver.class);
		
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				triggerAtMillis, pi);
		

		return super.onStartCommand(intent, flags, startId);
	}

}
