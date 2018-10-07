package com.auxomate.mynewself.mynewself.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.activities.AspireGallery;
import com.auxomate.mynewself.mynewself.activities.HomeActivity;
import com.auxomate.mynewself.mynewself.models.AspireRecycler;
import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.AddPostAspire;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AspireFragment extends Fragment implements View.OnClickListener {

    View RootView;
    RecyclerView mRecycler;
   // DatabaseReference mDatabse;

    public ImageView imageButtonAdd;
    private EditText editTextDesc;
    private Button buttonSubmit;
    ProgressDialog mProgress;
    private static Uri resultUri= null;
    StorageReference mStorage;
    DatabaseReference mDatabase;
    String key;
    public String [] imageUrl;
    private static final int MAX_DIMENSION = 1200;
    Uri uploadUri;
    Context applicationContext = HomeActivity.getContextOfApplication();


    public AspireFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AspireRecycler,AspireViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AspireRecycler, AspireViewHolder>(
                AspireRecycler.class,
                R.layout.aspire_recycler,
                AspireViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder  (AspireViewHolder viewHolder, final AspireRecycler model, int position) {



                viewHolder.setDes(model.getDescription());
                viewHolder.setImage(getContext(),model.getImage());



//                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getActivity(),AspireGallery.class);
////                        intent.putExtra("imageUrl",model.getImage());
////                        intent.putExtra("title",model.getTitle());
////                        intent.putExtra("desc",model.getDescription());
//                        startActivity(intent);
//                    }
//                });

            }
        };

        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.aspire_imgbutton_add:
                PrefManager.putString(getActivity(),PrefManager.PRF_FROMWHERE_FRAGS,"aspire");
                CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE).start(getContext(),this);

                break;

            case R.id.aspire_button_submit:

                String name = editTextDesc.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Snackbar.make(view,"Please enter Your feelings.",Snackbar.LENGTH_LONG).show();


                }else {
                    postToFirebase();
                    imageButtonAdd.setImageResource(R.drawable.add_btn);
                }

//                Intent intent = new Intent(getActivity(),AspireGallery.class);
//                startActivity(intent);

                break;

        }
    }

    public void setImageUri(Uri uri) {
        Log.d("setImageUri",uri.toString());
        imageButtonAdd.setImageURI(uri);
    }

    public static class AspireViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton imageView;
        Layout layout;
        public static ArrayList<String> imageUrl = new ArrayList<String>();

        public AspireViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }

        public void setDes(String description){
            TextView post_des= mView.findViewById(R.id.aspire_recycler_des);

            post_des.setText(description);
        }
        public void setImage(Context ctx, String image){

            ImageView imageView = mView.findViewById(R.id.aspire_recycler_image);
            Picasso.with(ctx).load(image).resize(150,150).into(imageView);
            Log.e("URL",image);
            imageUrl.add(image);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView = inflater.inflate(R.layout.fragment_aspire, container, false);
        mRecycler = RootView.findViewById(R.id.aspire_recycler);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //mDatabse = FirebaseDatabase.getInstance().getReference().child("Auxomate");

        init();

        return RootView;



    }

    private void init() {

        key = PrefManager.getString(getActivity(),PrefManager.PRF_USERKEY);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Auxomate").child(key);

        imageButtonAdd = RootView.findViewById(R.id.aspire_imgbutton_add);
        imageButtonAdd.setOnClickListener(this);
        editTextDesc = RootView.findViewById(R.id.aspire_edittext_description);
        buttonSubmit = RootView.findViewById(R.id.aspire_button_submit);
        buttonSubmit.setOnClickListener(this);
        mProgress= new ProgressDialog(getActivity());

    }

    private void postToFirebase() {

        Log.d("postToFirebase",resultUri.toString());
        mProgress.setMessage("Posting");
        mProgress.show();
        final String des_val = editTextDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(des_val) && resultUri != null){
            try {
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), resultUri),
                                MAX_DIMENSION);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), bitmap, "Title", null);
                uploadUri = Uri.parse(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final StorageReference filepath = mStorage.child("AuxoImage").child(uploadUri.getLastPathSegment());


            UploadTask uploadTask = filepath.putFile(uploadUri);
            //                    DatabaseReference newPost = mDatabase.push();
//                    newPost.child("title").setValue(title_val);
//                    newPost.child("description").setValue(des_val);
//                    newPost.child("image").setValue(mStorage.getDownloadUrl().toString());
//
//                    mProgress.dismiss();
//                    startActivity(new Intent(AddPostAspire.this,HomeActivity.class));
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
                        newPost.child("description").setValue(des_val);
                        newPost.child("image").setValue(downloadUri.toString());
                        mProgress.dismiss();
                        editTextDesc.setText("");

                        //startActivity(new Intent(AddPostAspire.this,HomeActivity.class));
                    } else {
                        // Handle failures
                        // ...
                        mProgress.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong while adding post!", Toast.LENGTH_LONG).show();
                    }
                }
            });




        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                imageButtonAdd.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static AspireFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        AspireFragment fragment = new AspireFragment();
        fragment.setArguments(args);
        return fragment;
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
