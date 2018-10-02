package com.auxomate.mynewself.mynewself.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.auxomate.mynewself.mynewself.activities.AspireGallery;
import com.auxomate.mynewself.mynewself.activities.HomeActivity;
import com.auxomate.mynewself.mynewself.activities.MainActivity;
import com.auxomate.mynewself.mynewself.activities.TaskSubmit;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                PrefManager localData = new PrefManager(context);
               // NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_tonehour(), localData.get_tonemin());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        int m = intent.getIntExtra("nID",9);

        Log.d("getNotificationIntent","ID"+m);
        if(m<=3)
        {
            NotificationScheduler.showNotification(context, HomeActivity.class, m);
        }
        else
        {
            NotificationScheduler.showNotification(context, AspireGallery.class, m);
        }
        //Trigger the notification


//        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification notification = intent.getParcelableExtra(NOTIFICATION);
//        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//        notificationManager.notify(id, notification);

    }
}
