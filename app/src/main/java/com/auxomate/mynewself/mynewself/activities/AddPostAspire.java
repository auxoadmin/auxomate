package com.auxomate.mynewself.mynewself.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.auxomate.mynewself.mynewself.R;
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

public class AddPostAspire extends AppCompatActivity implements View.OnClickListener {
    ImageButton imageButton;
    EditText post_title_edt,post_des_edt;
    Button post_btn;
    Uri resultUri = null;
    StorageReference mStorage;
    DatabaseReference mDatabase;
    ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_aspire);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Auxomate");

        imageButton=findViewById(R.id.post_add_image);
        imageButton.setOnClickListener(this);
        post_title_edt=findViewById(R.id.post_edit_text);
        post_des_edt=findViewById(R.id.post_multieditText);
        post_btn=findViewById(R.id.post_button);
        post_btn.setOnClickListener(this);

        mProgress= new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_add_image:
                CropImage.activity()
                        .setCropShape(CropImageView.CropShape.RECTANGLE).start(this);
                break;
            case R.id.post_button:
                postToFirebase();
                break;
        }

    }

    private void postToFirebase() {
        mProgress.setMessage("Posting");
        mProgress.show();
        final String title_val = post_title_edt.getText().toString().trim();
        final String des_val = post_des_edt.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(des_val) && resultUri != null){

            final StorageReference filepath = mStorage.child("AuxoImage").child(resultUri.getLastPathSegment());


            UploadTask uploadTask = filepath.putFile(resultUri);
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
                    newPost.child("title").setValue(title_val);
                    newPost.child("description").setValue(des_val);
                    newPost.child("image").setValue(downloadUri.toString());

                    mProgress.dismiss();
                    startActivity(new Intent(AddPostAspire.this,HomeActivity.class));
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });




        }
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
