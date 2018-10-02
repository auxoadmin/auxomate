package com.auxomate.mynewself.mynewself.activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.utilities.AlarmReceiver;
import com.auxomate.mynewself.mynewself.utilities.NotificationScheduler;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskSubmit extends AppCompatActivity implements View.OnClickListener{
    private String result = null;
    private String resultArray[] = null;

    private EditText editTextPTOne, editTextSTTwo, editTextSTThree, editTextSchTOneTime, editTextSchTOneDuration,
    editTextSchTTwoTime, editTextSchTTwoDuration, editTextSchTThreeTime, editTextSchTThreeDuration, editTextSchVOneTime,
    editTextSchVTwoTime, editTextSchVThreeTime;

    private Button buttonSubmit;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    String TAG = "RemindMe";
    PrefManager localData;
    AspireGallery aspireGallery;

    int hour, min;

    ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);

        result = getIntent().getStringExtra("visionResult");
        localData = new PrefManager(getApplicationContext());
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        aspireGallery = new AspireGallery();
        init();
    }

    private void init() {

        editTextPTOne = findViewById(R.id.tasksubmit_edittext_pttaskone);
        editTextSTTwo = findViewById(R.id.tasksubmit_edittext_sttasktwo);
        editTextSTThree = findViewById(R.id.tasksubmit_edittext_sttaskthree);
        editTextSchTOneTime = findViewById(R.id.tasksubmit_edittext_schttaskonetime);
        editTextSchTOneDuration = findViewById(R.id.tasksubmit_edittext_schttaskoneduration);
        editTextSchTTwoTime = findViewById(R.id.tasksubmit_edittext_schttasktwotime);
        editTextSchTTwoDuration = findViewById(R.id.tasksubmit_edittext_schttasktwoduration);
        editTextSchTThreeTime = findViewById(R.id.tasksubmit_edittext_schttaskthreetime);
        editTextSchTThreeDuration = findViewById(R.id.tasksubmit_edittext_schttaskthreeduration);
        editTextSchVOneTime = findViewById(R.id.tasksubmit_edittext_schvonetime);
        editTextSchVTwoTime = findViewById(R.id.tasksubmit_edittext_schvtwotime);
        editTextSchVThreeTime = findViewById(R.id.tasksubmit_edittext_schvthreetime);

        buttonSubmit = findViewById(R.id.tasksubmit_button_submit);
        buttonSubmit.setOnClickListener(this);
        editTextSchTOneTime.setOnClickListener(this);
        editTextSchTTwoTime.setOnClickListener(this);
        editTextSchTThreeTime.setOnClickListener(this);
        editTextSchVOneTime.setOnClickListener(this);
        editTextSchVTwoTime.setOnClickListener(this);
        editTextSchVThreeTime.setOnClickListener(this);

        getIncomingIntent();

    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("visionResult")){

            String[] split = result.split("(\\s|^)Primary Task(\\s|$)");



            String  stringTask1 = split[1].toString();
            String[] splitTask = stringTask1.split("(\\s|^)Secondary Tasks[\\r\\n]Task 2(\\s|$)|(\\s|^)SecondaryTasks[\\r\\n]Task2(\\s|$)");
            String[] splitTask1 = splitTask[0].split("(\\s|^)Task 1(\\s|$)|(\\s|^)Task1(\\s|$)|(\\s|^)Taski(\\s|$)|(\\s|^)Task i(\\s|$)|(\\s|^)Taskı(\\s|$)|(\\s|^)Task(\\s|$)");
            editTextPTOne.setText(splitTask1[1]);
            String[] splitTask2 = splitTask[1].split("(\\s|^)Task 3(\\s|$)|(\\s|^)Task3(\\s|$)");
            editTextSTTwo.setText(splitTask2[0]);
            String[] splitTask3 = splitTask2[1].split("(\\s|^)Schedule(\\s|$)");
            editTextSTThree.setText(splitTask3[0]);

            String[] splitSchedule = result.split("(\\s|^)Schedule Tasks(\\s|$)");
            String[] splitVisual = splitSchedule[1].split("(\\s|^)Schedule Visualizations(\\s|$)");

            String[] splitvTask3 = splitVisual[0].split("(\\s|^)Task 3(\\s|$)|(\\s|^)Task3(\\s|$)");
            String[] splitvTask2 = splitvTask3[0].split("(\\s|^)Task2(\\s|$)|(\\s|^)Task 2(\\s|$)");


            String [] vtask = splitvTask2[0].split("(\\s|^)Task 1(\\s|$)|(\\s|^)Task1(\\s|$)|(\\s|^)Taski(\\s|$)|(\\s|^)Task i(\\s|$)|(\\s|^)Taskı(\\s|$)");
            String  vtask1 = vtask[1].split("/")[0];
            String  vtask2 = splitvTask2[1].split("/")[0];
            String  vtask3 = splitvTask3[1].split("/")[0];

            Log.d("splitvTask1",vtask[0]);
            Log.d("splitdTask1",vtask[1]);
            Log.d("splitvTask2",splitvTask2[0]);
            Log.d("splitdTask2",vtask3 );
            Log.d("splitvTask3",vtask1);
            Log.d("splitdTask3",vtask2);




            // task1.setText(result);



        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tasksubmit_button_submit:

                submitTheTask();

                break;

            case R.id.tasksubmit_edittext_schttaskonetime:
                Log.d("edittext one","clicked");
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        Log.d(TAG, "onTimeSet: hour " + hourOfDay);
                        Log.d(TAG, "onTimeSet: min " + minutes);
                        localData.set_tonehour(hourOfDay);
                        localData.set_tonemin(minutes);
                        //scheduleNotification(getNotification("Task 1"),hourOfDay,minutes,1);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_tonehour(), localData.get_tonemin(),1);

                        Log.d("hour","value:"+hourOfDay);
                        editTextSchTOneTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schttasktwotime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        localData.set_ttwohour(hourOfDay);
                        localData.set_ttwomin(minutes);
                        //scheduleNotification(getNotification("Task 2"),hourOfDay,minutes,2);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_ttwohour(), localData.get_ttwomin(),2);

                        editTextSchTTwoTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schttaskthreetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        localData.set_tthreehour(hourOfDay);
                        localData.set_tthreemin(minutes);
                        //scheduleNotification(getNotification("Task 3"),hourOfDay,minutes,3);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_tthreehour(), localData.get_tthreemin(),3);

                        editTextSchTThreeTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schvonetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        localData.set_vonehour(hourOfDay);
                        localData.set_vonemin(minutes);
                        //scheduleNotification(getNotification("Visulization 1"),hourOfDay,minutes,4);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_vonehour(), localData.get_vonemin(),4);

                        editTextSchVOneTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schvtwotime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        localData.set_vtwohour(hourOfDay);
                        localData.set_vtwoemin(minutes);
                       // scheduleNotification(getNotification("Visulization 2"),hourOfDay,minutes,5);

                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_vtwohour(), localData.get_vtwomin(),5);

                        editTextSchVTwoTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schvthreetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        localData.set_vthreehour(hourOfDay);
                        localData.set_vthreemin(minutes);
                        //scheduleNotification(getNotification("Visulization 3"),hourOfDay,minutes,6);

                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_vthreehour(), localData.get_vthreemin(),6);

                        editTextSchVThreeTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;




        }

    }

    private void submitTheTask() {

        String taskOneDesc = editTextPTOne.getText().toString().trim();
        String taskTwoDesc = editTextSTTwo.getText().toString().trim();
        String taskThreeDesc = editTextSTThree.getText().toString().trim();

        String scheduleTaskOneTime = editTextSchTOneTime.getText().toString().trim();
        String scheduleTaskOneDuration = editTextSchTOneDuration.getText().toString().trim();
        String scheduleTaskTwoTime = editTextSchTTwoTime.getText().toString().trim();
        String scheduleTaskTwoDuration = editTextSchTTwoDuration.getText().toString().trim();
        String scheduleTaskThreeTime = editTextSchTThreeTime.getText().toString().trim();
        String scheduleTaskThreeDuration = editTextSchTThreeDuration.getText().toString().trim();

        String scheduleVOneTime = editTextSchVOneTime.getText().toString().trim();
        String scheduleVTwoTime = editTextSchVTwoTime.getText().toString().trim();
        String scheduleVThreeTime = editTextSchVThreeTime.getText().toString().trim();

        PrefManager.putString(this,PrefManager.TASK1_DES,taskOneDesc);
        PrefManager.putString(this,PrefManager.TASK2_DES,taskTwoDesc);
        PrefManager.putString(this,PrefManager.TASK3_DES,taskThreeDesc);

        //use it as you need...
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);





    }


