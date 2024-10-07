package com.vandyflintz.farmhousemovies;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.vandyflintz.farmhousemovies.adapter.CustomTrailerAdapter;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;
import static com.vandyflintz.farmhousemovies.Comments.uioptions;

public class News extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView listView;
    ImageLoader mImageLoader;
    RequestQueue requestQueue;
    AlertDialog alertDialog;
    MediaController mediaController;
    //VideoView videoView;
    TextView emptyText, starttime, endtime;
    SeekBar seekBar;
    Button exitbtn;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog pDialog;
    ScrollView scrollView;
    private List<Movie> movieList = new ArrayList<Movie>();
    private CustomTrailerAdapter adapter;
    private Handler videoHandler = new Handler();
    String url = "http://192.168.43.189/farmhousemovies/api/upcoming.php";
    public GestureDetector detector;
    public GestureDetector.SimpleOnGestureListener listener;
    GestureDetector gd;
    boolean playwhenready = true, exitdiag = false, resExists = false;
    int currentwindow = 0;
    long playbackposition = 0;
    RelativeLayout rl;
    PlayerView videoView;
    SimpleExoPlayer player;
    public boolean screenstate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);


        setContentView(R.layout.activity_news);

        emptyText = findViewById(R.id.emptytext);
        listView = findViewById(R.id.listnews);
        exitbtn = findViewById(R.id.exitbtn);
        swipeRefreshLayout = findViewById(R.id.swipenews);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = findViewById(R.id.sBar);
        adapter = new CustomTrailerAdapter(this, movieList);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        progressBar.setVisibility(View.VISIBLE);
        mImageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            final LruCache<String, Bitmap> mCache= new LruCache<String, Bitmap>(10);

            @Override
            public void putBitmap(String url, Bitmap bitmap){
                mCache.put(url, bitmap);
            }

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }


        });


        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(News.this, Home.class);
                startActivity(i);
            }
        });

        listView.setAdapter(adapter);
        emptyText.setVisibility(View.INVISIBLE);
        getTrailers();


    }

    public void getTrailers(){

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        if(response.length()>0){
                            progressBar.setVisibility(View.INVISIBLE);
                            emptyText.setVisibility(View.INVISIBLE);
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.setTitle(obj.getString("title"));
                                    movie.setThumbnailUrl(obj.getString("image"));
                                    movie.setFilename(obj.getString("moviedirectory"));
                                    movie.setDesc(obj.getString("description"));
                                    movie.setCast(obj.getString("cast"));
                                    movie.setPic(obj.getString("image"));
                                    movie.setDate(obj.getString("releasedate"));

                                    // adding movie to movies array
                                    movieList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{

                            progressBar.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            emptyText.setText("No information available");
                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(News.this, error.getMessage() , Toast.LENGTH_LONG).show();
                emptyText.setText("Error connecting to server, Swipe to refresh");
                hidePDialog();

            }
        });

        AppController.getInstance().addToRequestQueue(movieReq);
    }


    public void expandBtnListener(View v) {
        View parentRow = (View)v.getParent();
        // listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        View row = listView.getChildAt(position);

        TextView textView2 = ((TextView)row.findViewById(R.id.title));
        TextView textView3 = ((TextView)row.findViewById(R.id.mcast));
        TextView textView4 = ((TextView)row.findViewById(R.id.mpid));
        TextView textView5 = ((TextView)row.findViewById(R.id.mpath));
        TextView textView6 = ((TextView)row.findViewById(R.id.mdesc));
        TextView mpic = ((TextView)row.findViewById(R.id.mpic));
        TextView mdate = ((TextView)row.findViewById(R.id.mdate));


        String movietitle = textView2.getText().toString() ;
        String moviepath = textView5.getText().toString() ;
        String movieid = textView4.getText().toString() ;
        String desc = textView6.getText().toString() ;
        String cast = textView3.getText().toString() ;
        String thumbnail = mpic.getText().toString() ;
        String date = mdate.getText().toString() ;
        popdialog(movietitle,moviepath,movieid,desc,cast,thumbnail,date);



    }

    private void popdialog(String movietitle, String moviepath, String movieid, String desc,
                           String cast, String thumbnail, String date) {

        screenstate = true;

        AlertDialog.Builder adb = new AlertDialog.Builder(News.this,
                R.style.DialogTheme);


        View mydialog =
                getLayoutInflater().inflate(R.layout.news_row_two,
                        (ViewGroup) null);

        adb.setView(mydialog);



       // EditText comment = (EditText)mydialog.findViewById(R.id.comment);
        TextView title = mydialog.findViewById(R.id.title);
        TextView movieinfo = mydialog.findViewById(R.id.movieinfo);
        TextView casttext = mydialog.findViewById(R.id.casttext);
        TextView datetext = mydialog.findViewById(R.id.datetext);
        Button playpause = mydialog.findViewById(R.id.playpause);
        videoView = mydialog.findViewById(R.id.trailervid);
        NetworkImageView networkImageView = mydialog.findViewById(R.id.poster);
        TextView cancelbtn = mydialog.findViewById(R.id.closebtn);
        ImageView imageView = mydialog.findViewById(R.id.imgoverlay);
        starttime = mydialog.findViewById(R.id.starttime);
        endtime = mydialog.findViewById(R.id.endtime);
        seekBar = mydialog.findViewById(R.id.mseekbar);
        scrollView = mydialog.findViewById(R.id.scrollView);
        MediaController mediaController;
        rl = mydialog.findViewById(R.id.detailscon);



        /*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    videoView.seekTo(progress);
                    int position = videoView.getCurrentPosition();
                    starttime.setText("" + convertintotime(position));
                    endtime.setText("-" + convertintotime(player.getDuration() - position));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/



        LinearLayout linearLayout = mydialog.findViewById(R.id.seekcon);
        linearLayout.setVisibility(View.INVISIBLE);


        title.setText(movietitle);
        movieinfo.setText(desc);
        casttext.setText(cast);
        datetext.setText(date);

        //networkimageview.setImageUrl("", mimageloader);

        networkImageView.setImageUrl(thumbnail,
                mImageLoader);

        if(moviepath == null){
            videoView.setVisibility(View.GONE);
        }

        /*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setProgress(0);
                seekBar.setMax(player.getDuration());
                videoHandler.postDelayed(videoRunnable, 0);

            }
        });*/

        initmediaplayer(moviepath);


        if(imageView.getVisibility() == View.VISIBLE){

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    if (!player.isPlaying()) {
                        player.setPlayWhenReady(true);

                    } else {
                        //videoView.pause();
                        player.setPlayWhenReady(false);
                    }
                }
            });

        }

            videoView.getVideoSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                        if (!player.isPlaying()) {
                            player.setPlayWhenReady(true);
                            //videoView.start();
                            //videoHandler.postDelayed(videoRunnable, 0);
                        } else {
                            player.setPlayWhenReady(false);
                            //videoView.pause();
                        }

                }
            });

            videoView.getVideoSurfaceView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!player.isPlaying()) {
                        player.setPlayWhenReady(true);
                        //videoView.start();
                        //videoHandler.postDelayed(videoRunnable, 0);
                    } else {
                        player.setPlayWhenReady(false);
                        //videoView.pause();
                    }

                    return gd.onTouchEvent(event);
                }
            });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                releaseplayer();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //layoutParams.copyFrom(Objects.requireNonNull(alertDialog.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
        layoutParams.height = WindowManager.LayoutParams.FILL_PARENT;

        alertDialog = adb.create();

        alertDialog.getWindow().getDecorView().setSystemUiVisibility(uioptions);
        alertDialog.show();
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0,0);
        alertDialog.getWindow().setAttributes(layoutParams);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.setCanceledOnTouchOutside(false);

        gd = new GestureDetector(News.this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e){
                return true;
            }

            @Override
            public boolean onFling(MotionEvent event, MotionEvent e2, float vX, float vY){


                if(event.getAction()== MotionEvent.ACTION_DOWN ){
                    //   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    return true;
                }
                return super.onFling(event, e2, vX, vY);
            }


        });




        gd.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                //go fullscreen
                if(screenstate){
                    screenstate = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    rl.setVisibility(View.GONE);

                }else{
                    screenstate = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

                    final Handler scrollhandler = new Handler();
                    scrollhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0,scrollView.getBottom());
                        }
                    }, 500);
                    rl.setVisibility(View.VISIBLE);

                }
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        });


    }

        public Runnable videoRunnable = new Runnable() {
        @Override
        public void run() {

            long position = player.getCurrentPosition();
            if(seekBar != null) {
                seekBar.setProgress((int) player.getCurrentPosition());
            }


            if(player.isPlaying()){
                videoHandler.postDelayed(this, 100);
            }

            starttime.setText("" + convertintotime((int) position));
            endtime.setText("-" + convertintotime((int) (player.getDuration() - position)));
        }
    };

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public  void clearplayer() {
        releaseplayer();
    }

    private void releaseplayer(){
        if(player != null) {
            playwhenready = player.getPlayWhenReady();
            playbackposition = player.getContentPosition();
            currentwindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        releaseplayer();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        releaseplayer();
    }
    
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

    private void initmediaplayer(String videofilename) {


        if (player != null) {
            //player = null;
        } else {
            player = ExoPlayerFactory.newSimpleInstance(News.this);
        }

        videoView.setPlayer(player);
        //player.setPlayWhenReady(playwhenready);
        Uri uri = Uri.parse(videofilename);
        MediaSource mediaSource = buildMediaSource(uri);
       // player.seekTo(currentwindow, playbackposition);
        player.prepare(mediaSource, false, false);
        //player.addListener(eventListener);

    }


    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory datasourcefactory = new DefaultDataSourceFactory(News.this, "exoplayer" +
                "-codelab");
        return  new ProgressiveMediaSource.Factory(datasourcefactory).createMediaSource(uri);
    }

    @Override
    public void  onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hidenavigation(getWindow());
        }
    }

    @SuppressLint("PrivateApi")
    private void setFlagsOnThePeekView(){

    }

    private void hidenavigation(Window window) {
        int uioptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        window.getDecorView().setSystemUiVisibility(uioptions);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshmview();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    private void refreshmview() {
        progressBar.setVisibility(View.VISIBLE);
        movieList.clear();
        listView.invalidateViews();
        getTrailers();
    }
}
