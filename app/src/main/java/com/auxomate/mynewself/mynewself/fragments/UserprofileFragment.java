package com.auxomate.mynewself.mynewself.fragments;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class UserprofileFragment extends Fragment {

    private View view;
    private Switch aSwitchTask, aSwitchVis, aSwitchQuoteScreen,aSwitchGratitude;
    private LinearLayout linearLayoutTask, linearLayoutVis,linearLayoutGrati;
    private EditText editTextTaskTime, editTextVisTime;
    private Button btnGratitude;
    StorageReference mStorage;
    DatabaseReference mDatabase;
    ProgressDialog mProgress;


    String TAG = "RemindMe";
    PrefManager prefManager;

    SwitchCompat reminderSwitch;
    TextView tvTime;

    LinearLayout ll_set_time;

    int hour, min;

    ClipboardManager myClipboard;

    public UserprofileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_userprofile, container, false);

        String name = prefManager.getString(getActivity(),PrefManager.PRF_USERNAME_WELCOME);
        Toast.makeText(getActivity(),name, Toast.LENGTH_SHORT).show();

        init();

        return view;
    }

    private void init() {

        aSwitchTask = view.findViewById(R.id.userprofile_switch_task);
        aSwitchVis = view.findViewById(R.id.userprofile_switch_visualizations);
        aSwitchQuoteScreen = view.findViewById(R.id.userprofile_switch_quotescreen);
        aSwitchGratitude = view.findViewById(R.id.userprofile_switch_dailygratitude);

        linearLayoutTask = view.findViewById(R.id.userprofile_linear_task);
        linearLayoutTask.setVisibility(View.GONE);
        linearLayoutVis = view.findViewById(R.id.userprofile_linear_visualizations);
        linearLayoutVis.setVisibility(View.GONE);
        linearLayoutGrati = view.findViewById(R.id.userprofile_linear_gratitude);
        btnGratitude = view.findViewById(R.id.userprofile_button_savegratitude);


        editTextTaskTime = view.findViewById(R.id.userprofile_edittext_tasktime);
        editTextVisTime = view.findViewById(R.id.userprofile_edittext_vistime);
        mStorage = FirebaseStorage.getInstance().getReference();
        String key = prefManager.getString(getActivity(),PrefManager.PRF_USERKEY);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Auxomate").child(key);
        mProgress= new ProgressDialog(getActivity());



        aSwitchTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    PrefManager.putBoolean(getActivity(),PrefManager.TASK_NOTIFICATION,true);

                }else {
                    PrefManager.putBoolean(getActivity(),PrefManager.TASK_NOTIFICATION,false);
                }

            }
        });

        aSwitchVis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    PrefManager.putBoolean(getActivity(),PrefManager.VISUAL_NOTIFICATION,true);
                }else {
                    PrefManager.putBoolean(getActivity(),PrefManager.VISUAL_NOTIFICATION,false);
                }

            }
        });

        aSwitchQuoteScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PrefManager.putBoolean(getActivity(),PrefManager.SSAVER_NOTIFICATION,true);
                }
                else {
                    PrefManager.putBoolean(getActivity(),PrefManager.SSAVER_NOTIFICATION,false);
                }
            }
        });

        aSwitchGratitude.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {

                if (b){
                    PrefManager.putBoolean(getActivity(),PrefManager.GRATI_NOTIFICATION,true);
                    linearLayoutGrati.setVisibility(View.VISIBLE);
                }else {
                    PrefManager.putBoolean(getActivity(),PrefManager.GRATI_NOTIFICATION,false);
                    linearLayoutGrati.setVisibility(View.GONE);
                }

            }
        });

        btnGratitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();

            }
        });

    }

    public void imagePicker(){
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE).start(getContext(),this);

    }
    public static UserprofileFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        UserprofileFragment fragment = new UserprofileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                postToFirebase(resultUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("Faield","defwed");

                Exception error = result.getError();
            }
        }
    }

    private void postToFirebase(Uri resultUri) {
        mProgress.setMessage("Adding gratitude to cloud");
        mProgress.show();
        final StorageReference filepath = mStorage.child("AuxoGratitudeImage").child(resultUri.getLastPathSegment());
        UploadTask uploadTask = filepath.putFile(resultUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("Gratitude").setValue(downloadUri.toString());
                    mProgress.dismiss();


                    //startActivity(new Intent(AddPostAspire.this,HomeActivity.class));
                } else {
                    // Handle failures
                    // ...

                    Toast.makeText(getActivity(), "Something went wrong while adding Gratitude!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