//    private void showTimePickerDialog(int h, int m) {
//
//        LayoutInflater inflater = getLayoutInflater();
//
//
//        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
//                        Log.d(TAG, "onTimeSet: hour " + hour);
//                        Log.d(TAG, "onTimeSet: min " + min);
//                        localData.set_hour(hour);
//                        localData.set_min(min);
//                        tvTime.setText(getFormatedTime(hour, min));
//                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
//
//
//                    }
//                }, h, m, false);
//
//        builder.setCustomTitle(view);
//        builder.show();
//
//    }
//
//    public String getFormatedTime(int h, int m) {
//        final String OLD_FORMAT = "HH:mm";
//        final String NEW_FORMAT = "hh:mm a";
//
//        String oldDateString = h + ":" + m;
//        String newDateString = "";
//
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
//            Date d = sdf.parse(oldDateString);
//            sdf.applyPattern(NEW_FORMAT);
//            newDateString = sdf.format(d);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return newDateString;
//    }


//    private void scheduleNotification(Notification notification, int hour,int min,int reqCode) {
//
//        Calendar calendar = Calendar.getInstance();
//
//        Calendar setCalendar = Calendar.getInstance();
//        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
//        setCalendar.set(Calendar.MINUTE, min);
//        setCalendar.set(Calendar.SECOND, 0);
//
//        // cancel already scheduled reminders
//        //cancelReminder(context,cls);
//
//        if(setCalendar.before(calendar))
//            setCalendar.add(Calendar.DATE,1);
//
//        // Enable a receiver
//
//        ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
//        PackageManager pm = this.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//
//        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
//        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
//        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.set(AlarmManager.ELAPSED_REALTIME, setCalendar.getTimeInMillis(), pendingIntent);
//
//
//    }
//
//    private Notification getNotification(String content) {
//
////        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
////
////        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
////
////            // Configure the notification channel.
////            notificationChannel.setDescription("Channel description");
////            notificationChannel.enableLights(true);
////            notificationChannel.setLightColor(Color.RED);
////            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
////            notificationChannel.enableVibration(true);
////            notificationManager.createNotificationChannel(notificationChannel);
////
////
////
////        }
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("Scheduled Notification");
//        builder.setContentText(content);
//        builder.setSmallIcon(R.drawable.logo_small);
//        return builder.build();
//
//    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }
}
