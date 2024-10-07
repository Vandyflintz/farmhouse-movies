package com.vandyflintz.farmhousemovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.vandyflintz.farmhousemovies.adapter.CustomFavouritesAdapter;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.card.CardFragment;
import com.vandyflintz.farmhousemovies.card.CardPresenter;
import com.vandyflintz.farmhousemovies.data.SavedCard;
import com.vandyflintz.farmhousemovies.data.SavedPhone;
import com.vandyflintz.farmhousemovies.ghmobilemoney.GhMobileMoneyFragment;
import com.vandyflintz.farmhousemovies.ghmobilemoney.GhMobileMoneyPresenter;
import com.vandyflintz.farmhousemovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavouritesNew.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavouritesNew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesNew extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FavouritesNew() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesNew.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesNew newInstance(String param1, String param2) {
        FavouritesNew fragment = new FavouritesNew();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
           timehandler.removeCallbacks(updatelistview);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(videoname != null) {
            initmediaplayer(videoname);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        releaseplayer();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ListView listView;
    SharedPreferences sp,fsp, sharedPreferences;
    String videodir,vidname,videoname,part,userid;
    private CustomFavouritesAdapter adapter;
    private List<Movie> movieList = new ArrayList<Movie>();
    String url;
    ProgressDialog pDialog;
    TextView emptyText, starttime, endtime, dstarttime, dendtime, titletext, mtitle;
    ImageView imageView, exitimg;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout, relativeLayoutone;
    Button mainplay, mainpause, mainstop,mainmagnify, demagnify, playpause;
    SeekBar seekBar, dseekBar;
    public boolean screenstate;
    public boolean pausing = false, stopped = true;
    private Handler videoHandler = new Handler(), viewHandler = new Handler();
    Runnable viewRunnable;
    MediaController mediaController;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    RelativeLayout gridLayout;
    GridLayout gridLayoutone;
    String partid, meprice,sname;
    String textquery, subscription, request, currentdate, servermsg;
    AlertDialog alertDialog;
    RelativeLayout relativeLayouttwo;
    TextView amount, message, movietxt, hidspin, pnote, vul;
    Button submit, closebtn, retrybtn;
    EditText num, voucher;
    Spinner wallets;
    String[] arrayspMoMo;
    ProgressBar progressBarone;
    String amt, walletnum, vouchernum, phonenum, status, expidate;
    public static final int CONNECTION_TIMEOUT_ONE=50000;
    public static final int READ_TIMEOUT_ONE=55000;
    public static int vpos, index;
    String movietype, movieid, sid;
    Handler timehandler = new Handler();
    int curpage, remaining;
    Button loadmore, refreshbtn;
    View footerview;
    ProgressBar progressBar;
    int pos;
    PlayerView videoView;
    SimpleExoPlayer player;
    PlayerControlView controlView, playView, fullcontrolView;
    boolean playwhenready = true, exitdiag = false, resExists = false;
    int currentwindow = 0;
    long playbackposition = 0;
    private Runnable videoRunnable;
    Player.EventListener eventListener;
    GhMobileMoneyPresenter ghMobileMoneyPresenter;
    CardPresenter cardPresenter;
    ProgressDialog progressDialog;
    EditText emailEt;
    EditText amountEt;
    EditText apiKeyEt;
    EditText txRefEt;
    EditText narrationEt;
    EditText currencyEt;
    EditText merchantIdEt;
    EditText apiUserEt;
    EditText dUrlEt;
    EditText fNameEt;
    EditText lNameEt;
    Button startPayBtn;
    SwitchCompat cardSwitch;
    SwitchCompat ghMobileMoneySwitch;
    SwitchCompat isLiveSwitch;
    List<Meta> meta = new ArrayList<>();
    String finalcode, pstatus;
    public boolean detailsexist = false;
    public boolean movetonextscreen = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites_new, container, false);

        listView = v.findViewById(R.id.listmoviesparts);
        imageView = v.findViewById(R.id.imgoverlay);
        videoView= v.findViewById(R.id.sv);
        linearLayout = v.findViewById(R.id.seekcon);
        relativeLayout = v.findViewById(R.id.controlscon);
        relativeLayoutone = v.findViewById(R.id.conpanel);
        seekBar = v.findViewById(R.id.mseekbar);
        mainplay = v.findViewById(R.id.playbtn);
        progressBar = v.findViewById(R.id.sBar);
        refreshbtn = v.findViewById(R.id.refreshbtn);
        mainpause =  v.findViewById(R.id.pausebtn);
        mainstop = v.findViewById(R.id.stopbtn);
        mainmagnify = v.findViewById(R.id.btnmagnify);
        titletext = v.findViewById(R.id.movietitle);
        starttime = v.findViewById(R.id.starttime);
        endtime = v.findViewById(R.id.endtime);
        videoView = v.findViewById(R.id.sv);
        playView = v.findViewById(R.id.ppcv);
        dseekBar = v.findViewById(R.id.dseekbar);
        mtitle = v.findViewById(R.id.dtitle);
        dstarttime = v.findViewById(R.id.dstarttime);
        dendtime = v.findViewById(R.id.dendtime);
        demagnify = v.findViewById(R.id.dmagbtn);
        playpause = v.findViewById(R.id.playpause);
        emptyText = v.findViewById(R.id.emptytext);
        gridLayoutone = v.findViewById(R.id.title);
        linearLayout.setVisibility(View.INVISIBLE);
        relativeLayoutone.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
        emptyText.setVisibility(View.INVISIBLE);
        refreshbtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);
        footerview =
                ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        loadmore = footerview.findViewById(R.id.btnloadmore);

        controlView = v.findViewById(R.id.pcv);
        controlView.setVisibility(View.INVISIBLE);
        fullcontrolView = v.findViewById(R.id.fullpcv);

        listView.addFooterView(footerview);
        footerview.setVisibility(View.GONE);
        loadmore.setVisibility(View.GONE);
        refreshbtn.setVisibility(View.GONE);
        sp = getActivity().getSharedPreferences("MovieDetails", Context.MODE_PRIVATE);
        videodir = (sp.getString("MoviePath", ""));
        vidname = (sp.getString("MovieTitle", ""));

        fsp = getActivity().getSharedPreferences("MoviePartDetails", Context.MODE_PRIVATE);

        //fav = (fsp.getString("Favstatus", ""));
        part = (fsp.getString("MovieID",""));
        sharedPreferences = getActivity().getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));
        adapter = new CustomFavouritesAdapter(getActivity(), movieList, this);
        listView.setAdapter(adapter);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() - listView.getFooterViewsCount())>= (adapter.getCount()- 1)){
                    //Toast.makeText(getActivity(), "Bottom has been reached", Toast.LENGTH_LONG)
                    // .show();
                    if(remaining != 0){
                        footerview.setVisibility(View.VISIBLE);
                        loadmore.setVisibility(View.VISIBLE);
                    }else{
                        footerview.setVisibility(View.GONE);
                        loadmore.setVisibility(View.GONE);
                    }

                }



            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footerview.setVisibility(View.GONE);
                loadmore.setVisibility(View.GONE);

                curpage += 1;
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)progressBar.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                progressBar.setLayoutParams(layoutParams);
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    fetchfavourites(curpage, "tv","fetch");
                }else{
                    fetchfavourites(curpage, "other", "fetch");
                }
            }
        });

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
                refreshbtn.setVisibility(View.GONE);

                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    fetchfavourites(curpage, "tv","fetch");
                }else{
                    fetchfavourites(curpage, "other", "fetch");
                }
            }
        });


        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpause.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                if(player.isPlaying()){
                    player.setPlayWhenReady(false);
                    playpause.setBackgroundResource(R.drawable.play_btn);
                }else{
                    player.setPlayWhenReady(true);
                    playpause.setBackgroundResource(R.drawable.pause_btn);
                }
            }
        });

        ((UserProfile)getActivity()).setFragmentRefreshListener(new UserProfile.FragmentRefreshListener() {
            @Override
            public void onRefresh(String sentcode) {
                onresultreceived(sentcode);
            }
        });

        eventListener = new Player.EventListener() {


            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


                if(playbackState == Player.STATE_READY){
                        if(!expidate.equalsIgnoreCase("N/A")) {
                            calculatedate(expidate);
                        }
                    timehandler.postDelayed(updatelistview,3000);

                }
                if(playbackState == ExoPlayer.STATE_ENDED){

                    timehandler.removeCallbacks(updatelistview);
                    playpause.setBackgroundResource(R.drawable.play_btn);

                    if(screenstate) {
                        relativeLayoutone.setVisibility(View.VISIBLE);
                        fullcontrolView.show();
                    }
                }
            }


        };

        videoView.getVideoSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(screenstate) {
                    if (relativeLayoutone.getVisibility() == View.VISIBLE) {
                        relativeLayoutone.setVisibility(View.INVISIBLE);

                    } else {
                        relativeLayoutone.setVisibility(View.VISIBLE);
                        relativeLayoutone.bringToFront();

                        viewHandler.postDelayed(viewRunnable, 2000);
                    }
                }else{
                    if(!player.isPlaying() && player != null) {
                        player.setPlayWhenReady(true);
                    }else if(player.isPlaying()){
                        player.setPlayWhenReady(false);
                    }
                }
            }
        });



        mainplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mainplay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                pausing = false;

                if(!player.isPlaying()){
                    player.setPlayWhenReady(true);
                    imageView.setVisibility(View.INVISIBLE);

                }
            }
        });

        mainmagnify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mainmagnify.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                screenstate = true;
                //titletext.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                //gridLayout.setVisibility(View.GONE);
                videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                videoView.setShowMultiWindowTimeBar(true);
                controlView.setVisibility(View.GONE);
                controlView.setPlayer(null);
                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ((UserProfile)getActivity()).hideTabLayout();

                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                params.leftMargin =0;
                params.rightMargin=0;
                params.topMargin=0;
                params.bottomMargin=0;

                videoView.setTranslationZ(50);
                videoView.bringToFront();

                relativeLayoutone.setLayoutParams(params);
                videoView.setLayoutParams(params);

            }
        });


        demagnify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Favourites.this, vidname, Toast.LENGTH_LONG).show();
                demagnify.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                screenstate = false;
                relativeLayoutone.setVisibility(View.INVISIBLE);
                titletext.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                relativeLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                //gridLayout.setVisibility(View.VISIBLE);
                controlView.setVisibility(View.VISIBLE);
                controlView.setPlayer(player);
               (v.findViewById(R.id.movietitle)).setVisibility(View.VISIBLE);
                (v.findViewById(R.id.seekcon)).setVisibility(View.VISIBLE);
                (v.findViewById(R.id.controlscon)).setVisibility(View.VISIBLE);
                //titletext.setText(vidname);
                ((UserProfile)getActivity()).showTabLayout();
                RelativeLayout.LayoutParams params2 =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                android.widget.RelativeLayout.LayoutParams params =
                        (android.widget.RelativeLayout.LayoutParams)videoView.getLayoutParams();
                params.width = displayMetrics.widthPixels;
                params.height =(int)(160*displayMetrics.density);
                params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                int margins = dp(10);
                int margin = dp(58);
                params.bottomMargin = margins;
                params.leftMargin =margins;
                params.rightMargin=margins;
                params.topMargin=dp(30);
                params2.topMargin = margin;
                videoView.setTranslationZ(0);
                videoView.setLayoutParams(params);
            }
        });





        curpage = 1;

        url ="http://192.168.43.189/farmhousemovies/api/getfavourites.php?user="+userid+"&request=fetch";


        if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                ".android.tv")){
            fetchfavourites(curpage, "tv", "fetch");
        }else{
            fetchfavourites(curpage, "other", "fetch");
        }

        return v;
    }







    private void fetchfavourites(int pagenum, String device, String request) {

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url+"&page="+pagenum+"&device="+device+
                "&rtype=normal",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        if(response.length()>0){

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject objcount = response.getJSONObject(0);
                                    JSONObject obj = response.getJSONObject(i);
                                    Movie movie = new Movie();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    movie.setTitle(obj.getString("title"));
                                    movie.setThumbnailUrl(obj.getString("image"));
                                    movie.setDuration(obj.getString("movieDuration"));
                                    movie.setFilename(obj.getString("movieFile"));
                                    movie.setID(obj.getString("moviedirectory"));
                                    movie.setDesc(obj.getString("description"));
                                    movie.setDate(obj.getString("releasedate"));
                                    movie.setpartID(obj.getString("partid"));
                                    movie.setExpirydate(obj.getString("expirydate"));
                                    movie.setMovieID(obj.getString("movieID"));
                                    movie.setSeriesID(obj.getString("seasonID"));
                                    movie.setCategory(obj.getString("category"));
                                    movie.setPrice(obj.getString("price"));
                                    movie.setPaidstatus(obj.getString("paidstatus"));
                                    movie.setMovietype(obj.getString("movietype"));

                                    remaining = objcount.getInt("remainingitems");
                                    curpage = Integer.parseInt(objcount.getString("pagenum"));
                                    // adding movie to movies array
                                    movieList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            linearLayout.setVisibility(View.INVISIBLE);
                            relativeLayoutone.setVisibility(View.INVISIBLE);
                            relativeLayout.setVisibility(View.INVISIBLE);
                            videoView.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            emptyText.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
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
                final Handler searchhandler = new Handler();
                searchhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyText.setVisibility(View.VISIBLE);
                        emptyText.setText("Oops, error encountered: Retry");
                        refreshbtn.setVisibility(View.VISIBLE);

                    }
                }, 500);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public  static  int dp(int px){
        return (int)(px * Resources.getSystem().getDisplayMetrics().density);
    }

    public void startmonitoring(){
        timehandler.postDelayed(updatelistview,5000);
        resExists = true;
    }

    private Runnable updatelistview = new Runnable() {
        @Override
        public void run() {
            if(!expidate.equalsIgnoreCase("N/A")) {
                calculatedate(expidate);
            }
            timehandler.postDelayed(this,5000);
        }
    };

    private void calculatedate(String expidate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dbdate = null;
        Date fdate = null;
        try {
            dbdate = simpleDateFormat.format(simpleDateFormat.parse(expidate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date pdate = Calendar.getInstance().getTime();
        try {
            fdate = simpleDateFormat.parse(dbdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = fdate.getTime() - pdate.getTime();
        long mins = TimeUnit.MILLISECONDS.toMinutes(millis);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {



                //Toast.makeText(Home.this, String.valueOf(mins), Toast.LENGTH_LONG)
                // .show();
                if(mins < 1){
                    timehandler.removeCallbacks(updatelistview);
                    getrefreshedrecords();
                   /* if(frag != null){
                        refreshfragsetfav(frag,listposition);
                    }*/
                    if(player != null){
                       player.setPlayWhenReady(false);
                    }

                    resExists = false;
                    getpaymentdetails(movietype, movieid, sid, partid, meprice, sname, pos);



                }
            }
        });
    }

    public void getpaymentdetails(String mtype, String mid, String serid, String pid,
                                  String mprice, String stitle, int position){

        if(mtype.equalsIgnoreCase("Single")){
            showpaymentdialog(mtype, mid, "", pid, userid,mprice,stitle,position);
        }else if(mtype.equalsIgnoreCase("Season")){
            showpaymentdialog(mtype, mid, serid, pid, userid,mprice,stitle, position);
        }else if(mtype.equalsIgnoreCase("Series")){
            showpaymentdialog(mtype, mid, serid, pid, userid,mprice,stitle, position);
        }
    }

    public void playvideo(String moviepath, String movietitle, String mtype, String mid,
                          String mseasonid, String mpartid, String mprice, String expirydate, int position){
        meprice = mprice;
        partid = mpartid;
        sname = movietitle;
        videoname = moviepath;
        movietype = mtype;
        movieid = mid;
        sid = mseasonid;
        pos = position;
        expidate = expirydate;
        titletext.setVisibility(View.VISIBLE);
        titletext.setText(movietitle);
        mtitle.setText(movietitle);
        imageView.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        initmediaplayer(videoname);
    }

    public void removeVideo(String movieid, String seasonid, String mid , String movietype){
        new AsyncSetFav().execute(movieid,userid,movietype,seasonid, mid);
    }





    private void saveDialogDetails(String movietype, String movieid, String sid, String partid,
                                   String userid, String mPrice, String mName){
        new PreferencesManager(getActivity()).saveAlertDetails(movietype,movieid,sid,partid,
                userid,mPrice,mName);
    }

    private void showpaymentdialog(String movietype, String movieid, String sid, String partid,
                                   String userid, String mPrice, String mName, int position) {

        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        saveDialogDetails(movietype, movieid, sid,partid,userid,mPrice,mName);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        View mydialog =
                getLayoutInflater().inflate(R.layout.paymentdiag,
                        (ViewGroup) null);
        adb.setView(mydialog);
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                alertDialog.cancel();
            }
        });
        View titleview =
                ((LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.texthead,null,false);
        adb.setCustomTitle(titleview);





        relativeLayout = mydialog.findViewById(R.id.rl);
        gridLayout = mydialog.findViewById(R.id.gl);
        amount = mydialog.findViewById(R.id.mcost);
        message = mydialog.findViewById(R.id.pmsg);
        movietxt = mydialog.findViewById(R.id.dmtitle);
        submit = mydialog.findViewById(R.id.sendbtn);
        closebtn = mydialog.findViewById(R.id.donebtn);
        voucher = mydialog.findViewById(R.id.etVoucher);
        num = mydialog.findViewById(R.id.phonenum);
        wallets = mydialog.findViewById(R.id.spinMoMo);
        hidspin = mydialog.findViewById(R.id.hidspin);
        progressBarone = mydialog.findViewById(R.id.sBar);
        retrybtn = mydialog.findViewById(R.id.retrybtn);
        pnote = mydialog.findViewById(R.id.pnote);
        vul = mydialog.findViewById(R.id.txtvoucher);
        TextView headmsg = mydialog.findViewById(R.id.txtreason);
        if(resExists) {
            headmsg.setVisibility(View.VISIBLE);
            headmsg.setText("Your subscription has expired, please renew it to continue");
        }
        retrybtn.setVisibility(View.GONE);
        voucher.setVisibility(View.GONE);
        vul.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.GONE);

        final Handler alerthandler = new Handler();
        alerthandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(detailsexist){
                    pnote.append(servermsg);
                }
                if(movetonextscreen){
                    pstatus = "success";
                    message.setText("Payment Successful!");
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    closebtn.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.INVISIBLE);
                }
            }
        }, 500);

        pnote.setText("NB: Subscription is valid for 7 days");

        this.arrayspMoMo = new String[]{"-- Select Wallet --","mtn","airteltigo","vodafone"};

        ArrayAdapter<String> adapterNCD = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arrayspMoMo);
        wallets.setAdapter(adapterNCD);

        setupSpinners();

        Calendar cal = Calendar.getInstance();
        String dayofmonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(Calendar.MONTH));
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String min = String.valueOf(cal.get(Calendar.MINUTE));
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String secs = String.valueOf(cal.get(Calendar.SECOND));

        if(month.length()<2){
            month = "0"+month;
        }

        if(min.length()<2){
            min = "0"+min;
        }

        if(hour.length()<2){
            hour = "0"+hour;
        }

        if(secs.length()<2){
            secs = "0"+secs;
        }

        String transid = shufflestring(month+year+min+hour+secs);

        amount.setText(mPrice+" p");
        movietxt.setText(mName);
        emailEt = mydialog.findViewById(R.id.emailEt);
        amountEt = mydialog.findViewById(R.id.amountEt);
        if(!(mPrice.contains("."))){
            amount.setText(mPrice+".00 p");
            amountEt.setText(mPrice+".00");
        }else{
            amount.setText(mPrice+" p");
            amountEt.setText(mPrice);
        }

        apiKeyEt = mydialog.findViewById(R.id.apiKeyEt);
        txRefEt = mydialog.findViewById(R.id.txRefEt);
        txRefEt.setText(transid);
        merchantIdEt = mydialog.findViewById(R.id.merchant_idEt);
        merchantIdEt.setText("TTM-00004144");
        apiUserEt = mydialog.findViewById(R.id.api_userEt);
        apiUserEt.setText("farmhouse5f32b9f237fe6");
        dUrlEt = mydialog.findViewById(R.id.d_urlEt);
        dUrlEt.setText("http://192.168.43.189/farmhousemovies/api/");
        narrationEt = mydialog.findViewById(R.id.narrationTV);
        currencyEt = mydialog.findViewById(R.id.currencyEt);
        fNameEt = mydialog.findViewById(R.id.fNameEt);
        lNameEt = mydialog.findViewById(R.id.lnameEt);
