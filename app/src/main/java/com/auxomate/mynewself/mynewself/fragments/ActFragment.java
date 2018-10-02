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

    private static final String CLOUD_VISION_API_KEY = BuildConfig.API_KEY;
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MAX_DIMENSION = 1200;
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    ProgressDialog mProgress;
    public static Activity context=null;
    private static String visionString;
    Uri resultUri = null;
    ImageButton mMainImage;
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

        RootView = inflater.inflate(R.layout.fragment_act, container, false);
        //task1 = RootView.findViewById(R.id.task1);
        mMainImage = RootView.findViewById(R.id.act_imgbutton_add);

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











        mProgress= new ProgressDialog(getActivity());
        mMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefManager.putString(getActivity(),PrefManager.PRF_FROMWHERE_FRAGS,"act");
                CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE).start(getActivity());
            }
        });



        return RootView;
    }


    public void uploadImage(Activity act,Uri uri) {

        mProgress.setMessage("Rendaring Your Image");
        mProgress.show();

        context = act;
        Log.d("uploadImage",uri.toString());
        if (uri != null) {
            try {
               //  scale the image to save on bandwidth
               Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri),
                                MAX_DIMENSION);


                callCloudVision(act,bitmap);
               // mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                //Toast.makeText(getActivity(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
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

                        String packageName = context.getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(context.getPackageManager(), packageName);

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
        Log.d(TAG, "created Cloud Vision request object, sending request");

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
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(mContext,response);


            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
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
                Intent i = new Intent(activity, TaskSubmit.class);
                i.putExtra("visionResult", visionString);
                startActivity(i);
            }
            else
            {
                Log.d("onPostExecute","Failed");
            }




        }


    }



    private void callCloudVision(Activity act, final Bitmap bitmap) {
        // Switch text to loading
        //mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> textDetectionTask = new TextDetectionTask(act, prepareAnnotationRequest(bitmap));
            textDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
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






    public static ActFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        ActFragment fragment = new ActFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
