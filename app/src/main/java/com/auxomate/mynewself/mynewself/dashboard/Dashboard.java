package com.auxomate.mynewself.mynewself.dashboard;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.auxomate.mynewself.mynewself.utilities.auxomate;

import java.util.ArrayList;

public class Dashboard {

    static ArrayList<Fragment> fragments;
    private Activity activity;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Integer> imagesList = new ArrayList<>();
    static long transitionDuration = 1000;

    private Dashboard(@NonNull Activity activity, ArrayList<String> list,
                     ArrayList<Integer> imagesList, ArrayList<Fragment> fragmentsList) {
        this.activity = activity;
        this.list = list;
        this.imagesList = imagesList;
        fragments = fragmentsList;
    }

    public static Dashboard initialize(@NonNull Activity activity, ArrayList<String> list,
                                      ArrayList<Integer> imagesList, ArrayList<Fragment> fragmentsList) {
        return new Dashboard(activity, list, imagesList, fragmentsList);
    }

    public static ArrayList<Fragment> getFragments() {
        return fragments;
    }

    public void start() {
        activity.finish();
        MenuPage.startActivity(activity, list, imagesList);
    }

    public void setTransitionDuration(long milliSeconds) {
        transitionDuration = milliSeconds;
    }

    public static long getTransitionDuration() {
        return transitionDuration;
    }
}
