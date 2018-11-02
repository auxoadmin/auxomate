package com.auxomate.mynewself.mynewself.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserWelcomeFragment extends Fragment {

    private View view;
    private TextView textViewName;
    boolean isFragmentLoaded = false;

    public UserWelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_welcome, container, false);

        init();

        return view;
    }

    private void init() {

        textViewName = view.findViewById(R.id.userwelcome_textview_name);


    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded) {
            //Load Your Data Here like.... new GetContacts().execute();
            String name = PrefManager.getString(getActivity(),PrefManager.PRF_USERNAME_WELCOME);
            textViewName.setText(name);

            isFragmentLoaded = true;
        }
        else{
        }
    }

}
