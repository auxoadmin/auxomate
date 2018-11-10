package com.auxomate.mynewself.mynewself.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.BuildConfig;
import com.auxomate.mynewself.mynewself.activities.AddPostAspire;
import com.auxomate.mynewself.mynewself.activities.AddTask;
import com.auxomate.mynewself.mynewself.activities.Main2Activity;
import com.auxomate.mynewself.mynewself.activities.MainActivity;
import com.auxomate.mynewself.mynewself.activities.TaskSubmit;
import com.auxomate.mynewself.mynewself.models.AspireRecycler;
import com.auxomate.mynewself.mynewself.utilities.PackageManagerUtils;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.models.AudioModel;
import com.auxomate.mynewself.mynewself.utilities.RecordDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.alhazmy13.wordcloud.ColorTemplate;
import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.os.FileObserver.DELETE;


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
    private Paint p = new Paint();
    RecordDialog recordDialog;
    String key ;
    String keynode;
    AudioModel audioModel;
    FirebaseRecyclerAdapter<AudioModel,AwareViewHolder> firebaseRecyclerAdapter;

    private static final String CLOUD_VISION_API_KEY = BuildConfig.API_KEY;
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;
    ProgressDialog mProgress;
    public static Activity context=null;
    private static String visionString;
    String wordCloudString = null;
    List<WordCloud> items;
    View RootView;





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
       // setHasOptionsMenu(true);

        String key = PrefManager.getString(getActivity(),PrefManager.PRF_USERKEY);
        RootView = inflater.inflate(R.layout.fragment_aware, container, false);
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
        mProgress = new ProgressDialog(getActivity());
        // Inflate the layout for this fragment
        initSwipe();

        return RootView;
    }

    private void initSwipe() {




        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();





                if (direction == ItemTouchHelper.LEFT) {
                    firebaseRecyclerAdapter.getRef(position).removeValue();
                    firebaseRecyclerAdapter.notifyItemRemoved(position);
                    recyclerView.invalidate();

                    //Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                    Snackbar.make(viewHolder.itemView,"Deleted",Snackbar.LENGTH_SHORT).show();
                    Log.d("keynode",keynode);

                   // adapter.removeItem(position);

                } else {


                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {
//                        p.setColor(Color.parseColor("#388E3C"));
//                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
//                        c.drawRect(background, p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
//                        c.drawBitmap(icon, null, icon_dest, p);

                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
//                    else {
//                        p.setColor(Color.parseColor("#D32F2F"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background, p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
//                        c.drawBitmap(icon, null, icon_dest, p);
//                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AudioModel, AwareViewHolder>(
                AudioModel.class,
                R.layout.listitem_recycler,
                AwareViewHolder.class,
                mDatabase
        ) {

            public int position;

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }
            @Override
            protected void populateViewHolder(AwareViewHolder viewHolder, AudioModel model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setUrl(model.getUrl());

                viewHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        model.setPosition(position);

                       // setPosition(position);
                        return false;
                    }
                });

                keynode= this.getRef(position).getKey();




            }


        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);






    }



    public static class AwareViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        View mView;
        TextView textView;

        public AwareViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
           // mView.setOnCreateContextMenuListener(this);

        }

        public void setName(String name) {
            textView = mView.findViewById(R.id.aware_recycler_tv);
            textView.setText(name);
        }


        public void setUrl(final String url) {

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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, DELETE, 0, "Delete");
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


                recordDialog = RecordDialog.newInstance("Record Audio");
                recordDialog.setMessage("Press for record");
                recordDialog.show(getActivity().getFragmentManager(),"TAG");
                recordDialog.setPositiveButton("Save", new RecordDialog.ClickListener() {
                    @Override
                    public void OnClickListener(String path) {
                        //Toast.makeText(getActivity(),"Save audio: " + path, Toast.LENGTH_LONG).show();
                        uploadAudio(path);
                    }
                });

                  //  startRecording();
                    //recordBtn.setEnabled(false);
                    //recordBtnStop.setEnabled(true);
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
               // uploadAudio();
                break;


        }

    }

    private void uploadAudio(String path) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View cv = li.inflate(R.layout.dialog_recordingname, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(cv);
        final AlertDialog show = builder.show();

        final EditText editText = (EditText) cv.findViewById(R.id.dialog_edt);


        cv.findViewById(R.id.dialog_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s = editText.getText().toString();
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("audio/wav")
                        .build();
                UploadTask uploadTask ;
                final StorageReference filepath = storageRef.child("Recordings").child(s+"wav");
                Uri uri = Uri.fromFile(new File(path));
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        DatabaseReference mPostReference = null;

        try {
            position = audioModel.getPosition();


        } catch (Exception e) {
//            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {

            case 3:
                //delete message

                if (position!= -1) {


                    firebaseRecyclerAdapter.getRef(position).removeValue();
                    firebaseRecyclerAdapter.notifyItemRemoved(position);
                    recyclerView.invalidate();
                    Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                }


                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu_aware,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_aware_action){
            CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE).start(getContext(),this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                uploadImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    public void uploadImage(Uri uri) {

        mProgress.setMessage("Processing your image");
        mProgress.show();


        Log.d("uploadImage",uri.toString());
        if (uri != null) {
            try {
                //  scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri),
                                MAX_DIMENSION);


                callCloudVision(bitmap);
                // mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d("AddTask", "Image picking failed because " + e.getMessage());
                //Toast.makeText(getActivity(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("AddTask", "Image picker gave us a null image.");
            // Toast.makeText(getActivity(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getActivity().getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getActivity().getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature textDetection = new Feature();
                textDetection.setType("DOCUMENT_TEXT_DETECTION");
                textDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(textDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d("AddTask", "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private class TextDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<Activity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        //public AddTask activity;
        private  Context mContext;


        TextDetectionTask(Activity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("AddTask", "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(mContext,response);


            } catch (GoogleJsonResponseException e) {
                Log.d("AddTask", "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d("AddTask", "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            visionString = result;
            // taskSubmit();


            Activity activity = mActivityWeakReference.get();
            if(activity!=null && !activity.isFinishing()) {
                mProgress.dismiss();
                Log.d("result", result);



                Log.d("incomingWordCloudString",result);




                Intent i = new Intent(getActivity(),MainActivity.class);
                i.putExtra("wordCloud",result);
                startActivity(i);






            }
            else
            {
                Log.d("onPostExecute","Failed");
            }




        }


    }



    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        //mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> textDetectionTask = new  TextDetectionTask(getActivity(), prepareAnnotationRequest(bitmap));
            textDetectionTask.execute();
        } catch (IOException e) {
            Log.d("AddTask", "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }



    private static String convertResponseToString(Context ctx, BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("");




        //   BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponses();

        for (AnnotateImageResponse res : responses) {
            if (res == null) {
                Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();

            }
            String pageList = res.getFullTextAnnotation().getText();
            Log.d("VisionList",pageList);

            // For full list of available annotations, see http://g.co/cloud/vision/docs

            message.append(String.format(Locale.US, "%s", res.getFullTextAnnotation().getText()));
            message.append("\n");

        }




        return message.toString();
    }
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }



}



