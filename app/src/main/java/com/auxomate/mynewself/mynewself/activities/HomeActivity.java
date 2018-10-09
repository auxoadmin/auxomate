package com.auxomate.mynewself.mynewself.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.fragments.UserprofileFragment;
import com.auxomate.mynewself.mynewself.fragments.ActFragment;
import com.auxomate.mynewself.mynewself.fragments.AspireFragment;
import com.auxomate.mynewself.mynewself.fragments.AwareFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;



public class HomeActivity extends AppCompatActivity  {

    AspireFragment aspireFragment;
    ActFragment actFragment;
    Button aware,aspire,act;
    ArrayList<String> menuList = new ArrayList<>();     //menu titles
    ArrayList<Integer> imagesList = new ArrayList<>();      //menu backgrounds
    ArrayList<Fragment> fragmentsList = new ArrayList<>();

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    FragmentManager fragmentManager= getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();


    public String fromWhere="";

ActionBar toolbar ;
   //private ActionBar toolbar;
   PrefManager prefManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager= getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_aware:
                    transaction.replace(R.id.container,new AwareFragment()).commit();

                   toolbar.setTitle("Aware");
                    return true;
                case R.id.navigation_aspire:

                    transaction.replace(R.id.container,new AspireFragment()).commit();

                    toolbar.setTitle("Aspire");
                    return true;
                case R.id.navigation_act:
                    transaction.replace(R.id.container,actFragment).commit();

                    toolbar.setTitle("Act");
                    return true;
                case R.id.navigation_userprofile:
                    transaction.replace(R.id.container,new UserprofileFragment()).commit();

                    toolbar.setTitle("User Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        menuList.add("Aware");       //add titles
//        menuList.add("Aspire");
//        menuList.add("Act");
//        menuList.add("User");

//        aware = findViewById(R.id.home_AwareFrag);
//        aware.setOnClickListener(this);
//        aspire=findViewById(R.id.home_AspireFrag);
//        aspire.setOnClickListener(this);
//        act=findViewById(R.id.home_ActFrag);
//        act.setOnClickListener(this);
//        imagesList.add(R.drawable.aware_144dp);        //add background images
//        imagesList.add(R.drawable.aspire_144dp);
//        imagesList.add(R.drawable.act_144dp);
//        imagesList.add(R.drawable.user_24dp);
//
//        fragmentsList.add(AwareFragment.newInstance("Aware"));      //add fragment instances
//        fragmentsList.add(AspireFragment.newInstance("Aspire"));
//        fragmentsList.add(ActFragment.newInstance("Act"));
//        fragmentsList.add(UserprofileFragment.newInstance("User Profile"));
//        toolbar = findViewById(R.id.toolbar);

//        Allagi allagi = Allagi.initialize(HomeActivity.this, menuList, imagesList, fragmentsList);
//        allagi.start();

        contextOfApplication = getApplicationContext();



        toolbar = getSupportActionBar();




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        aspireFragment = new AspireFragment();
        actFragment = new ActFragment();



        //navigation.setOnNavigationItemSelectedListener();
        navigation.setSelectedItemId(R.id.navigation_act);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        transaction.replace(R.id.container,actFragment).commit();
        toolbar.setTitle("Act");



    }

//    @Override
//    public void onClick(View v) {
////            FragmentManager fragmentManager= getSupportFragmentManager();
////            FragmentTransaction transaction = fragmentManager.beginTransaction();
//        switch (v.getId()){
//            case R.id.home_AwareFrag:
//                transaction.replace(R.id.container,new AwareFragment()).commit();
//
//                   //toolbar.setTitle("Aware");
//
//                break;
//            case R.id.home_AspireFrag:
//                transaction.replace(R.id.container,new AspireFragment()).commit();
//
//               // toolbar.setTitle("Aspire");
//                break;
//            case R.id.home_ActFrag:
//                transaction.replace(R.id.container,new ActFragment()).commit();
//
//               // toolbar.setTitle("Act");
//                break;
//        }
//
//    }




}
