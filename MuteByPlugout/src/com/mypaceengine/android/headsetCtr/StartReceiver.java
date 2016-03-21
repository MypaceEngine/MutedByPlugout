package com.mypaceengine.android.headsetCtr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		SharedPreferences pref=Util.getPreferences(arg0);
		if(pref.getBoolean(Util.BOOTUP_KEY, false)){
			if(!Util.isServiceRunning(arg0, HeadSetService.class)){
				Intent intent = new Intent(arg0, HeadSetService.class);
				arg0.startService(intent);
			}
		}
	}
}
