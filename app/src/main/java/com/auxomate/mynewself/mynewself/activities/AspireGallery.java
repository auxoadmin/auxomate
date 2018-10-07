package com.auxomate.mynewself.mynewself.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.adapters.AspireViewPageAdapter;
import com.auxomate.mynewself.mynewself.fragments.AspireFragment;
import com.auxomate.mynewself.mynewself.models.AspireNotificationPager;
import com.auxomate.mynewself.mynewself.models.AspireRecycler;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AspireGallery extends AppCompatActivity {


    DatabaseReference mDatabase;
    String key;
    ArrayList<String> urlList ;
    ArrayList<String> desc ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspire_gallery);
        key = PrefManager.getString(this,PrefManager.PRF_USERKEY);
        urlList = new ArrayList<>();
        desc = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Auxomate").child(key);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    getImageUrl(dataSnapshot);

                } else {
                    Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //  getIncomingIntent();


    }

    private void getImageUrl(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            AspireNotificationPager ar = dataSnapshot.getValue(AspireNotificationPager.class);
            urlList.add(ar.getImage());
            desc.add(ar.getDescription());
            Log.d("ImageUrlLIst",ar.getImage());

            ViewPager viewPager = findViewById(R.id.aspire_viewpager);
            AspireViewPageAdapter aspireViewPageAdapter = new AspireViewPageAdapter(AspireGallery.this, urlList,desc);
            viewPager.setAdapter(aspireViewPageAdapter);


        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }

    //    private void getIncomingIntent(){
//        Log.d("Gallery","Incoming Intent");
//        if(getIntent().hasExtra("imageUrl")&&getIntent().hasExtra("title")&&getIntent().hasExtra("desc")){
//
//            String url = getIntent().getStringExtra("imageUrl");
//            String title = getIntent().getStringExtra("title");
//            String desc = getIntent().getStringExtra("desc");
//
//
//        }
//    }
//    private void setGalleryData(String imageUrl,String title,String desc){
//        galleryTitle.setText(title);
//        getGalleryDesc.setText(desc);
//        Picasso.with(getApplicationContext()).load(imageUrl).resize(900,600).into(galleryImgeView);
//
//    }
}
