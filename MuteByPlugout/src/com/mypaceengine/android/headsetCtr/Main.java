package com.mypaceengine.android.headsetCtr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Main extends Activity {

	private HeadSetService audioCtrService;

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			audioCtrService = ((HeadSetService.HeadSetServiceBinder)service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			audioCtrService = null;
		}

	};
	CheckBox enableBtn=null;
	CheckBox startUpBtn=null;
	CheckBox manerBtn=null;
	Button marketBtn=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		EditText te=(EditText)findViewById(R.id.editText1);
		te.setBackgroundColor(Color.BLACK);
		te.setTextColor(Color.WHITE);

		enableBtn=(CheckBox)findViewById(R.id.enableBtn);
		enableBtn.setChecked(getServiceActive());
		enableBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				CheckBox btn=(CheckBox)view;
				if(btn.isChecked()){
					startService();

				}else{
					endService();
				}
			}

		});

		startUpBtn=(CheckBox)findViewById(R.id.startUpBtn);
		startUpBtn.setChecked(this.getServiceOnBootup());
		startUpBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				CheckBox btn=(CheckBox)view;
				setServiceOnBootup(btn.isChecked());
			}

		});


		manerBtn=(CheckBox)findViewById(R.id.ManerBox1);
		manerBtn.setChecked(this.getServiceOnlyManer());
		manerBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				CheckBox btn=(CheckBox)view;
				setServiceOnlyManer(btn.isChecked());
			}

		});
		marketBtn= (Button)findViewById(R.id.button1);
		marketBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.setData(Uri.parse("https://market.android.com/details?id=com.mypaceengine.android.headsetCtr"));
				startActivity(it);
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onDestroy() {
		try{
			if(serviceConnection!=null){
				unbindService(serviceConnection); 
				audioCtrService=null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		super.onDestroy();

	}

	public void startService(){
		Intent intent = new Intent(this, HeadSetService.class);
		if(!Util.isServiceRunning(getApplicationContext(), HeadSetService.class)){
			startService(intent);
		}
		if(serviceConnection!=null){
			// サービスにバインド
			bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		}
	}
	public void endService(){
		try{
			if(serviceConnection!=null){
				unbindService(serviceConnection); // バインド解除
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try{
			Intent intent = new Intent(this, HeadSetService.class);
			this.stopService(intent);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try{
			if(audioCtrService!=null){
				//		audioCtrService.stopSelf(); // サービスは必要ないので終了させる。
				audioCtrService=null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public boolean getServiceActive(){
		return Util.isServiceRunning(getApplicationContext(), HeadSetService.class);
	}


	public void setServiceOnBootup(boolean flag){
		Editor edit=Util.getEditor(getApplicationContext());
		edit.putBoolean(Util.BOOTUP_KEY, flag);
		edit.commit();
	}
	public boolean getServiceOnBootup(){
		SharedPreferences pref=Util.getPreferences(getApplicationContext());
		return pref.getBoolean(Util.BOOTUP_KEY, false);
	}
	public void setServiceOnlyManer(boolean flag){
		Editor edit=Util.getEditor(getApplicationContext());
		edit.putBoolean(Util.MANER_KEY, flag);
		edit.commit();
		if(audioCtrService!=null){
			audioCtrService.chgListener();
		}
	}
	public boolean getServiceOnlyManer(){
		SharedPreferences pref=Util.getPreferences(getApplicationContext());
		return pref.getBoolean(Util.MANER_KEY, false);
	}

}