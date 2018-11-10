package com.auxomate.mynewself.mynewself.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.BuildConfig;
import com.auxomate.mynewself.mynewself.activities.AddPostAspire;
import com.auxomate.mynewself.mynewself.activities.AddTask;
import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.HomeActivity;
import com.auxomate.mynewself.mynewself.activities.MainActivity;
import com.auxomate.mynewself.mynewself.activities.TaskSubmit;
import com.auxomate.mynewself.mynewself.utilities.AlarmReceiver;
import com.auxomate.mynewself.mynewself.utilities.NotificationScheduler;
import com.auxomate.mynewself.mynewself.utilities.PackageManagerUtils;
import com.auxomate.mynewself.mynewself.utilities.PermissionUtils;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Page;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class ActFragment extends Fragment implements View.OnClickListener{
    //EditText task1;

    EditText taskoneDes,tasktwoDes,taskthreeDes,taskoneTime,tasktwoTime,taskthreeTime,visualoneTime,visualtwoTime,
            visualthreeTime;
    ImageButton editTask;
    Button submitBtn;
    PrefManager prefManager;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;





    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    View RootView;
    //public HomeActivity activity;
    //Context applicationContext = HomeActivity.getContextOfApplication();

    public ActFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        RootView = inflater.inflate(R.layout.fragment_act, container, false);
        //task1 = RootView.findViewById(R.id.task1);


        taskoneDes = RootView.findViewById(R.id.actFrament_edittext_pttaskone);
        taskoneDes.setEnabled(false);

        tasktwoDes = RootView.findViewById(R.id.actFrament_edittext_sttasktwo);
        tasktwoDes.setEnabled(false);

        taskthreeDes = RootView.findViewById(R.id.actFrament_edittext_sttaskthree);
        taskthreeDes.setEnabled(false);

        taskoneTime = RootView.findViewById(R.id.actFrament_edittext_schttaskonetime);
        taskoneTime.setEnabled(false);
        taskoneTime.setOnClickListener(this);

        tasktwoTime = RootView.findViewById(R.id.actFrament_edittext_schttasktwotime);
        tasktwoTime.setEnabled(false);
        tasktwoTime.setOnClickListener(this);

        taskthreeTime = RootView.findViewById(R.id.actFrament_edittext_schttaskthreetime);
        taskthreeTime.setEnabled(false);
        taskthreeTime.setOnClickListener(this);

        visualoneTime = RootView.findViewById(R.id.actFrament_edittext_schvonetime);
        visualoneTime.setEnabled(false);
        visualoneTime.setOnClickListener(this);

        visualtwoTime = RootView.findViewById(R.id.actFrament_edittext_schvtwotime);
        visualtwoTime.setEnabled(false);
        visualtwoTime.setOnClickListener(this);

        visualthreeTime = RootView.findViewById(R.id.actFrament_edittext_schvthreetime);
        visualthreeTime.setEnabled(false);
        visualthreeTime.setOnClickListener(this);

        editTask = RootView.findViewById(R.id.act_imgbtn_edit);
        submitBtn = RootView.findViewById(R.id.actFragment_button_submit);
        submitBtn.setVisibility(View.GONE);
        submitBtn.setOnClickListener(this);


        taskoneDes.setText(PrefManager.getString(getActivity(),PrefManager.TASK1_DES));
        tasktwoDes.setText(PrefManager.getString(getActivity(),PrefManager.TASK2_DES));
        taskthreeDes.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));



        taskoneTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK1Time));
        tasktwoTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK2Time));
        taskthreeTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3Time));
        visualoneTime.setText(PrefManager.getString(getActivity(),PrefManager.V1Time));
        visualtwoTime.setText(PrefManager.getString(getActivity(),PrefManager.V2Time));
        visualthreeTime.setText(PrefManager.getString(getActivity(),PrefManager.V3Time));




