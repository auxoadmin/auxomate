package com.auxomate.mynewself.mynewself.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.BuildConfig;
import com.auxomate.mynewself.mynewself.activities.AddPostAspire;
import com.auxomate.mynewself.mynewself.activities.AddTask;
import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.HomeActivity;
import com.auxomate.mynewself.mynewself.activities.MainActivity;
import com.auxomate.mynewself.mynewself.activities.TaskSubmit;
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
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class ActFragment extends Fragment {
    //EditText task1;

    TextView taskoneDes,tasktwoDes,taskthreeDes,taskoneTime,tasktwoTime,taskthreeTime,visualoneTime,visualtwoTime,
            visualthreeTime;


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
        tasktwoDes = RootView.findViewById(R.id.actFrament_edittext_sttasktwo);
        taskthreeDes = RootView.findViewById(R.id.actFrament_edittext_sttaskthree);
        taskoneTime = RootView.findViewById(R.id.actFrament_edittext_schttaskonetime);
        tasktwoTime = RootView.findViewById(R.id.actFrament_edittext_schttasktwotime);
        taskthreeTime = RootView.findViewById(R.id.actFrament_edittext_schttaskthreetime);
        visualoneTime = RootView.findViewById(R.id.actFrament_edittext_schvonetime);
        visualtwoTime = RootView.findViewById(R.id.actFrament_edittext_schvtwotime);
        visualthreeTime = RootView.findViewById(R.id.actFrament_edittext_schvthreetime);


        taskoneDes.setText(PrefManager.getString(getActivity(),PrefManager.TASK1_DES));
        tasktwoDes.setText(PrefManager.getString(getActivity(),PrefManager.TASK2_DES));
        taskthreeDes.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        taskoneTime.setText(PrefManager.getString(getActivity(),PrefManager.));
//        tasktwoTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        taskthreeTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        visualoneTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        visualtwoTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));
//        visualthreeTime.setText(PrefManager.getString(getActivity(),PrefManager.TASK3_DES));















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
}
