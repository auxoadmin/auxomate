package com.auxomate.mynewself.mynewself.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.fragments.AspireFragment;
import com.auxomate.mynewself.mynewself.fragments.AwareFragment;
import com.auxomate.mynewself.mynewself.models.AudioModel;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddPostAspire extends AppCompatActivity implements View.OnClickListener {
    ImageView imageButton;
    EditText post_des_edt;
    Button post_btn;

    StorageReference mStorage;
    DatabaseReference mDatabase;
    ProgressDialog mProgress;
    private static Uri resultUri= null;
    String key;

    private static final int MAX_DIMENSION = 1200;
    Uri uploadUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_aspire);

        init();



    }

    private void init() {

        key = PrefManager.getString(getApplicationContext(),PrefManager.PRF_USERKEY);

        mStorage = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Auxomate").child(key);

        imageButton=findViewById(R.id.aspire_imgbutton_add);
        imageButton.setOnClickListener(this);

        post_des_edt=findViewById(R.id.aspire_edittext_description);
        post_btn=findViewById(R.id.aspire_button_submit);
        post_btn.setOnClickListener(this);

        mProgress= new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aspire_imgbutton_add:
                CropImage.activity()
                        .setCropShape(CropImageView.CropShape.RECTANGLE).start(this);
                break;
            case R.id.aspire_button_submit:
                String name = post_des_edt.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Snackbar.make(view,"Please enter Your feelings.",Snackbar.LENGTH_LONG).show();


                }else {
                    postToFirebase();
                    imageButton.setImageResource(R.drawable.add_btn);
                }
                break;
        }

    }

    private void postToFirebase() {
        Log.d("postToFirebase",resultUri.toString());
        mProgress.setMessage("Posting");
        mProgress.show();
        final String des_val = post_des_edt.getText().toString().trim();

        if(!TextUtils.isEmpty(des_val) && resultUri != null){
            try {
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri),
                                MAX_DIMENSION);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
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
                        post_des_edt.setText("");

                        finish();

                        //startActivity(new Intent(AddPostAspire.this,HomeActivity.class));
                    } else {
                        // Handle failures
                        // ...
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext() , "Something went wrong while adding post!", Toast.LENGTH_LONG).show();
                    }
                }
            });




        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                imageButton.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("Faield","defwed");

                Exception error = result.getError();
            }
        }
    }
}
