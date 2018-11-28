package com.auxomate.mynewself.mynewself.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.activities.AspireGallery;
import com.auxomate.mynewself.mynewself.activities.HomeActivity;
import com.auxomate.mynewself.mynewself.models.AspireRecycler;
import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.AddPostAspire;

import com.auxomate.mynewself.mynewself.utilities.PrefManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AspireFragment extends Fragment  {

    View RootView;
    RecyclerView mRecycler;
   // DatabaseReference mDatabse;


    ProgressDialog mProgress;
    private static Uri resultUri= null;
    StorageReference mStorage;
    DatabaseReference mDatabase;
    String key;

    private static final int MAX_DIMENSION = 1200;
    Uri uploadUri;
    public Context applicationContext = HomeActivity.getContextOfApplication();
    FirebaseRecyclerAdapter<AspireRecycler,AspireViewHolder> firebaseRecyclerAdapter;
    private Paint p = new Paint();
    FloatingActionButton fab;
    PrefManager prefManager;


    public AspireFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecycler.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AspireRecycler, AspireViewHolder>(
                AspireRecycler.class,
                R.layout.aspire_recycler,
                AspireViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder  (AspireViewHolder viewHolder, final AspireRecycler model, int position) {



                viewHolder.setDes(model.getDescription());
                viewHolder.setImage(getContext(),model.getImage());



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),AspireGallery.class);
//                        intent.putExtra("imageUrl",model.getImage());
//                        intent.putExtra("title",model.getTitle());
//                        intent.putExtra("desc",model.getDescription());
                        startActivity(intent);
                    }
                });

            }
        };

        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }





    public static class AspireViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageView imageView;
        Layout layout;
        public static ArrayList<String> imageUrl = new ArrayList<String>();

        public AspireViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;





        }

        public void setDes(String description){
            imageView = mView.findViewById(R.id.aspire_recycler_image);
            TextView post_des= mView.findViewById(R.id.aspire_recycler_des);

            post_des.setText(description);
        }
        public void setImage(Context ctx, String image){




           // Picasso.with(ctx).load(image).resize(150,150).into(imageView);
            Picasso.with(ctx).load(image).resize(150,150)
                    .networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).resize(150,150).into(imageView);
                }
            });
//            Log.e("URL",image);
            imageUrl.add(image);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        RootView = inflater.inflate(R.layout.fragment_aspire, container, false);
        mRecycler = RootView.findViewById(R.id.aspire_recycler);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        fab = RootView.findViewById(R.id.aspire_fab_addvisual);

        //mDatabse = FirebaseDatabase.getInstance().getReference().child("Auxomate");

        init();
        initSwipe();

        return RootView;



    }

    private void init() {

        key = prefManager.getString(getActivity(),PrefManager.PRF_USERKEY);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Auxomate").child(key);
        mDatabase.keepSynced(true);


        mProgress= new ProgressDialog(getActivity());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddPostAspire.class));

            }
        });

    }







    public static AspireFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        AspireFragment fragment = new AspireFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu_aspire,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_aspire_action){
            startActivity(new Intent(getActivity(),AddPostAspire.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSwipe() {




        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();





                if (direction == ItemTouchHelper.LEFT)  {
                    firebaseRecyclerAdapter.getRef(position).removeValue();
                    firebaseRecyclerAdapter.notifyItemRemoved(position);
                    mRecycler.invalidate();
                   // Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                    Snackbar.make(viewHolder.itemView,"Deleted",Snackbar.LENGTH_SHORT).show();


                    // adapter.removeItem(position);

                }
// else {
//
////                    LayoutInflater li = LayoutInflater.from(getActivity());
////                    View cv = li.inflate(R.layout.edit_aspire_recyclerdata, null);
////                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                    builder.setView(cv);
////                    final AlertDialog show = builder.show();
////
////                    final EditText editText = (EditText) cv.findViewById(R.id.aspiredialog_edt);
////                    cv.findViewById(R.id.aspiredialog_btn_save).setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            AspireRecycler aspireRecycler = new AspireRecycler();
////                            final String s = editText.getText().toString();
////                            aspireRecycler.setDescription(s);
////
////                            firebaseRecyclerAdapter.getRef(position).setValue(aspireRecycler);
////                            firebaseRecyclerAdapter.notifyItemChanged(position);
////                            mRecycler.invalidate();
////
////
////                        }
////                    });
//
//                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {


                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                    else {
//                        p.setColor(Color.parseColor("#388E3C"));
//                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
//                        c.drawRect(background, p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
//                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecycler);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

}
