package com.mypaceengine.android.headsetCtr;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ヘッドセットの状態を取得するブロードキャストレシーバー
 * @author piroto
 */
public class HeadsetStateReceiver extends BroadcastReceiver {
	private boolean isPlugged = false;
	private String headsetType = null;
	private boolean isMicrophone = false;

	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {


			int state = intent.getIntExtra("state", 0);
			setPlugged((state > 0)); // 0:unplugged,1:headset with microphone,2:a headset with no microphone

			String name = intent.getStringExtra("name");
			setHeadsetType(name);

			int microphone  = intent.getIntExtra("microphone", 0);
			setMicrophone((microphone == 1));
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

	public boolean isPlugged() {
		return isPlugged;
	}

	public void setPlugged(boolean isPlugged) {
		this.isPlugged = isPlugged;
	}

	public String getHeadsetType() {
		return headsetType;
	}

	public void setHeadsetType(String headsetType) {
		this.headsetType = headsetType;
	}

	public boolean isMicrophone() {
		return isMicrophone;
	}

	public void setMicrophone(boolean isMicrophone) {
		this.isMicrophone = isMicrophone;
	}
}
