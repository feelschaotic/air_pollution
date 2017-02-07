package com.ramo.air.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VoiceAirReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i=new Intent(context,VoiceAirService.class);
		context.startService(i);
	}

}
