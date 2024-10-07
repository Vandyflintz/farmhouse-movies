package com.vandyflintz.farmhousemovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity {
ImageView im1, im2;
ProgressBar progressBar;
TextView TimeField, ptitle, desc;
ViewFlipper imageFrame;
private Handler handler = new Handler();
Runnable runnable;
CountDownTimer countDownTimer;
    private static int SPLASHSCREEN_TIME_OUT = 4900;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);

        setContentView(R.layout.activity_main);
        imageFrame = findViewById(R.id.imgframe);
        im2 = findViewById(R.id.tvstand);
       im1 = findViewById(R.id.mainlogo);
       ptitle = findViewById(R.id.ptitle);
       desc = findViewById(R.id.desc);
      im1.bringToFront();
      im1.setTranslationZ(70);

        Animation fade = AnimationUtils.loadAnimation(this, R.anim.alpha);
        desc.startAnimation(fade);
        ptitle.startAnimation(fade);

        Animation fly = AnimationUtils.loadAnimation(this, R.anim.translate);
        im2.setAnimation(fly);
        im1.setAnimation(fly);
        imageFrame.setAnimation(fly);



      int slide_img[]={R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4};


      for(int i=0; i<slide_img.length;i++){
          setFlipperImage(slide_img[i]);
      }


       // im2.bringToFront();
        TimeField = findViewById(R.id.timetext);
        startprogress();
        progressBar.setVisibility(View.VISIBLE);

        final Handler handler1 = new Handler();

       final String[] msg = {"...","Ready","Steady","Go!"};

        handler1.postDelayed(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                TimeField.setText(msg[i]);
                i++;
                if(i == msg.length){
                    handler1.removeCallbacks(this);
                }else{
                    handler1.postDelayed(this, 1000);
                }
            }
        }, 1000);


        final Handler handler2 = new Handler();

        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                            //countDownTimer.cancel();
                            //TimeField.setText("Go!");

                Intent i = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(i);
                MainActivity.this.finish();

            }
        }, 3900);



    }

    private void setFlipperImage(int res) {
        ImageView image = new ImageView(this);
        image.setBackgroundResource(res);
        imageFrame.addView(image);
        imageFrame.setAutoStart(true);
        imageFrame.startFlipping();
        imageFrame.setFlipInterval(800);
    }

    private void startprogress() {
        this.progressBar = findViewById(R.id.pBar);
    }

    @Override
    protected void onResume(){
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        super.onResume();

    }
}
