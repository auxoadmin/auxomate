package com.auxomate.mynewself.mynewself.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.WelcomeActivity;
import com.auxomate.mynewself.mynewself.activities.WelcomeNameActivity;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserNameFragment extends Fragment {

    private View view;
    private EditText editTextName;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private boolean isDone;
    private Button buttonSubmit;

    public UserNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_name, container, false);
   

        init();


        return view;
    }

    private void init() {

        editTextName = view.findViewById(R.id.username_edittext_username);
        buttonSubmit = view.findViewById(R.id.usename_button_submit);
        buttonSubmit.setVisibility(View.GONE);

        ((WelcomeNameActivity)getActivity()).buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Snackbar.make(view,"Please enter your name.",Snackbar.LENGTH_LONG).show();


                }else {

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    PrefManager.putString(getActivity(),PrefManager.PRF_USERNAME_WELCOME,name);
                    String key = databaseReference.child("users").push().getKey();

                    databaseReference.child("users").child(key).child("user_name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                PrefManager.putString(getActivity(),PrefManager.PRF_USERKEY,key);

                                PrefManager.putBoolean(getActivity(),PrefManager.PRF_WELCOME_BOARDS,true);
                                // Toast.makeText(getActivity(), "User created!", Toast.LENGTH_LONG).show();
                                ((WelcomeNameActivity)getActivity()).viewPagerFrags.setCurrentItem(((WelcomeNameActivity)getActivity()).viewPagerFrags.getCurrentItem()+1, true);

                            } else {
                                progressDialog.dismiss();
                                Log.e("User_ADD", Objects.requireNonNull(task.getException()).getMessage());
                                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });



                }

            }
        });




    }





}
