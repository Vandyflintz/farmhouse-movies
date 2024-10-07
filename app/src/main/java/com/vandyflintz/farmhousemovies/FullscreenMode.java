package com.vandyflintz.farmhousemovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FullscreenMode extends AppCompatActivity {

    private static final String TAG = "";
    RelativeLayout relativeLayout;
    String url, title, playstate, videostate;
    VideoView videoView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    TextView mtitle, starttime, endtime;
    Button exitfullscreen, playpause;

    private Handler videoHandler = new Handler();
    int  movieposition;
    int curposition;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        setContentView(R.layout.activity_fullscreen_mode);

        relativeLayout = findViewById(R.id.conpanel);
        videoView = findViewById(R.id.fullsv);
        seekBar = findViewById(R.id.dseekbar);
        mtitle = findViewById(R.id.dtitle);
        starttime = findViewById(R.id.dstarttime);
        endtime = findViewById(R.id.dendtime);
        exitfullscreen = findViewById(R.id.dmagbtn);
        playpause = findViewById(R.id.playpause);

        relativeLayout.setVisibility(View.INVISIBLE);

        SharedPreferences sp = getSharedPreferences("MovieDetails", Context.MODE_PRIVATE);
        title = (sp.getString("MovieTitle", ""));

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("movieurl");
        //title = bundle.getString("movietitle");
        movieposition = bundle.getInt("movieposition");
        playstate = bundle.getString("moviestate");

        mtitle.setText(title);


        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        android.widget.RelativeLayout.LayoutParams params =
                (android.widget.RelativeLayout.LayoutParams)videoView.getLayoutParams();
        params.width =displayMetrics.widthPixels;
        params.height =displayMetrics.heightPixels;
        params.leftMargin =0;
        params.rightMargin=0;
        params.topMargin=0;
        params.bottomMargin=0;
        videoView.setLayoutParams(params);

        videoView.seekTo(movieposition);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = videoView.getDuration();
                seekBar.setProgress(0);
                seekBar.setMax(videoView.getDuration());
                videoHandler.postDelayed(videoRunnable, 0);
                videoView.start();
            }
        });




       // Toast.makeText(FullscreenMode.this, String.valueOf(duration), Toast.LENGTH_LONG).show();
        mediaController = new MediaController(this);
        mediaController.setMediaPlayer(videoView);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        mediaController.setVisibility(View.GONE);

         curposition = videoView.getCurrentPosition();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(FullscreenMode.this, "Shown", Toast.LENGTH_LONG).show();
                if(relativeLayout.getVisibility() == View.VISIBLE) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                }else{
                    relativeLayout.setVisibility(View.VISIBLE);
                    relativeLayout.bringToFront();
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videostate = "ended";
            }
        });

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    videoView.pause();
                    playpause.setBackgroundResource(R.drawable.pause_btn);
                }else{
                    videoView.start();
                    playpause.setBackgroundResource(R.drawable.play_btn);
                }
            }
        });

        exitfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                Intent intent = new Intent();
                intent.putExtra("movpos", videoView.getCurrentPosition());
                intent.putExtra("state", videostate);
                intent.putExtra("duration", videoView.getDuration());
                intent.setClassName(getBaseContext(), "com.vandyflintz.farmhousemovies.Home");

                Intent output = new Intent();
                output.putExtra("number", 1);
                setResult(Activity.RESULT_OK, output);
                FullscreenMode.this.finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    videoView.seekTo(progress);
                    int position = videoView.getCurrentPosition();
                    starttime.setText("" + convertintotime(position));
                    endtime.setText("-" + convertintotime(videoView.getDuration() - position));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //int progress = seekBar.getProgress();
                //mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //setHandler();
        //initializeseekbar();

    }




   private Runnable videoRunnable = new Runnable() {
        @Override
        public void run() {

                long position = videoView.getCurrentPosition();
                if(seekBar != null) {
                    seekBar.setProgress(videoView.getCurrentPosition());
                }

                if(videoView.isPlaying()){
                    videoHandler.postDelayed(this, 100);
                }

                starttime.setText("" + convertintotime((int) position));
                endtime.setText("-" + convertintotime((int) (videoView.getDuration() - position)));


        }
    };



    private String convertintotime(int ms) {
        String time;
        int x,seconds,minutes,hours;
        x = (int) (ms/1000);
        seconds = x % 60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x % 24;

        if(hours != 0){
            time =
                    String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }else{
            time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }
        return time;
    }






    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
}
