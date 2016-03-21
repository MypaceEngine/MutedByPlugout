package com.mypaceengine.android.headsetCtr;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;


public class BlueToothHeadSetReceiver extends BroadcastReceiver{
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.context=context;
		if("android.bluetooth.a2dp.action.SINK_STATE_CHANGED".equals(intent.getAction())){
			BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			String address=device.getAddress();
			Bundle extras = intent.getExtras();
			int state = extras.getInt("android.bluetooth.a2dp.extra.SINK_STATE");
			if(state==0){
				del(address);
			}else if((state==4)){
				add(address);
			}
			if(auditCtr!=null){
				auditCtr.chgListener();
			}
		}else
			if("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED".equals(intent.getAction())){
				BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String address=device.getAddress();
				Bundle extras = intent.getExtras();
				int state = extras.getInt("android.bluetooth.profile.extra.STATE");
				if(state==0){
					del(address);
				}else if((state==2)){
					add(address);
				}
				if(auditCtr!=null){
					auditCtr.chgListener();
				}

			}else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
				if(disabelFlag){
					Bundle extras = intent.getExtras();
					int state = extras.getInt(BluetoothAdapter.EXTRA_STATE);
					if(state==BluetoothAdapter.STATE_OFF){
						BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
						if(mBluetoothAdapter!=null){
							mBluetoothAdapter.enable();
							disabelFlag=false;
						}
					}

				}
			}
	}
	boolean disabelFlag=false;
	private Service service=null;
	private AudioCtr auditCtr=null;
	public void init(Service _service,AudioCtr _auditCtr){
		service=_service;
		auditCtr=_auditCtr;
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter!=null){
			if(mBluetoothAdapter.isEnabled()){
				mBluetoothAdapter.disable();
				disabelFlag=true;

			}
		}
	}

	public void finish(){

	}

	public boolean isConnected(){
		boolean result=(deviceMap.size()>0);
		return result;

	}
	private HashMap<String,Integer> deviceMap=new HashMap<String,Integer>();
	public void add(String address){
		Integer inte=null;
		if(deviceMap.containsKey(address)){
			inte=deviceMap.get(address);
		}
		if(inte==null){
			inte=new Integer(0);
		}
		inte=new Integer((inte.intValue())+1);
		deviceMap.put(address, inte);
	}
	public void del(String address){
		Integer inte=null;
		if(deviceMap.containsKey(address)){
			inte=deviceMap.get(address);
		}
		if(inte!=null){
			int val=inte.intValue();
			val=val-1;
			if(val<=0){
				deviceMap.remove(address);
			}else{
				inte=new Integer(val);
				deviceMap.put(address, inte);
			}
		}
	}

}
