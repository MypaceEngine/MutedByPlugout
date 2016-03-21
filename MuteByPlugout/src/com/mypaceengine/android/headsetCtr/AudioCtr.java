package com.mypaceengine.android.headsetCtr;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

public class AudioCtr {

	private Object syncObj=new Object();
	private HeadsetStateReceiver headsetStateReceiver = null;
	private BlueToothHeadSetReceiver blueToothHeadSetReceiver=null;
	private AudioNoisyReceiver audioNoisyReceiver=null;
	private ManerReciver manerReceiver=null;
	AudioManager audio = null;
	Service service=null;
	public void init(Service _service){
		audio = (AudioManager) _service.getSystemService(Context.AUDIO_SERVICE);
		service=_service;
		headsetStateReceiver = new HeadsetStateReceiver();

		blueToothHeadSetReceiver=new BlueToothHeadSetReceiver();

		audioNoisyReceiver=new AudioNoisyReceiver();

		manerReceiver=new ManerReciver();

		blueToothHeadSetReceiver.init(_service, this);
		headsetStateReceiver.init(_service, this);
		audioNoisyReceiver.init(_service, this);
		manerReceiver.init(_service, this);
		synchronized(syncObj){
			if(blueToothHeadSetReceiver.isConnected()){
				if(audio!=null){
					audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
				}
				if(service!=null){
					AppNotification.putNotice(
							service.getApplicationContext(),
							R.drawable.sound_high,
							service.getApplicationContext().getString(R.string.service_Start),
							service.getApplicationContext().getString(R.string.jackinMain),
							service.getApplicationContext().getString(R.string.jackinSub)

							);
				}
				muted=false;
			}else{
				if(audio!=null){
					audio.setStreamMute(AudioManager.STREAM_MUSIC, true);
				}
				if(service!=null){
					AppNotification.putNotice(
							service.getApplicationContext(),
							R.drawable.sound_mute,
							service.getApplicationContext().getString(R.string.service_Start),
							service.getApplicationContext().getString(R.string.jackooutMain),
							service.getApplicationContext().getString(R.string.jackoutSub)

							);
				}
				muted=true;
			}
		}

	}
	public void start(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		if((service!=null)&&(headsetStateReceiver!=null)){
			service.registerReceiver(headsetStateReceiver, filter);
		}

		IntentFilter filter2 = new IntentFilter();

		filter2.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter2.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		filter2.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		filter2.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter2.addAction("android.bluetooth.headset.action.STATE_CHANGED");
		filter2.addAction("android.bluetooth.a2dp.action.SINK_STATE_CHANGED");
		filter2.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
		filter2.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
		if((service!=null)&&(blueToothHeadSetReceiver!=null)){
			service.registerReceiver(blueToothHeadSetReceiver, filter2);
		}

		IntentFilter filter3 = new IntentFilter();
		filter3.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
		if((service!=null)&&(audioNoisyReceiver!=null)){
			service.registerReceiver(audioNoisyReceiver, filter3);
		}

		IntentFilter filter4 = new IntentFilter();
		filter4.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
		if((service!=null)&&(manerReceiver!=null)){
			service.registerReceiver(manerReceiver, filter4);
		}
	}
	boolean muted=false;
	public void finish(){
		if(service!=null){
			if(headsetStateReceiver!=null){
				headsetStateReceiver.finish();
				service.unregisterReceiver(headsetStateReceiver);
			}
			if(blueToothHeadSetReceiver!=null){
				blueToothHeadSetReceiver.finish();
				service.unregisterReceiver(blueToothHeadSetReceiver);
			}
			if(audioNoisyReceiver!=null){
				audioNoisyReceiver.finish();
				service.unregisterReceiver(audioNoisyReceiver);
			}
			if(manerReceiver!=null){
				manerReceiver.finish();
				service.unregisterReceiver(manerReceiver);
			}
		}
		clearALL();
	}

	public boolean getManer(){
		AudioManager audioManager = audio;
		if(audioManager==null){
			return false;
		}
		int mode = audioManager.getRingerMode();
		int cc=AudioManager.RINGER_MODE_VIBRATE;

		if((mode==AudioManager.RINGER_MODE_SILENT)||(mode==AudioManager.RINGER_MODE_VIBRATE)){
			// マナーモードなので何もしない
			return true;
		}else{
			// ノーマルモードなので音を鳴らす
			return false;
		}
	}

	public void chgListener(){
		synchronized(syncObj){
			try{


				if((blueToothHeadSetReceiver!=null)&&(headsetStateReceiver!=null)){
					boolean flag=(!
							(blueToothHeadSetReceiver.isConnected()||headsetStateReceiver.isPlugged())
							);
					if(service!=null){
						SharedPreferences pref=Util.getPreferences(service.getApplicationContext());
						boolean maner= pref.getBoolean(Util.MANER_KEY, false);
						if((maner)&&(!getManer())){
							clearMute_MANER();
							flag=false;
							muted=false;
							return;
						}
					}

					if(muted!=flag){
						if(flag){
							setMute();
						}else{
							clearMute();
						}
						muted=flag;
					}
				}
			}catch(Throwable ex){
				Log.i("Exception", "message", ex);
			}
		}
	}

	public void setMute(){
		synchronized(syncObj){
			if(audio!=null){
				audio.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
			if(service!=null){
				AppNotification.putNotice(

						service.getApplicationContext(),
						R.drawable.sound_mute,
						service.getApplicationContext().getString(R.string.jackooutMain)+service.getApplicationContext().getString(R.string.jackoutSub2),
						service.getApplicationContext().getString(R.string.jackooutMain),
						service.getApplicationContext().getString(R.string.jackoutSub)

						);
			}
		}
	}

	public void clearMute(){
		if(audio!=null){
			audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
		}

		if(service!=null){

			AppNotification.putNotice(
					service.getApplicationContext(),
					R.drawable.sound_high,
					service.getApplicationContext().getString(R.string.jackinMain)+service.getApplicationContext().getString(R.string.jackinSub2),
					service.getApplicationContext().getString(R.string.jackinMain),
					service.getApplicationContext().getString(R.string.jackinSub)
					);
		}
	}
	public void clearMute_MANER(){
		if(audio!=null){
			audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
		}

		if(service!=null){

			AppNotification.putNotice(
					service.getApplicationContext(),
					R.drawable.sound_high,
					service.getApplicationContext().getString(R.string.ManerMgs)+service.getApplicationContext().getString(R.string.jackinSub2),
					service.getApplicationContext().getString(R.string.ManerMgs),
					service.getApplicationContext().getString(R.string.jackinSub)
					);
		}
	}
	public void clearALL(){
		synchronized(syncObj){
			if(audio!=null){
				audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
			}
			if(service!=null){
				AppNotification.putNotice(
						service.getApplicationContext(),
						R.drawable.sound_high,
						service.getApplicationContext().getString(R.string.jackinSub),
						"",
						""
						);
				AppNotification.removeNotice(service.getApplicationContext());
			}
		}
	}
}
