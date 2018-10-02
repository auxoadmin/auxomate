package com.auxomate.mynewself.mynewself.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.HomeActivity;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoToHomeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView textViewName;
    private Button buttonNext;

    public GoToHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_go_to_home, container, false);

        init();

        return view;
    }

    private void init() {

        String name = PrefManager.getString(getActivity(),PrefManager.PRF_USERNAME_WELCOME);

        textViewName = view.findViewById(R.id.gotohome_textview_name);
        textViewName.setText("Journey "+name+"!");
        buttonNext = view.findViewById(R.id.gotohome_button_next);
        buttonNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.gotohome_button_next:

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();

                break;
        }

    }
}
