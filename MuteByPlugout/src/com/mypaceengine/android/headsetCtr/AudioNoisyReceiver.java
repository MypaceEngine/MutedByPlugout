package com.mypaceengine.android.headsetCtr;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.text.format.Time;


public class AudioNoisyReceiver extends BroadcastReceiver{
	private Context context;
	//   UtilTimerTest tt=null;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
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