//        taskoneTime.setText(PrefManager.getString(getActivity(),PrefManager.));
//        tasktwoTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        taskthreeTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        visualoneTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        visualtwoTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        visualthreeTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));



        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getActivity(),TaskSubmit.class));
                taskoneDes.setEnabled(true);

                tasktwoDes.setEnabled(true);

                taskthreeDes.setEnabled(true);

                taskoneTime.setEnabled(true);
                tasktwoTime.setEnabled(true);
                taskthreeTime.setEnabled(true);
                visualoneTime.setEnabled(true);
                visualtwoTime.setEnabled(true);
                visualthreeTime.setEnabled(true);
                submitBtn.setVisibility(View.VISIBLE);

            }
        });
        return RootView;
    }







    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu_act,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_act_action){
            startActivity(new Intent(getActivity(),AddTask.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public static ActFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        ActFragment fragment = new ActFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        prefManager = new PrefManager(context);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tasksubmit_button_submit:



               // submitTheTask();

                break;

            case R.id.actFrament_edittext_schttaskonetime:
                Log.d("edittext one","clicked");
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        prefManager.set_tonehour(hourOfDay);
                        prefManager.set_tonemin(minutes);


                        //scheduleNotification(getNotification("Task 1"),hourOfDay,minutes,1);
                        NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, prefManager.get_tonehour(), prefManager.get_tonemin(),1);

                        Log.d("hour","value:"+hourOfDay);
                        taskoneTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.actFrament_edittext_schttasktwotime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        prefManager.set_ttwohour(hourOfDay);
                        prefManager.set_ttwomin(minutes);


                        //scheduleNotification(getNotification("Task 2"),hourOfDay,minutes,2);
                        NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, prefManager.get_ttwohour(), prefManager.get_ttwomin(),2);

                        tasktwoTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.actFrament_edittext_schttaskthreetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        prefManager.set_tthreehour(hourOfDay);
                        prefManager.set_tthreemin(minutes);

                        //scheduleNotification(getNotification("Task 3"),hourOfDay,minutes,3);
                        NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, prefManager.get_tthreehour(), prefManager.get_tthreemin(),3);
                        Log.d("getHourFromPref",prefManager.get_tonehour()+":"+prefManager.get_vonemin());


                        taskthreeTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.actFrament_edittext_schvonetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        prefManager.set_vonehour(hourOfDay);
                        prefManager.set_vonemin(minutes);

                        //scheduleNotification(getNotification("Visulization 1"),hourOfDay,minutes,4);
                        NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, prefManager.get_vonehour(), prefManager.get_vonemin(),4);

                        visualoneTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.actFrament_edittext_schvtwotime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        prefManager.set_vtwohour(hourOfDay);
                        prefManager.set_vtwoemin(minutes);
                        // scheduleNotification(getNotification("Visulization 2"),hourOfDay,minutes,5);

                        NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, prefManager.get_vtwohour(), prefManager.get_vtwomin(),5);

                        visualtwoTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.actFrament_edittext_schvthreetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        prefManager.set_vthreehour(hourOfDay);
                        prefManager.set_vthreemin(minutes);

                        //scheduleNotification(getNotification("Visulization 3"),hourOfDay,minutes,6);

                        NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, prefManager.get_vthreehour(), prefManager.get_vthreemin(),6);

                        visualthreeTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            case R.id.actFragment_button_submit:
                taskoneDes.setEnabled(false);
                PrefManager.putString(getActivity(),PrefManager.TASK1_DES,taskoneDes.getText().toString());
                tasktwoDes.setEnabled(false);
                PrefManager.putString(getActivity(),PrefManager.TASK2_DES,tasktwoDes.getText().toString());
                taskthreeDes.setEnabled(false);
                PrefManager.putString(getActivity(),PrefManager.TASK3_DES,taskthreeDes.getText().toString());
                taskoneTime.setEnabled(false);
                tasktwoTime.setEnabled(false);
                taskthreeTime.setEnabled(false);
                visualoneTime.setEnabled(false);
                visualtwoTime.setEnabled(false);
                visualthreeTime.setEnabled(false);
                PrefManager.putString(getActivity(),PrefManager.TASK1Time,taskoneTime.getText().toString());
                PrefManager.putString(getActivity(),PrefManager.TASK2Time,tasktwoTime.getText().toString());
                PrefManager.putString(getActivity(),PrefManager.TASK3Time,taskthreeTime.getText().toString());
                PrefManager.putString(getActivity(),PrefManager.V1Time,visualoneTime.getText().toString());
                PrefManager.putString(getActivity(),PrefManager.V2Time,visualtwoTime.getText().toString());
                PrefManager.putString(getActivity(),PrefManager.V3Time,visualthreeTime.getText().toString());
                submitBtn.setVisibility(View.GONE);

                break;




        }

    }
}
