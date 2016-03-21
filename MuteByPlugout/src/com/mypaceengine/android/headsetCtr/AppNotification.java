package com.mypaceengine.android.headsetCtr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AppNotification {

	public static final int NOTIFICATION_ID = 1119;

	private static Notification createNotification(Context context,int icon,String tooltip,String mainTitle,String subtitle) {
		Notification notification = new Notification(
				icon,
				tooltip,                                        
				System.currentTimeMillis()                
				);

		PendingIntent pi = PendingIntent.getActivity(
				context,
				0,                                             // requestCode
				new Intent(context, Main.class),
				0                                              // Default flags
				);
		notification.setLatestEventInfo(
				context,
				mainTitle,
				subtitle,
				pi
				);
		notification.flags = notification.flags
				| Notification.FLAG_NO_CLEAR         
				| Notification.FLAG_ONGOING_EVENT;   
		notification.number = 0;
		return notification;
	}

	public static void putNotice(Context context,int icon,String tooltip,String mainTitle,String subtitle) {
		NotificationManager nm = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = createNotification(context,icon,tooltip,mainTitle,subtitle);
		if(nm!=null){
			nm.notify(AppNotification.NOTIFICATION_ID, notification);
		}
	}

	public static void removeNotice(Context context) {
		NotificationManager nm = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		if(nm!=null){
			nm.cancel(NOTIFICATION_ID);
		}
	}
}