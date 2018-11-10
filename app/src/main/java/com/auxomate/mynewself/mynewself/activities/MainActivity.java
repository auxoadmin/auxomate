package com.auxomate.mynewself.mynewself.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auxomate.mynewself.mynewself.R;

import net.alhazmy13.wordcloud.ColorTemplate;
import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    String wordCloudString = null;
    List<WordCloud> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WordCloudView wordCloud = findViewById(R.id.wordCloud);
        //View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main, null);

        wordCloudString = getIntent().getStringExtra("wordCloud");
        Log.d("incomingWordCloudString", wordCloudString);


        String[] data = wordCloudString.split("\\r?\\n");
        Log.d("wordcloudeSplit", Arrays.toString(data));
        items = new ArrayList<>();
        Random random = new Random();
        for (String s : data) {
            items.add(new WordCloud(s, random.nextInt(50)));
        }

        wordCloud.setDataSet(items);
        wordCloud.setSize(1000, 1000);

        wordCloud.setColors(ColorTemplate.MATERIAL_COLORS);
//       wordCloud.getSettings().setLoadWithOverviewMode(true);
//       wordCloud.getSettings().setUseWideViewPort(true);
//       wordCloud.getSettings().setDisplayZoomControls(false);
//       wordCloud.getSettings().setBuiltInZoomControls(true);

        wordCloud.notifyDataSetChanged();
      //  view.layout(0, 0, 900, 900);
        wordCloud.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //addView(wordCloud, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                wordCloud.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getYourLayout();
                        Log.d("wordcloude","callmethod");
                       // createBitmapFromView(wordCloud, wordCloud.getChildAt(0).getHeight(), wordCloud.getChildAt(0).getWidth());

                        //if (listener != null) listener.onComplete();
                    }
                }, 500);
            }
        });


    }

    private void getYourLayout() {

        try {
            Log.d("wordcloude","called");
            Dialog fb_event_info = new Dialog(MainActivity.this);
            fb_event_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
            fb_event_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            fb_event_info.setContentView(R.layout.activity_main);


            final LinearLayout lnr_fb_info = (LinearLayout) fb_event_info.findViewById(R.id.container);
            Bitmap bitmap = Bitmap.createBitmap(lnr_fb_info.getWidth(), lnr_fb_info.getHeight(), Bitmap.Config.ARGB_8888);


            lnr_fb_info.setDrawingCacheEnabled(true);

            lnr_fb_info.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            lnr_fb_info.layout(0, 0, lnr_fb_info.getMeasuredWidth(), lnr_fb_info.getMeasuredHeight());

            lnr_fb_info.buildDrawingCache(true);
            bitmap = Bitmap.createBitmap(lnr_fb_info.getDrawingCache());
            Log.d("worldCloude", "canvas created");

            try {
                FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/woldcloude.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                Toast.makeText(this, "WorldCloudSaved", Toast.LENGTH_SHORT).show();
                Log.d("worldCloude", "imagesaved");
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
        }
    }






//    public Bitmap viewToBitmap(View view) {
////        int width = view.getWidth();
////        int height = view.getHeight();
////
////        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
////        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
////
////        //Cause the view to re-layout
////        view.measure(measuredWidth, measuredHeight);
////        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
////
////        //Create a bitmap backed Canvas to draw the view into
////        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
////        Canvas c = new Canvas(b);
////
////        //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
////        view.draw(c);
////        Log.d("worldCloude","canvas created");
////        try{
////            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() +"/file.png");
////            b.compress(Bitmap.CompressFormat.PNG,100,output);
////            Toast.makeText(this, "WorldCloudSaved", Toast.LENGTH_SHORT).show();
////            Log.d("worldCloude","imagesaved");
////            output.close();
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return b;
//    }


}
