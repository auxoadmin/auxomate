package com.auxomate.mynewself.mynewself.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.fragments.ActFragment;
import com.auxomate.mynewself.mynewself.fragments.AspireFragment;
import com.auxomate.mynewself.mynewself.fragments.AwareFragment;
import com.auxomate.mynewself.mynewself.fragments.GoToHomeFragment;
import com.auxomate.mynewself.mynewself.fragments.UserNameFragment;
import com.auxomate.mynewself.mynewself.fragments.UserWelcomeFragment;
import com.auxomate.mynewself.mynewself.fragments.UserprofileFragment;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WelcomeNameActivity extends FragmentActivity implements View.OnClickListener {

    private TextInputLayout inputLayoutName;
    Button nameBtn;
    private PrefManager prefManager;
    EditText nameEdt;

    public ViewPager viewPagerFrags;
   // public Button buttonNext, buttonSkip;
    public boolean isUserSet = false;

    public UserNameFragment userNameFragment;
    public UserWelcomeFragment userWelcomeFragment;
    public AwareFragment awareFragment;
    public AspireFragment aspireFragment;
    public ActFragment actFragment;
    public UserprofileFragment userprofileFragment;
    public GoToHomeFragment goToHomeFragment;
    private static final int MAX_DIMENSION = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (prefManager.getBoolean(this,PrefManager.PRF_WELCOME_BOARDS)) {

            startActivity(new Intent(WelcomeNameActivity.this, HomeActivity.class));

            finish();
        }


        setContentView(R.layout.activity_welcome_name);

        inputLayoutName = (TextInputLayout) findViewById(R.id.nameInputLayout);
        nameBtn = findViewById(R.id.nameBtn);
        nameBtn.setOnClickListener(this);
        nameEdt = findViewById(R.id.nameEdt);


        init();


    }

    private void init() {

       // buttonNext = findViewById(R.id.welcomename_button_next);
//        buttonNext.setOnClickListener(this);
//        buttonNext.setVisibility(View.GONE);
//
//       // buttonSkip = findViewById(R.id.welcomename_button_skip);
//        buttonSkip.setOnClickListener(this);

        viewPagerFrags = findViewById(R.id.welcomname_viewpager_frags);
        setupViewPager(viewPagerFrags);


        viewPagerFrags.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

//            @Override
//            public void onPageSelected(int position) {
//                switch (position){
//                    case 0:
//                        buttonSkip.setVisibility(View.GONE);
//                      //  buttonNext.setVisibility(View.VISIBLE);
//                        buttonNext.setVisibility(View.GONE);
//                        break;
//                    case 1:
//                        buttonSkip.setVisibility(View.GONE);
//                       // buttonNext.setText("Begin reinventing yourself!!!");
//                        buttonNext.setVisibility(View.VISIBLE);
//                        break;
//                    case 2:
//                        buttonSkip.setVisibility(View.VISIBLE);
//                        buttonNext.setText("Next");
//                        buttonNext.setVisibility(View.VISIBLE);
//                        break;
//                    case 3:
//                        buttonSkip.setVisibility(View.VISIBLE);
//                        buttonNext.setVisibility(View.VISIBLE);
//                        break;
//                    case 4:
//                        buttonSkip.setVisibility(View.VISIBLE);
//                        buttonNext.setVisibility(View.VISIBLE);
//                        break;
//                    case 5:
//                        buttonSkip.setVisibility(View.GONE);
//                        buttonNext.setVisibility(View.VISIBLE);
//                        break;
//                    case 6:
//                        buttonSkip.setVisibility(View.GONE);
//                        buttonNext.setVisibility(View.GONE);
//                        break;
//                }
//            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        if (userNameFragment == null){
            userNameFragment = new UserNameFragment();
        }
        if (userWelcomeFragment == null){
            userWelcomeFragment = new UserWelcomeFragment();
        }
        if (awareFragment == null){
            awareFragment = new AwareFragment();
        }
        if (aspireFragment == null){
            aspireFragment = new AspireFragment();
        }
        if (actFragment == null){
            actFragment = new ActFragment();
        }
        if (userprofileFragment == null){
            userprofileFragment = new UserprofileFragment();
        }
        if (goToHomeFragment == null){
            goToHomeFragment = new GoToHomeFragment();
        }

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(userNameFragment, "User Name");
        adapter.addFragment(userWelcomeFragment, "User Welcome");
//        adapter.addFragment(awareFragment, "Aware");
//        adapter.addFragment(aspireFragment, "Aspire");
//        adapter.addFragment(actFragment, "Act");
//        adapter.addFragment(userprofileFragment, "User Profile");
 //       adapter.addFragment(goToHomeFragment, "Go To Home");
        viewPager.setAdapter(adapter);



    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.nameBtn:
                String name = nameEdt.getText().toString();
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);

                break;

//            case R.id.welcomename_button_next:
//
//                if (viewPagerFrags.getCurrentItem()==0){
//
//                    viewPagerFrags.setCurrentItem(viewPagerFrags.getCurrentItem() + 1, true);
//
//                }else if (viewPagerFrags.getCurrentItem() == 5){
//
//                    Intent intent = new Intent(WelcomeNameActivity.this,HomeActivity.class);
//                    startActivity(intent);
//                    this.finish();
//
//                }else if(viewPagerFrags.getCurrentItem() == 1){
//                    viewPagerFrags.setCurrentItem(6, true);
//                }
//
//                else {
//                    viewPagerFrags.setCurrentItem(viewPagerFrags.getCurrentItem() + 1, true);
//                }
//
//                break;
//
//            case R.id.welcomename_button_skip:
//                Intent intent = new Intent(WelcomeNameActivity.this,HomeActivity.class);
//                startActivity(intent);
//                this.finish();
//                break;
        }
    }




}
