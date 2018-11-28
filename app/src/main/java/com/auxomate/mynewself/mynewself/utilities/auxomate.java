package com.auxomate.mynewself.mynewself.utilities;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.auxomate.mynewself.mynewself.dashboard.MenuPage;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class auxomate extends Application{




    @Override
    public void onCreate() {

        super.onCreate();

        PrefManager.putBoolean(this,PrefManager.TASK_NOTIFICATION,true);
        PrefManager.putBoolean(this,PrefManager.VISUAL_NOTIFICATION,true);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built= builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }


}
