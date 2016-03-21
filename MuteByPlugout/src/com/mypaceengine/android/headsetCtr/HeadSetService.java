package com.mypaceengine.android.headsetCtr;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class HeadSetService extends Service {

	class HeadSetServiceBinder extends Binder {

		HeadSetService getService() {
			return HeadSetService.this;
		}

	}

	public static final String ACTION = "MuteByJackOut Service";
	private AudioCtr auditCtr=null;
	@Override
	public void onCreate() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				Log.e("tag", e.toString());
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement element : elements) {
					Log.e("tag", element.toString());
				}
			}
		});
		super.onCreate();
		auditCtr=new AudioCtr();
		auditCtr.init(this);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(auditCtr!=null){
			auditCtr.start();
		}
	}

	@Override
	public void onDestroy() {
		if(auditCtr!=null){
			auditCtr.finish();
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return new HeadSetServiceBinder();
	}

	@Override
	public void onRebind(Intent intent) {
	}

	@Override
	public boolean onUnbind(Intent intent) {

		return false; 
	}
	public void chgListener(){
		if(auditCtr!=null){
			auditCtr.chgListener();
		}
	}

}