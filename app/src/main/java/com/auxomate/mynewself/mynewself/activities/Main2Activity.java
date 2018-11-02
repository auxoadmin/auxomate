package com.auxomate.mynewself.mynewself.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.auxomate.mynewself.mynewself.R;

public class Main2Activity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView=findViewById(R.id.stringtoImage_Imageview);
        Intent i = getIntent();
        String des = i.getStringExtra("quote");
        Log.d("getIncomingIntent",des);
        Bitmap bm = StringToBitMap(des);
        imageView.setImageBitmap(bm);   //MyPhoto is image control.

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
