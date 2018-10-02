package com.auxomate.mynewself.mynewself.fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;

public class UserprofileFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Switch aSwitchTask, aSwitchVis, aSwitchQuoteScreen;
    private LinearLayout linearLayoutTask, linearLayoutVis;
    private EditText editTextTaskTime, editTextVisTime;
    private Button buttonSave;

    String TAG = "RemindMe";
    PrefManager localData;

    SwitchCompat reminderSwitch;
    TextView tvTime;

    LinearLayout ll_set_time;

    int hour, min;

    ClipboardManager myClipboard;

    public UserprofileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_userprofile, container, false);

        String name = PrefManager.getString(getActivity(),PrefManager.PRF_USERNAME_WELCOME);
        Toast.makeText(getActivity(),name, Toast.LENGTH_SHORT).show();

        init();

        return view;
    }

    private void init() {

        aSwitchTask = view.findViewById(R.id.userprofile_switch_task);
        aSwitchVis = view.findViewById(R.id.userprofile_switch_visualizations);
        aSwitchQuoteScreen = view.findViewById(R.id.userprofile_switch_quotescreen);

        linearLayoutTask = view.findViewById(R.id.userprofile_linear_task);
        linearLayoutVis = view.findViewById(R.id.userprofile_linear_visualizations);

        editTextTaskTime = view.findViewById(R.id.userprofile_edittext_tasktime);
        editTextVisTime = view.findViewById(R.id.userprofile_edittext_vistime);

        buttonSave = view.findViewById(R.id.userprofile_button_save);
        buttonSave.setOnClickListener(this);

        aSwitchTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    linearLayoutTask.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutTask.setVisibility(View.GONE);
                }

            }
        });

        aSwitchVis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    linearLayoutVis.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutVis.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userprofile_button_save:
                saveInfo();
                break;
        }
    }

    private void saveInfo() {

        String taskTime = editTextTaskTime.getText().toString().trim();
        String visTime = editTextVisTime.getText().toString().trim();

    }

    public static UserprofileFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        UserprofileFragment fragment = new UserprofileFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
