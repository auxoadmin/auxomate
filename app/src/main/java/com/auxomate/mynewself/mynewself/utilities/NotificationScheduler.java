package com.auxomate.mynewself.mynewself.utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.HomeActivity;

import java.util.Calendar;
import java.util.Random;



import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationScheduler {


    public static final int DAILY_REMINDER_REQUEST_CODE=100;
    public static final String TAG="NotificationScheduler";



    public static void setReminder(Context context, Class<?> cls, int hour, int min,int m)
    {
        Calendar calendar = Calendar.getInstance();

        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);

        // cancel already scheduled reminders
        cancelReminder(context,cls);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("nID",m);
        int REN = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REN, intent1, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    public static void cancelReminder(Context context,Class<?> cls)
    {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotification(Context context,Class<?> cls,int m)
    {
        PrefManager prefManager = new PrefManager(context);
        Log.d(TAG,"called");



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(m, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);

        Notification notification = builder.setContentTitle("TASK 1")
                .setContentText(prefManager.getString(context,PrefManager.TASK1_DES))
                .setAutoCancel(true)
                .setSound(alarmSound)

                .setSmallIcon(R.drawable.mainlogo_48dp)
                .setContentIntent(pendingIntent).build();



        Notification notificationTask2 = builder.setContentTitle("TASK 2")
                .setContentText(prefManager.getString(context,PrefManager.TASK2_DES))
                .setAutoCancel(true)
                .setSound(alarmSound)

                .setSmallIcon(R.drawable.mainlogo_48dp)
                .setContentIntent(pendingIntent).build();
        Notification notificationTask3 = builder.setContentTitle("TASK 3")
                .setContentText(prefManager.getString(context,PrefManager.TASK3_DES))
                .setAutoCancel(true)
                .setSound(alarmSound)

                .setSmallIcon(R.drawable.mainlogo_48dp)
                .setContentIntent(pendingIntent).build();
        Notification notificationV1 = builder.setContentTitle("Visualization 1")
                .setContentText("Visualizatiuon is Due")
                .setAutoCancel(true)
                .setSound(alarmSound)

                .setSmallIcon(R.drawable.mainlogo_48dp)
                .setContentIntent(pendingIntent).build();
        Notification notificationV2 = builder.setContentTitle("Visualization 2")
                .setContentText("Visualizatiuon is Due")
                .setAutoCancel(true)
                .setSound(alarmSound)

                .setSmallIcon(R.drawable.mainlogo_48dp)
                .setContentIntent(pendingIntent).build();
        Notification notificationV3 = builder.setContentTitle("Visualization 3")
                .setContentText("Visualizatiuon is Due")
                .setAutoCancel(true)
                .setSound(alarmSound)

                .setSmallIcon(R.drawable.mainlogo_48dp)
                .setContentIntent(pendingIntent).build();

        if(m==1){
            notificationManager.notify(++m, notification);

        }
        else if(m==2){
            notificationManager.notify(++m, notificationTask2);

        }
        else if(m==3){
            notificationManager.notify(++m, notificationTask3);

        }
        else if(m==4){
            notificationManager.notify(++m, notificationV1);
//            Carousal carousal = Carousal.with(context).beginTransaction();
//            carousal.setContentTitle("Visualization").setContentText("Your content description here");
//            CarousalItem cItem = new CarousalItem("Item Id here", "Item Title", "Item Content","");
//
//            //Additionally we can set a type to it. It is useful if we are showing more than one type
//            //of data in carousal. so that we know, where to go when an item is clicked.
//           // cItem.setType(TYPE_QUOTE);
//
//            //Now add item to the carousal
//            carousal.addCarousalItem(cItem);
//            carousal.setOtherRegionClickable(true);
//            carousal.buildCarousal();


        }
        else if(m==5){
            notificationManager.notify(++m, notificationV2);

        }
        else if (m==6){
            notificationManager.notify(++m, notificationV3);

        }







    }




}