//        voucherCode = findViewById(R.id.voucherCodeEt);
        startPayBtn = mydialog.findViewById(R.id.startPaymentBtn);
        cardSwitch = mydialog.findViewById(R.id.cardPaymentSwitch);
        ghMobileMoneySwitch = mydialog.findViewById(R.id.accountGHMobileMoneySwitch);
        isLiveSwitch = mydialog.findViewById(R.id.isLiveSwitch);

        apiKeyEt.setText(thetellerConstants.API_KEY);

        meta.add(new Meta("test key 1", "test value 1"));
        meta.add(new Meta("test key 2", "test value 2"));

        startPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressIndicator(true);
                validateEntries();
                showProgressIndicator(false);

            }

        });


        voucher.setVisibility(View.INVISIBLE);


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                alertDialog.dismiss();
                detailsexist = false;
                movetonextscreen = false;
                SharedPreferences nsp = getActivity().getSharedPreferences("AlertDialogDetails",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor neditor = nsp.edit();
                neditor.clear();
                neditor.apply();


                if(pstatus=="success") {
                    index = listView.getFirstVisiblePosition();
                    View v = listView.getChildAt(0);
                    vpos = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
                    int totalitems = (listView.getAdapter().getCount()) - 1;
                    getrefreshedrecords();
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amt = amount.getText().toString();
                walletnum = wallets.getSelectedItem().toString();
                vouchernum = voucher.getText().toString();
                phonenum = num.getText().toString();

                if(walletnum.length()==0 || walletnum.equalsIgnoreCase("-- Select Wallet --")){
                    hidspin.setError("Please select your vendor");
                }else if(phonenum.length() == 0 || phonenum.length() < 10){
                    num.setError("Enter a valid number");
                }else if(walletnum.equalsIgnoreCase("Vodafone") && vouchernum.equalsIgnoreCase("")){
                    voucher.setError("Voucher cannot be empty!");
                }else{
                    relativeLayout.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.GONE);
                    closebtn.setVisibility(View.GONE);
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                    new AsyncPay().execute(phonenum,walletnum,amt,movietype,movieid,partid, sid,
                            userid);
                }
            }
        });


        retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                gridLayout.setVisibility(View.VISIBLE);


            }
        });


        adb.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                detailsexist = false;
                movetonextscreen = false;
                SharedPreferences nsp = getActivity().getSharedPreferences("AlertDialogDetails",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor neditor = nsp.edit();
                neditor.clear();
                neditor.apply();
            }
        });

        alertDialog = adb.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"));
            }
        });



        alertDialog.setCanceledOnTouchOutside(false);
        int ui_flags =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        alertDialog.getWindow().getDecorView().setSystemUiVisibility(ui_flags);
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(layoutParams);


    }

    private void clearErrors() {
        emailEt.setError(null);
        amountEt.setError(null);
        apiKeyEt.setError(null);
        txRefEt.setError(null);
        merchantIdEt.setError(null);
        apiUserEt.setError(null);
        dUrlEt.setError(null);
        narrationEt.setError(null);
        currencyEt.setError(null);
        fNameEt.setError(null);
        lNameEt.setError(null);
    }

    public void showProgressIndicator(boolean active) {

        // if (this.isFinishing()) { return; }
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
        }

        if (active && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        else {
            progressDialog.dismiss();
        }
    }

    private void validateEntries() {

        clearErrors();
        String email = emailEt.getText().toString();
        String amount = amountEt.getText().toString();
        String apiKey = apiKeyEt.getText().toString();
        String txRef = txRefEt.getText().toString();
        String merchantId = merchantIdEt.getText().toString();
        String apiUser = apiUserEt.getText().toString();
        String dUrl = dUrlEt.getText().toString();
        String narration = narrationEt.getText().toString();
        String currency = currencyEt.getText().toString();
        String fName = fNameEt.getText().toString();
        String lName = lNameEt.getText().toString();



        boolean valid = true;

        if (amount.length() == 0) {
            amount = "0";
        }

        //check for compulsory fields
        if (amount.length() < 1){
            valid = false;
            amountEt.setError("A valid amount is required");
        }


        if (!Utils.isEmailValid(email)) {
            valid = false;
            emailEt.setError("A valid email is required");
        }

        if (apiKey.length() < 1){
            valid = false;
            apiKeyEt.setError("A valid public key is required");
        }
        if (txRef.length() < 1){
            valid = false;
            txRefEt.setError("A valid transaction ID is required");
        }

        if (merchantId.length() < 1){
            valid = false;
            merchantIdEt.setError("A valid merchant ID is required");
        }

        if (apiUser.length() < 1){
            valid = false;
            apiUserEt.setError("A valid Api Username is required");
        }
        if (dUrl.length() < 1){
            valid = false;
            dUrlEt.setError("A valid url for 3D response is required");
        }
        if (apiKey.length() < 1){
            valid = false;
            apiKeyEt.setError("A valid public key is required");
        }
        if (narration.length() < 1){
            valid = false;
            narrationEt.setError("A valid description is required");
        }

        if (currency.length() < 1){
            valid = false;
            currencyEt.setError("A valid currency code is required");
        }
        if (fName.length() < 1){
            valid = false;
            fNameEt.setError("A valid first name is required");
        }
        if (lName.length() < 1){
            valid = false;
            lNameEt.setError("A valid last name is required");
        }

        if (valid) {

            ghMobileMoneyPresenter = new GhMobileMoneyPresenter(getActivity(), new GhMobileMoneyFragment());

            cardPresenter = new CardPresenter(getActivity(), new CardFragment());
            List<SavedPhone> checkForSavedMobileMoney = ghMobileMoneyPresenter.checkForSavedGHMobileMoney(email);
            List<SavedCard> checkForSavedCards = cardPresenter.checkForSavedCards(email);

            if (checkForSavedCards.isEmpty() && checkForSavedMobileMoney.isEmpty()){
                new thetellerManager(getActivity()).setAmount(Double.parseDouble(amount))
                        .setEmail(email)
                        .setfName(fName)
                        .setlName(lName)
                        .setMerchant_id(merchantId)
                        .setNarration(narration)
                        .setApiUser(apiUser)
                        .setApiKey(apiKey)
                        .setTxRef(txRef)
                        .set3dUrl(dUrl)
                        .acceptCardPayments(cardSwitch.isChecked())
                        .acceptGHMobileMoneyPayments(ghMobileMoneySwitch.isChecked())
                        .onStagingEnv(!isLiveSwitch.isChecked())
                        .setActivityName("MostViewed")
                        .initialize();
            }else {
                new thetellerManager(getActivity()).setAmount(Double.parseDouble(amount))
                        .setEmail(email)
                        .setfName(fName)
                        .setlName(lName)
                        .setMerchant_id(merchantId)
                        .setNarration(narration)
                        .setApiUser(apiUser)
                        .setApiKey(apiKey)
                        .setTxRef(txRef)
                        .set3dUrl(dUrl)
                        .acceptCardPayments(cardSwitch.isChecked())
                        .acceptGHMobileMoneyPayments(ghMobileMoneySwitch.isChecked())
                        .onStagingEnv(!isLiveSwitch.isChecked())
                        .setActivityName("MostViewed")
                        .chooseCardOrNumber();
            }
        }
    }


    public void onresultreceived(String result){
        //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        String finalresponse = result;

        finalcode = result.replace("{", "").replace("}", "").replace("\"", "");

        String[] strarray = finalcode.split(",");
        String scode = strarray[1];
        String[] farray = scode.split(":");
        String fcode = farray[1];
        //Toast.makeText(getActivity(), fcode, Toast.LENGTH_LONG).show();

        if(Integer.parseInt(fcode) != 000){
            //Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
            if(Integer.parseInt(fcode) == 100){
                servermsg = "\n"+"\n"+"Transaction failed, check your balance and " +
                        "authorization " +
                        "and " +
                        "try again!";
            }else{
                servermsg = "\n"+"\n" + "Transaction failed, operation was aborted!";
            }
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
            }
            detailsexist = true;
            if(alertDialog.isShowing()){
                alertDialog.dismiss();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AlertDialogDetails",
                        Context.MODE_PRIVATE);
                String  mtype = (sharedPreferences.getString("Mtype",""));
                String  mid = (sharedPreferences.getString("Mid",""));
                String  sid = (sharedPreferences.getString("Sid",""));
                String  partid = (sharedPreferences.getString("Partid",""));
                String  mprice = (sharedPreferences.getString("Mprice",""));
                String  mname = (sharedPreferences.getString("Mname",""));


                new AsyncPay().execute(mtype,mid,partid, sid, userid,"failedpayment");

                int position = 0;

                showpaymentdialog(mtype,mid,sid,partid,userid,mprice,mname, position);
            }
        }

        if(Integer.parseInt(fcode) == 000){
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
            }

            movetonextscreen = true;
            if(alertDialog.isShowing()){
                alertDialog.dismiss();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AlertDialogDetails",
                        Context.MODE_PRIVATE);
                String  mtype = (sharedPreferences.getString("Mtype",""));
                String  mid = (sharedPreferences.getString("Mid",""));
                String  sid = (sharedPreferences.getString("Sid",""));
                String  partid = (sharedPreferences.getString("Partid",""));
                String  mprice = (sharedPreferences.getString("Mprice",""));
                String  mname = (sharedPreferences.getString("Mname",""));

                int position = 0;
                showpaymentdialog(mtype,mid,sid,partid,userid,mprice,mname, position);
                new AsyncPay().execute(mtype,mid,partid, sid, userid,"successfulpayment");
            }
        }

    }

    public String shufflestring(String str){
        List <String> letters = Arrays.asList(str.split(""));
        Collections.shuffle(letters);
        StringBuilder stringBuilder = new StringBuilder();
        for(String letter: letters){
            stringBuilder.append(letter);
        }
        return stringBuilder.toString();
    }

    private void getrefreshedrecords() {
        index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        vpos = (v== null) ? 0 : (v.getTop() - listView.getPaddingTop());
        //vpos = listposition;
        int totalitems = (listView.getAdapter().getCount()) - 1;
        fetchnewrecords(totalitems);
       // Toast.makeText(getActivity(), String.valueOf(totalitems), Toast.LENGTH_LONG).show();
    }

    public  void scrolltoitem() {
        listView.setSelectionFromTop(index, vpos);
    }

    private void fetchnewrecords(int totalitems) {

        movieList.clear();
        listView.invalidateViews();

        JsonArrayRequest movieReq = new JsonArrayRequest("http://192.168.43.189/farmhousemovies/api/getfavourites.php?user="+userid+"&request=fetch"+"&items="+totalitems+
                "&rtype=refresh",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        if(response.length()>0){

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Movie movie = new Movie();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    movie.setTitle(obj.getString("title"));
                                    movie.setThumbnailUrl(obj.getString("image"));
                                    movie.setDuration(obj.getString("movieDuration"));
                                    movie.setFilename(obj.getString("movieFile"));
                                    movie.setID(obj.getString("moviedirectory"));
                                    movie.setDesc(obj.getString("description"));
                                    movie.setDate(obj.getString("releasedate"));
                                    movie.setpartID(obj.getString("partid"));
                                    movie.setExpirydate(obj.getString("expirydate"));
                                    movie.setMovieID(obj.getString("movieID"));
                                    movie.setSeriesID(obj.getString("seasonID"));
                                    movie.setCategory(obj.getString("category"));
                                    movie.setPrice(obj.getString("price"));
                                    movie.setPaidstatus(obj.getString("paidstatus"));
                                    movie.setMovietype(obj.getString("movietype"));

                                    // adding movie to movies array
                                    movieList.add(movie);

                                    final Handler searchhandler = new Handler();
                                    searchhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrolltoitem();
                                        }
                                    }, 500);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{

                            linearLayout.setVisibility(View.INVISIBLE);
                            relativeLayoutone.setVisibility(View.INVISIBLE);
                            relativeLayout.setVisibility(View.INVISIBLE);
                            videoView.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            emptyText.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
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
                final Handler searchhandler = new Handler();
                searchhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyText.setVisibility(View.VISIBLE);
                        emptyText.setText("Oops, error encountered: Retry");
                        refreshbtn.setVisibility(View.VISIBLE);

                    }
                }, 500);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void setupSpinners() {
        wallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 3) {
                    voucher.setVisibility(View.VISIBLE);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Follow the steps to generate your voucher code");
                    alertDialogBuilder.setMessage("1.Dial *110#\n2.Select option 6 (Generate Voucher)\n3.You will receive an SMS with 6 digits code\n\nNB: The code is active within 5 minutes.");
                    alertDialogBuilder.setPositiveButton("Alright",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(getActivity(), "Thank You", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    voucher.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                voucher.setVisibility(View.GONE);
            }
        });
    }

    public class AsyncPay extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.43.189/farmhousemovies/api/make_payment.php");


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mType", params[0])
                        .appendQueryParameter("movieID", params[1])
                        .appendQueryParameter("partID", params[2])
                        .appendQueryParameter("seasonID", params[3])
                        .appendQueryParameter("userID", params[4])
                        .appendQueryParameter("rstatus", params[5]);
                String query = builder.build().getEncodedQuery();



                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("payment status: ", result);
            if (result.contains("success")) {
                Log.d("Payment","payment recorded");
            }else if (result.contains("exception") || result.contains("unsuccessful")) {

                Toast.makeText(getActivity(), "Error connecting to server!.",
                        Toast.LENGTH_LONG).show();

                progressBarone.setVisibility(View.INVISIBLE);
                closebtn.setVisibility(View.VISIBLE);
                retrybtn.setVisibility(View.VISIBLE);
            }
        }
    }



    public class AsyncSetFav extends AsyncTask<String, String, String>
    {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url =
                        new URL("http://192.168.43.189/farmhousemovies/api/setfavourites.php?partid="+params[0]+"&movietype="+params[2]+"&movieid="+params[4]+"&seasonid="+params[3]+"&user="+userid);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // conn.setRequestMethod("GET");



                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("", "");
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if(result.contains("saved")) {
                Log.d("Favourites Status: ","Added to favorites");
                makechanges();

            }else if(result.contains("removed")){
                Log.d("Favourites Status: ","Removed from favorites");
                makechanges();
            }
            else if (!result.contains("saved")  || result.contains("error")){

                Log.d("Save Error: ","unable to save to favorites");


            } else if (result.contains("exception") || result.contains("unsuccessful")) {

                Log.d("Network Error: ","error connecting to server");

            }
        }



        @Override
        protected void onCancelled() {
            //do nothing
        }
    }

    private void makechanges() {

        getrefreshedrecords();
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


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    public void initmediaplayer(String videofilename) {


        if (player != null) {
            //player = null;
        } else {
            player = ExoPlayerFactory.newSimpleInstance(getActivity());
        }
        fullcontrolView.setPlayer(player);
        videoView.setPlayer(player);
        controlView.setPlayer(player);
        playView.setPlayer(player);
        player.setPlayWhenReady(playwhenready);
        Uri uri = Uri.parse(videofilename);
        MediaSource mediaSource = buildMediaSource(uri);
        player.seekTo(currentwindow, playbackposition);
        player.prepare(mediaSource, false, false);
        player.addListener(eventListener);
        controlView.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        if(!expidate.equalsIgnoreCase("N/A")) {
            startmonitoring();
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory datasourcefactory = new DefaultDataSourceFactory(getActivity(), "exoplayer" +
                "-codelab");
        return  new ProgressiveMediaSource.Factory(datasourcefactory).createMediaSource(uri);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
