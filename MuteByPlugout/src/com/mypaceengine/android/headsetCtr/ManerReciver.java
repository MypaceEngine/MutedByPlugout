package com.mypaceengine.android.headsetCtr;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class ManerReciver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
			if(auditCtr!=null){
				auditCtr.chgListener();
			}
		}
	}

	private Service service=null;
	private AudioCtr auditCtr=null;
	public void init(Service _service,AudioCtr _auditCtr){
		service=_service;
		auditCtr=_auditCtr;
	}
	public void finish(){

	}

}
