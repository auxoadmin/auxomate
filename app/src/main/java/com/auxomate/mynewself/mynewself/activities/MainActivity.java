package com.auxomate.mynewself.mynewself.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    String wordCloudString = null;
    List<WordCloud> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WordCloudView wordCloud = findViewById(R.id.wordCloud);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main, null);

        wordCloudString = getIntent().getStringExtra("wordCloud");
        Log.d("incomingWordCloudString",wordCloudString);


        String[] data = wordCloudString.split("\\r?\\n");
        Log.d("wordcloudeSplit", Arrays.toString(data));
        items = new ArrayList<>();
        Random random = new Random();
        for (String s : data) {
            items.add(new WordCloud(s,random.nextInt(50)));
        }


        wordCloud.setDataSet(items);
        wordCloud.setSize(200,200);

        wordCloud.setColors(ColorTemplate.MATERIAL_COLORS);
        wordCloud.notifyDataSetChanged();
        view.layout(0, 0, 900, 900);



        wordCloud.post(new Runnable() {
            @Override
            public void run() {
                viewToBitmap(wordCloud);
            }
        });

    }

    public Bitmap viewToBitmap(View view){
        int width = view.getWidth();
        int height = view.getHeight();

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        //Cause the view to re-layout
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create a bitmap backed Canvas to draw the view into
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
        view.draw(c);
        Log.d("worldCloude","canvas created");
        try{
            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() +"/file.png");
            b.compress(Bitmap.CompressFormat.PNG,100,output);
            Toast.makeText(this, "WorldCloudSaved", Toast.LENGTH_SHORT).show();
            Log.d("worldCloude","imagesaved");
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
