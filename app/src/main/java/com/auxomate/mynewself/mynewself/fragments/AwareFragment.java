package com.auxomate.mynewself.mynewself.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.models.AudioModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AwareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AwareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AwareFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int AUDIO_PERMISSIONS_REQUEST = 0 ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button recordBtn,recordBtnStop,uploadBtn;
    ImageView recordBtnPlay;

    RecyclerView recyclerView;
    String mFileName = null;
    // Create a storage reference from our app

    StorageReference storageRef;
    DatabaseReference mDatabase;
    private MediaRecorder mRecorder = null;
    private PrefManager prefManager;




    public AwareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AwareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AwareFragment newInstance(String param1, String param2) {
        AwareFragment fragment = new AwareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied YOU CAN NOT RECORD AUDIO", Toast.LENGTH_SHORT).show();
                }

            }
            break;
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied YOU CAN NOT READ DATA FROM YOUR PHONE", Toast.LENGTH_SHORT).show();
                }


            }
            break;
            case 3: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied YOU CAN NOT STORE YOUR DATA INTO PHONE", Toast.LENGTH_SHORT).show();
                }

            }
            break;
            default: {
                Log.d("exeute defaule", "not execute");
                return;
            }
        }
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String key = PrefManager.getString(getActivity(),PrefManager.PRF_USERKEY);

        final View RootView = inflater.inflate(R.layout.fragment_aware, container, false);
        storageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Recordings").child(key);


        recordBtn=RootView.findViewById(R.id.recordBtn);
        recordBtn.setOnClickListener(this);
        recordBtnPlay=RootView.findViewById(R.id.recordBtnPlay);
        recordBtnPlay.setOnClickListener(this);
        recordBtnStop=RootView.findViewById(R.id.recordBtnStop);
        recordBtnStop.setOnClickListener(this);

        recyclerView =  RootView.findViewById(R.id.aware_recycler);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        recordBtnStop.setEnabled(false);
        recordBtnPlay.setEnabled(false);


        String namFile =  "Auxomate";
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";
        // Inflate the layout for this fragment

        return RootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AudioModel,AwareViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AudioModel, AwareViewHolder>(
                AudioModel.class,
                R.layout.listitem_recycler,
                AwareViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(AwareViewHolder viewHolder, AudioModel model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setUrl(model.getUrl());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class AwareViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        View mView;
        public AwareViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            mView.setOnCreateContextMenuListener(this);
        }

        public void setName(String name ){
            TextView textView = mView.findViewById(R.id.aware_recycler_tv);
            textView.setText(name);
        }

        public void setUrl(final String url){
            Log.d("AudioUrl",url);
            ImageButton imageButton = mView.findViewById(R.id.aware_recycler_btn);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                        mediaPlayer.prepare();

                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Delete");


        }



    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "Aware", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.recordBtn:

                    startRecording();
                    recordBtn.setEnabled(false);
                    recordBtnStop.setEnabled(true);
                    //recordBtn.setText("Recording");



                break;
            case R.id.recordBtnPlay:

                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(mFileName);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {

                }
                break;
            case R.id.recordBtnStop:
                stopRecording();
                recordBtn.setEnabled(true);
                recordBtnStop.setEnabled(false);
                recordBtnPlay.setEnabled(true);
                uploadAudio();
                break;


        }

    }

    private void uploadAudio() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View cv = li.inflate(R.layout.dialog_recordingname, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(cv);
        final AlertDialog show = builder.show();

        final EditText editText = (EditText) cv.findViewById(R.id.dialog_edt);


        cv.findViewById(R.id.dialog_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recordBtn.setText("Record");
                final String s = editText.getText().toString();
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("audio/3gp")
                        .build();
                UploadTask uploadTask ;
                final StorageReference filepath = storageRef.child("Recordings").child(s+"3gp");
                Uri uri = Uri.fromFile(new File(mFileName));
                uploadTask = filepath.putFile(uri);
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
                            Log.d("DownlodAudiourl",downloadUri.toString());
                            DatabaseReference newRecording = mDatabase.push();
                                    newRecording.child("name").setValue(s);
                                    newRecording.child("url").setValue(downloadUri.toString());
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
                show.dismiss();
            }
        });


    }

    private void startRecording() {

//        if(PermissionUtils.requestPermission(getActivity(), AUDIO_PERMISSIONS_REQUEST,
//              android.Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)) {
//            Log.d("permission","granted");
//
//        }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {

                mRecorder.prepare();
                Toast.makeText(getActivity(), "Recording Started", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("Recording log", "prepare() failed");
            }




        mRecorder.start();


    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public static AwareFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        AwareFragment fragment = new AwareFragment();
        fragment.setArguments(args);
        return fragment;
    }
}



