package com.mypaceengine.android.headsetCtr;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Util {
	static String ENABLE_KEY="ENABLE_KEY";
	static String BOOTUP_KEY="BOOTUP_KEY";
	static String MANER_KEY="MANER_KEY";

	static public boolean isServiceRunning(Context c, Class<?> cls) {
		ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningService = am.getRunningServices(Integer.MAX_VALUE);
		for (RunningServiceInfo i : runningService) {
			if (cls.getName().equals(i.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	static SharedPreferences getPreferences(Context c){
		return	c.getSharedPreferences("HeadSetJack", Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
	}
	static Editor getEditor(Context c){
		SharedPreferences pref =getPreferences(c);
		Editor e = pref.edit();
		return e;
	}
	static void writeBoolean(Context c,String key,boolean val){
		Editor e=getEditor(c);
		e.putBoolean(key,val);
		e.commit();
	}


}
