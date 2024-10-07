package com.vandyflintz.farmhousemovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.vandyflintz.farmhousemovies.adapter.CustomPartsListAdapter;
import com.vandyflintz.farmhousemovies.adapter.CustomSearchAdapter;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.card.CardFragment;
import com.vandyflintz.farmhousemovies.card.CardPresenter;
import com.vandyflintz.farmhousemovies.data.SavedCard;
import com.vandyflintz.farmhousemovies.data.SavedPhone;
import com.vandyflintz.farmhousemovies.ghmobilemoney.GhMobileMoneyFragment;
import com.vandyflintz.farmhousemovies.ghmobilemoney.GhMobileMoneyPresenter;
import com.vandyflintz.farmhousemovies.model.Movie;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.vandyflintz.farmhousemovies.thetellerConstants.theteller_results;


public class Home extends AppCompatActivity implements MoviesAnnex.OnFragmentInteractionListener,
        SeasonAnnexEpisodes.OnSeasonFragmentInteractionListener,
        TelenovelaAnnexEpisodes.OnTeleFragmentInteractionListener,
        MostViewed.OnFragmentMostViewedInteractionListener, Movies.OnFragmentMoviesInteractionListener {

    private static final String TAG = "";
    Toolbar toolbar;
    ViewPager viewpager, mviewPager;
    TabLayout tabLayout, mtabLayout;
    public SectionsPagerAdapter mSectionsPagerAdapter, mmSectionsPagerAdapter;
    SurfaceView surfaceView;
    Button playbtn, pausebtn, stopbtn, fullscreenbtn, morebtn, annexbtn, epibtn, clearbtn,
            mloadmore, annexloadmore, epiloadmore, subscribebtn;
    LinearLayout linearLayout,linearLayout2, playlayout, menuLayout;
    TextView titletext,hiddenone,hiddentwo, selectedid,selmovieid , selectedseasonid,
            selectedepisodeid, vul;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    ImageView imageView;
    Bundle bundle;
    Intent intent;
    String videoname, videodir, vidname, userid,fav,part, frag, cacname, cactype, servermsg,
            pstatus;
    SeekBar seekBar;
    MoviesAnnex.OnDataPassed dataPassed;
    public boolean pausing = false, stopped = true;
    TextView starttime, endtime, titleannex, titleepi;
    RelativeLayout relativeLayout,footer,header;
    public boolean screenstate;
    SeekBar dseekBar;
    TextView mtitle, dstarttime, dendtime,selectedsid;
    Button exitfullscreen, playpause, reveal , setfav, refreshbtn, refreshbtnannex, refreshbtnepi;
    RelativeLayout drelativeLayout, relativeLayout2, relativeLayout3, infocon, annexrel, epirel,
            srel;
    public Handler  viewHandler = new Handler();
    Handler videoHandler  = new Handler();
    Runnable viewRunnable;
    MediaController mediaController;
    //VideoView videoView;
    SearchView searchView;
    CharSequence charSequence;
    String text;
    ListView listView, listViewannex, listViewannexepisodes;
    String[] movieslist;
    private List<Movie> movieList = new ArrayList<Movie>();
    private List<Movie> movieListannex = new ArrayList<Movie>();
    private List<Movie> movieListannexepisodes = new ArrayList<Movie>();
    private CustomSearchAdapter adapter;
    public CustomPartsListAdapter clAdapter;
    private CustomPartsListAdapter adapterannex;
    ProgressBar progressBar, progressBarannex, progressBarepi;
    TextView emptymsg, emptyTextannex, emptyTextepi, selectedtype;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String hone,htwo, oldname, mtype, req, curTask;
    public boolean resumedactivity;
    int pausedmillisecs, curpage, curpageannex, curpageepi, remaining, remainingannex, remainingepi;
    SharedPreferences sp,fsp, sharedPreferences;
    int listposition;
    PlayerView videoView;
    SimpleExoPlayer player;
    PlayerControlView controlView, playView, fullcontrolView;
    boolean playwhenready = true, exitdiag = false;
    int currentwindow = 0;
    long playbackposition = 0;
    private Runnable videoRunnable;
    Player.EventListener eventListener;
    Timer timer;
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
    String finalcode;
    public boolean detailsexist = false;
    public boolean movetonextscreen = false;
    TimerTask timerTask;
    MostViewed mvfrag;
    Movies mfrag;
    Season sfrag;
    public boolean isTime;
    View sfooterview, afooterview, efooterview;
    String sid, movieid, movietype;
    String partid, meprice,sname;
    String textquery, subscription, request;
    AlertDialog alertDialog;
    RelativeLayout relativeLayoutone, gridLayout;
    TextView amount, message, movietxt, hidspin, pnote;
    Button submit, closediagbtn, retrybtn;
    EditText num, voucher;
    Spinner wallets;
    String[] arrayspMoMo;
    ProgressBar progressBarone;
    String amt, walletnum, vouchernum, phonenum, status, expidate;
    public static final int CONNECTION_TIMEOUT_ONE=50000;
    public static final int READ_TIMEOUT_ONE=55000;
    public static int vpos, index;
    public String currentlistview, category, enddate, newdate, currentdate;
    public boolean alreadyplaying = false, isTracking = false, resExists = false;
    Date newerdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        setContentView(R.layout.activity_home);

        //new AsyncCheckDate().execute();




        checkdate();

        sp = getSharedPreferences("MovieDetails", Context.MODE_PRIVATE);
        videodir = (sp.getString("MoviePath", ""));
        vidname = (sp.getString("MovieTitle", ""));

        fsp = getSharedPreferences("MoviePartDetails", Context.MODE_PRIVATE);
        mtype = (fsp.getString("MovieType", ""));
        //fav = (fsp.getString("Favstatus", ""));
        part = (fsp.getString("MovieID",""));

        sharedPreferences = getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));

        subscribebtn = findViewById(R.id.subscribebtn);
        hiddenone = findViewById(R.id.hiddenone);
        srel = findViewById(R.id.shead);
        selmovieid = findViewById(R.id.smid);
        clearbtn = findViewById(R.id.clearbtn);
        hiddentwo = findViewById(R.id.hiddentwo);
        toolbar = findViewById(R.id.toolbar);
        viewpager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tablayout);
        mviewPager = findViewById(R.id.mpager);
        mtabLayout = findViewById(R.id.mtablayout);
        playlayout = findViewById(R.id.pcon);
        //surfaceView = findViewById(R.id.sv);
        epirel= findViewById(R.id.epilayout);
        annexrel = findViewById(R.id.annexlayout);
        epibtn = findViewById(R.id.episodesbackbtn);
        annexbtn = findViewById(R.id.annexbackbtn);
        playView = findViewById(R.id.ppcv);
        toolbar.inflateMenu(R.menu.mymenu);
        imageView = findViewById(R.id.imgoverlay);
        seekBar = findViewById(R.id.mseekbar);
        playbtn = findViewById(R.id.playbtn);
        pausebtn =  findViewById(R.id.pausebtn);
        stopbtn = findViewById(R.id.stopbtn);
        fullscreenbtn = findViewById(R.id.btnmagnify);
        linearLayout = findViewById(R.id.controlscon);
        linearLayout2 = findViewById(R.id.seekcon);
        titletext = findViewById(R.id.movietitle);
        titleannex = findViewById(R.id.annextitletxt);
        titleepi = findViewById(R.id.episodestitletxt);
        starttime = findViewById(R.id.starttime);
        selectedtype = findViewById(R.id.txttype);
        endtime = findViewById(R.id.endtime);
        relativeLayout2 = findViewById(R.id.linear);
        relativeLayout3 = findViewById(R.id.mothercon);
        drelativeLayout = findViewById(R.id.conpanel);
        videoView = findViewById(R.id.sv);
        dseekBar = findViewById(R.id.dseekbar);
        mtitle = findViewById(R.id.dtitle);
        selectedid = findViewById(R.id.selid);
        selectedsid = findViewById(R.id.txtsid);
        dstarttime = findViewById(R.id.dstarttime);
        dendtime = findViewById(R.id.dendtime);
        exitfullscreen = findViewById(R.id.dmagbtn);
        playpause = findViewById(R.id.playpause);
        header = findViewById(R.id.head);
        footer = findViewById(R.id.footer);
        searchView = findViewById(R.id.searchbar);
        listView = findViewById(R.id.searchwindow);
        listViewannex = findViewById(R.id.searchannex);
        listViewannexepisodes = findViewById(R.id.searchannexepisodes);
        progressBar = findViewById(R.id.sBar);
        emptymsg = findViewById(R.id.emptylistmsg);
        infocon = findViewById(R.id.infocon);
        reveal = findViewById(R.id.infobtn);
        setfav = findViewById(R.id.favbtn);
        menuLayout = findViewById(R.id.morelayout);
        morebtn = findViewById(R.id.moreoptbtn);
        progressBarannex = findViewById(R.id.sBarannex);
        progressBarepi = findViewById(R.id.sBarepi);
        emptyTextannex = findViewById(R.id.emptytextannex);
        emptyTextepi = findViewById(R.id.emptytextepi);
        refreshbtn = findViewById(R.id.refreshbtn);
        refreshbtnannex = findViewById(R.id.refreshbtnannex);
        refreshbtnepi = findViewById(R.id.refreshbtnepi);

        sfooterview =
                ((LayoutInflater)Home.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        mloadmore = sfooterview.findViewById(R.id.btnloadmore);

        afooterview =
                ((LayoutInflater)Home.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        annexloadmore = afooterview.findViewById(R.id.btnloadmore);

        efooterview =
                ((LayoutInflater)Home.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        epiloadmore = efooterview.findViewById(R.id.btnloadmore);


        curpage = 1;
        curpageannex = 1;
        curpageepi = 1;

        adapter = new CustomSearchAdapter(this, movieList);
        clAdapter = new CustomPartsListAdapter(this, movieListannex);
        adapterannex = new CustomPartsListAdapter(this, movieListannexepisodes);
        listView.setAdapter(adapter);
        listViewannex.setAdapter(clAdapter);
        listViewannexepisodes.setAdapter(adapterannex);
        menuLayout.setVisibility(View.GONE);
        hone = hiddenone.getText().toString();
        htwo = hiddentwo.getText().toString();

        progressBar.setVisibility(View.GONE);
        progressBarannex.setVisibility(View.GONE);
        progressBarepi.setVisibility(View.GONE);
        emptymsg.setVisibility(View.INVISIBLE);
        emptyTextannex.setVisibility(View.INVISIBLE);
        emptyTextepi.setVisibility(View.INVISIBLE);
        refreshbtn.setVisibility(View.GONE);
        refreshbtnannex.setVisibility(View.GONE);
        refreshbtnepi.setVisibility(View.GONE);

        listView.addFooterView(sfooterview);
        sfooterview.setVisibility(View.GONE);
        mloadmore.setVisibility(View.GONE);

        listViewannex.addFooterView(afooterview);
        afooterview.setVisibility(View.GONE);
        annexloadmore.setVisibility(View.GONE);

        listViewannexepisodes.addFooterView(efooterview);
        efooterview.setVisibility(View.GONE);
        epiloadmore.setVisibility(View.GONE);

        drelativeLayout.setVisibility(View.INVISIBLE);
        infocon.setVisibility(View.INVISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);
       playlayout.setVisibility(View.INVISIBLE);
        srel.setVisibility(View.INVISIBLE);
        annexrel.setVisibility(View.INVISIBLE);
        epirel.setVisibility(View.INVISIBLE);
        clearbtn.setVisibility(View.GONE);
        intent = getIntent();

        bundle = intent.getExtras();
        if(bundle != null){
            //initmediaplayer(videodir);
        }

        isTime = false;


        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbtn.setVisibility(View.GONE);
                emptymsg.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                        ".google" +
                        ".android.tv")){
                    getresultfromserver(textquery, "tv",curpage);
                }else{
                    getresultfromserver(textquery,"other", curpage);
                }

            }
        });


        refreshbtnannex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbtnannex.setVisibility(View.GONE);
                emptyTextannex.setVisibility(View.INVISIBLE);
                progressBarannex.setVisibility(View.VISIBLE);

                if((movietype.equals("Season") || movietype.equals("Series")) && (sid == null || sid.equals(""))){


                    if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                            ".google" +
                            ".android.tv")){
                        movetoseries(movieid, movietype, "tv", curpageannex);

                    }else{
                        movetoseries(movieid, movietype, "other", curpageannex);
                    }

                }else if((movietype.equals("Season") || movietype.equals("Series")) && (sid != null || sid != "")){
                    epirel.setVisibility(View.VISIBLE);
                    listViewannexepisodes.setVisibility(View.VISIBLE);
                    req = "sep";
                    if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                            ".google" +
                            ".android.tv")){
                        movetoepisodes(movieid, sid , movietype, "tv", curpageannex, req);

                    }else{
                        movetoepisodes(movieid, sid , movietype, "other", curpageannex, req);
                    }
                }

            }
        });

        refreshbtnepi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbtnepi.setVisibility(View.GONE);
                emptyTextepi.setVisibility(View.INVISIBLE);
                progressBarepi.setVisibility(View.VISIBLE);

                req = "ep";
                if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                        ".google" +
                        ".android.tv")){
                    movetoepisodes(movieid, sid , movietype, "tv", curpageepi, req);

                }else{
                    movetoepisodes(movieid, sid , movietype, "other", curpageepi, req);
                }

            }
        });


        mloadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                curpage += 1;

                if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                        ".google" +
                        ".android.tv")){
                    getresultfromserver(textquery, "tv",curpage);
                }else{
                    getresultfromserver(textquery,"other", curpage);
                }
            }
        });

        annexloadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            curpageannex += 1;

                if((movietype.equals("Season") || movietype.equals("Series")) && (sid == null || sid.equals(""))){
                    //Toast.makeText(Home.this, sid, Toast.LENGTH_LONG).show();

                    if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                            ".google" +
                            ".android.tv")){
                        movetoseries(movieid, movietype, "tv", curpageannex);

                    }else{
                        movetoseries(movieid, movietype, "other", curpageannex);
                    }

                }else if((movietype.equals("Season") || movietype.equals("Series")) && (sid != null || sid != "")){
                    //Toast.makeText(Home.this, sid, Toast.LENGTH_LONG).show();
                    req = "sep";
                    if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                            ".google" +
                            ".android.tv")){
                        movetoepisodes(movieid, sid , movietype, "tv", curpageannex, req);

                    }else{
                        movetoepisodes(movieid, sid , movietype, "other", curpageannex, req);
                    }
                }

                else {


                    if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                            ".google" +
                            ".android.tv")){
                        movetoparts(movieid, "tv", curpageannex);

                    }else{

                        movetoparts(movieid, "other", curpageannex);
                    }
                }
            }
        });

        epiloadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curpageepi += 1;
                req = "ep";
                if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                        ".google" +
                        ".android.tv")){
                    movetoepisodes(movieid, sid , movietype, "tv", curpageepi, req);

                }else{
                    movetoepisodes(movieid, sid , movietype, "other", curpageepi, req);
                }
            }
        });



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() - listView.getFooterViewsCount())>= (adapter.getCount()- 1)){
                    //Toast.makeText(Home.this, "Bottom has been reached", Toast.LENGTH_LONG)
                    // .show();
                    if(remaining != 0){
                        sfooterview.setVisibility(View.VISIBLE);
                        mloadmore.setVisibility(View.VISIBLE);
                    }else{
                        sfooterview.setVisibility(View.GONE);
                        mloadmore.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listViewannex.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (listViewannex.getLastVisiblePosition() - listViewannex.getHeaderViewsCount() - listViewannex.getFooterViewsCount())>= (clAdapter.getCount()- 1)){
                    //Toast.makeText(Home.this, "Bottom has been reached", Toast.LENGTH_LONG)
                    // .show();
                    if(remainingannex != 0){
                        afooterview.setVisibility(View.VISIBLE);
                        annexloadmore.setVisibility(View.VISIBLE);
                    }else{
                        afooterview.setVisibility(View.GONE);
                        annexloadmore.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listViewannexepisodes.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (listViewannexepisodes.getLastVisiblePosition() - listViewannexepisodes.getHeaderViewsCount() - listViewannexepisodes.getFooterViewsCount())>= (adapterannex.getCount()- 1)){
                    //Toast.makeText(Home.this, "Bottom has been reached", Toast.LENGTH_LONG)
                    // .show();
                    if(remainingepi != 0){
                        efooterview.setVisibility(View.VISIBLE);
                        epiloadmore.setVisibility(View.VISIBLE);
                    }else{
                        efooterview.setVisibility(View.GONE);
                        epiloadmore.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuLayout.getVisibility() == View.VISIBLE){
                    menuLayout.setVisibility(View.GONE);
                }else{
                    menuLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.invalidateViews();
                movieList.clear();
                srel.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });


        View decorview = getWindow().getDecorView();
        decorview.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0){

                }else{
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    getWindow().getDecorView().setSystemUiVisibility(uioptions);
                }
            }
        });

         controlView = findViewById(R.id.pcv);
         controlView.setVisibility(View.INVISIBLE);
         fullcontrolView = findViewById(R.id.fullpcv);

        videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        videoView.setUseController(false);

        videoView.requestFocus();


        subscribebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subscribed = "1";
                String unsubscribed = "0";

                if(subscribebtn.getBackground().getConstantState()==getResources().getDrawable(R.drawable.ic_unsubscribed).getConstantState()){
                    new AsyncSetSubscr().execute(subscribed,movieid,userid);
                    subscribebtn.setBackgroundResource(R.drawable.ic_subscribed);
                    Toast.makeText(Home.this, "Subscribed", Toast.LENGTH_LONG).show();
                }else{
                    new AsyncSetSubscr().execute(unsubscribed,movieid,userid);
                    subscribebtn.setBackgroundResource(R.drawable.ic_unsubscribed);
                    Toast.makeText(Home.this, "Unsubscribed", Toast.LENGTH_LONG).show();
                }

            }
        });

        setfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mpid = selectedid.getText().toString();
                final String esid = selectedsid.getText().toString();
                final String etype = selectedtype.getText().toString();
                final String emid = selmovieid.getText().toString();
                String selected = "1";
                String deselected = "0";
                if(setfav.getBackground().getConstantState()==getResources().getDrawable(R.drawable.ic_favourite_unchecked).getConstantState()){
                    new AsyncSetFav().execute(selected,mpid,userid,etype,esid, emid);
                    setfav.setBackgroundResource(R.drawable.ic_favourite_checked);
                    Toast.makeText(Home.this, "Added to favourites", Toast.LENGTH_LONG).show();
                }else{
                    new AsyncSetFav().execute(deselected,mpid,userid,etype,esid, emid);
                    setfav.setBackgroundResource(R.drawable.ic_favourite_unchecked);
                    Toast.makeText(Home.this, "Removed from favourites", Toast.LENGTH_LONG).show();
                }

            }
        });

        annexbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annexrel.setVisibility(View.INVISIBLE);
                listViewannex.invalidateViews();
                movieListannex.clear();
            }
        });

        epibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epirel.setVisibility(View.INVISIBLE);
                listViewannexepisodes.invalidateViews();
                movieListannexepisodes.clear();
            }
        });



        reveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (infocon.getVisibility() == View.VISIBLE) {
                    infocon.setVisibility(View.INVISIBLE);
                    reveal.setBackgroundResource(R.drawable.reveal_btn);
                } else {
                    infocon.setVisibility(View.VISIBLE);
                    reveal.setBackgroundResource(R.drawable.hide_btn);
                }
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.exit){
                    new AlertDialog.Builder(Home.this, R.style.myDialogTheme).setTitle("Message")
                            .setMessage("😮 Sure about logging out?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int buttonselected) {
                                    Toast.makeText(Home.this, "You have successfully logged out!", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();
                                    finish();

                                    SharedPreferences nsp = getSharedPreferences("SignupDetails",
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor neditor = nsp.edit();
                                    neditor.clear();
                                    neditor.commit();
                                    finish();

                                    Intent intent = new Intent(Home.this, SigninActivity.class);
                                    startActivity(intent);
                                    Home.this.finish();

                                }
                            }).setNegativeButton(android.R.string.no, null).setCancelable(true).show();
                }

                if(item.getItemId()== R.id.reload){
                    recreate();
                }

                if(item.getItemId()==R.id.news){
                    Intent intent = new Intent(Home.this, News.class);
                    startActivity(intent);
                }


                if(item.getItemId()==R.id.profile){
                    Intent intent = new Intent(Home.this, UserProfile.class);
                    startActivity(intent);
                }
                return false;

            }
        });



       playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpause.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                if(player.isPlaying()){
                    player.setPlayWhenReady(false);
                    playpause.setBackgroundResource(R.drawable.play_btn);
                }else if(!player.isPlaying() && (player.getPlaybackState()== ExoPlayer.STATE_READY)){
                    player.setPlayWhenReady(true);
                    playpause.setBackgroundResource(R.drawable.pause_btn);
                }else {
                    player.seekTo(0);
                    player.setPlayWhenReady(true);
                    playpause.setBackgroundResource(R.drawable.pause_btn);
                    drelativeLayout.setVisibility(View.INVISIBLE);
                    fullcontrolView.setShowTimeoutMs(0);
                }
            }
        });


        viewRunnable = new Runnable() {
            @Override
            public void run() {
                drelativeLayout.setVisibility(View.INVISIBLE);
            }
        };




        videoView.getVideoSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(screenstate) {
                    if (drelativeLayout.getVisibility() == View.VISIBLE) {
                        drelativeLayout.setVisibility(View.INVISIBLE);

                    } else {
                        drelativeLayout.setVisibility(View.VISIBLE);
                        drelativeLayout.bringToFront();

                        viewHandler.postDelayed(viewRunnable, 2000);
                    }
                }else{
                    if(player!=null){
                        if(player.isPlaying()){
                            player.setPlayWhenReady(false);
                        }else if(!player.isPlaying() && (player.getPlaybackState() == ExoPlayer.STATE_READY)){
                            player.setPlayWhenReady(true);
                        }else {
                            player.seekTo(0);
                            player.setPlayWhenReady(true);
                        }
                    }


                }
            }
        });





        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    player.seekTo(progress);
                    int position = (int) player.getCurrentPosition();
                    starttime.setText("" + convertintotime(position));
                    endtime.setText("-" + convertintotime((int) (player.getDuration() - position)));
                    dstarttime.setText("" + convertintotime(position));
                    dendtime.setText("-" + convertintotime((int) (player.getDuration() - position)));
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


        dseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    player.seekTo(progress);
                    int position = (int) player.getCurrentPosition();
                    starttime.setText("" + convertintotime(position));
                    endtime.setText("-" + convertintotime((int) (player.getDuration() - position)));
                    dstarttime.setText("" + convertintotime(position));
                    dendtime.setText("-" + convertintotime((int) (player.getDuration() - position)));
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


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);

        // mmSectionsPagerAdapter = new mSectionsPagerAdapterm(getSupportFragmentManager());
        mviewPager.setOffscreenPageLimit(3);
        mviewPager.setAdapter(new mSectionsPagerAdapterm(getSupportFragmentManager()));
        mtabLayout.setupWithViewPager(mviewPager);



        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playbtn.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                pausing = false;
                imageView.setVisibility(View.INVISIBLE);
                if(!player.isPlaying()){
                    player.setPlayWhenReady(true);

                }
            }
        });

        fullscreenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLayout.setVisibility(View.GONE);
                fullscreenbtn.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                getWindow().getDecorView().setSystemUiVisibility(uioptions);
                screenstate = true;
                titletext.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
                footer.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                controlView.setVisibility(View.GONE);
                controlView.setPlayer(null);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                videoView.setShowMultiWindowTimeBar(true);
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

                relativeLayout2.setLayoutParams(params);
                videoView.setLayoutParams(params);

            }
        });


        exitfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitfullscreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                screenstate = false;
                videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                drelativeLayout.setVisibility(View.INVISIBLE);
                titletext.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);
                footer.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                controlView.setVisibility(View.VISIBLE);
                controlView.setPlayer(player);
                //linearLayout2.setVisibility(View.VISIBLE);

                (findViewById(R.id.movietitle)).setVisibility(View.VISIBLE);
                (findViewById(R.id.pcv)).setVisibility(View.VISIBLE);
                (findViewById(R.id.head)).setVisibility(View.VISIBLE);
                (findViewById(R.id.footer)).setVisibility(View.VISIBLE);
                //(findViewById(R.id.seekcon)).setVisibility(View.VISIBLE);
                (findViewById(R.id.controlscon)).setVisibility(View.VISIBLE);


                RelativeLayout.LayoutParams params2 =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                android.widget.RelativeLayout.LayoutParams params =
                        (android.widget.RelativeLayout.LayoutParams)videoView.getLayoutParams();
                params.width = displayMetrics.widthPixels;
                params.height =(int)(185*displayMetrics.density);
                params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                int margins = dp(10);
                int margin = dp(58);
                params.bottomMargin = margins;

                params.topMargin=margins;
                params2.topMargin = margin;
                videoView.setTranslationZ(0);
                relativeLayout2.setLayoutParams(params2);
                videoView.setLayoutParams(params);
            }
        });



       pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausebtn.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                // Toast.makeText(Home.this, "Pausing video", Toast.LENGTH_LONG).show();
                if(player.isPlaying()){
                    player.setPlayWhenReady(false);
                    pausing = true;

                }
            }
        });


        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageView.setVisibility(View.VISIBLE);
                stopbtn.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                if(player.isPlaying() || !pausing){
                    int duration = (int) player.getDuration();
                    player.seekTo(duration);
                    seekBar.setProgress(duration);
                    dseekBar.setProgress(duration);
                    // mediaPlayer.stop();
                    stopped = true;
                }

            }
        });

        charSequence = searchView.getQuery();
        Intent searchintent = getIntent();
        if(Intent.ACTION_SEARCH.equals(searchintent.getAction())){
            String query = searchintent.getStringExtra(SearchManager.QUERY);
            getSearchResults(query);
        }

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im =
                        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(searchView, 0);
                searchView.setIconified(false);
                toolbar.setVisibility(View.INVISIBLE);
                emptymsg.setVisibility(View.INVISIBLE);
                emptymsg.setText("");
                if(adapter.getCount() > 0  && adapter != null){
                   srel.setVisibility(View.VISIBLE);
                   clearbtn.setVisibility(View.VISIBLE);
                }else{
                    srel.setVisibility(View.INVISIBLE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textquery = query;
                InputMethodManager im =
                        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));

                if(textquery.length()> 0) {
                    getSearchResults(textquery);
                    im.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }else{
                    Toast.makeText(Home.this, "You can't submit an empty request.", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textquery = newText;

                return true;
            }
        });

        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    textquery = searchView.getQuery().toString();
                    getSearchResults(textquery);
                    return true;
                }
                return false;
            }
        });



        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                final Handler searchhandler = new Handler();
                searchhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srel.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        emptymsg.setVisibility(View.INVISIBLE);
                        emptymsg.setText("");
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }, 0);
                return false;
            }
        });

        int searchclosebuttonid = searchView.getContext().getResources().getIdentifier("android" +
                ":id/search_close_btn", null, null);

        ImageView closebtn = this.searchView.findViewById(searchclosebuttonid);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srel.setVisibility(View.INVISIBLE);
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView)view.findViewById(R.id.mfile));
                TextView textView2 = ((TextView)view.findViewById(R.id.stitle));
                TextView textView3 = ((TextView)view.findViewById(R.id.mmid));
                TextView textView4 = ((TextView)view.findViewById(R.id.mdesc));
                TextView textView5 = ((TextView)view.findViewById(R.id.mdate));
                TextView textView6 = ((TextView)view.findViewById(R.id.mpid));
                TextView textView7 = ((TextView)view.findViewById(R.id.mfid));
                TextView textView8 = ((TextView)view.findViewById(R.id.mtype));
                TextView seasonid = ((TextView)view.findViewById(R.id.msid));
                TextView subscr = ((TextView)view.findViewById(R.id.msub));
                TextView price = ((TextView)view.findViewById(R.id.mprice));
                TextView paidstatus = ((TextView)view.findViewById(R.id.paidbtn));
                TextView expDate = ((TextView)view.findViewById(R.id.mexp));

                String expiryDate = expDate.getText().toString();

                String moviefile = textView.getText().toString();
                sname = textView2.getText().toString();
                currentlistview = "listView";
                movieid = textView3.getText().toString() ;
                String moviedesc = textView4.getText().toString();
                String partdate = textView5.getText().toString();
                partid = textView6.getText().toString();
                String favistatus = textView7.getText().toString();
                meprice = price.getText().toString();
                movietype = textView8.getText().toString();
                sid = seasonid.getText().toString();
                TextView snametxt = ((TextView)view.findViewById(R.id.sesname));
                TextView epnametxt = ((TextView)view.findViewById(R.id.epname));
                TextView onametxt = ((TextView)view.findViewById(R.id.finalhiddentitle));

                String sesname = snametxt.getText().toString();
                String epname = epnametxt.getText().toString();
                String ofname = onametxt.getText().toString();

                saveRecordDetails(ofname,sesname,epname);
                
                if(sname.equalsIgnoreCase("part") || sname.equalsIgnoreCase("episode") || sname.contains("Part") || sname.contains("Episode")){

                    if(paidstatus.getVisibility() == View.VISIBLE){
                        //  Toast.makeText(Home.this, "You haven't paid for this movie",
                        // Toast.LENGTH_LONG).show();
                        if(movietype.equalsIgnoreCase("Single")){
                            showpaymentdialog(movietype, movieid, "", partid, userid,meprice,
                                    sname);
                        }else if(movietype.equalsIgnoreCase("Season")){
                            showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                    sname);
                        }else if(movietype.equalsIgnoreCase("Series")){
                            showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                    sname);
                        }

                    }
                    else {
                        alreadyplaying = true;
                        expidate = expiryDate;
                        srel.setVisibility(View.INVISIBLE);
                        fav = favistatus;
                        titletext.setText(sname);
                        subscription = subscr.getText().toString();
                        mtitle.setText(sname);
                        imageView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.VISIBLE);
                        playlayout.setVisibility(View.VISIBLE);
                        titletext.setVisibility(View.VISIBLE);
                        searchView.setIconified(true);
                        searchView.onActionViewCollapsed();
                        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        videoname = moviefile;
                        initmediaplayer();
                        final Handler searchhandler = new Handler();
                        searchhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                starttracking(expidate);
                            }
                        }, 2000);
                        playbackposition = 0;
                        savePartDetails(partid, partdate, moviedesc, favistatus, movietype, sid);
                        //Toast.makeText(Home.this,
                        //partid+"\n"+partdate+"\n"+moviedesc+"\n"+favistatus+"\n"+movietype+"\n"+sid,
                        //Toast.LENGTH_LONG ).show();
                        selectedid.setText(partid);
                        selectedsid.setText(sid);
                        selmovieid.setText(movieid);
                        selectedtype.setText(movietype);
                       // new AsyncRecordView().execute(partid, movietype, sid);
                        SharedPreferences rsp = getSharedPreferences("RecordDetails", Context.MODE_PRIVATE);
                        String movname = (rsp.getString("Mname", ""));
                        String sename = (rsp.getString("Sname", ""));
                        String epiname = (rsp.getString("Ename", ""));

                        final Handler recordhandler = new Handler();
                        recordhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncRecordView().execute(partid,mtype, sid, movieid, movname,
                                        sename, epiname,
                                        userid);

                            }
                        }, 1000);


                    }
                }else {
                    annexrel.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    listViewannex.setVisibility(View.VISIBLE);
                    if((movietype.equals("Season") || movietype.equals("Series")) && (sid == null || sid.equals(""))){
                        //Toast.makeText(Home.this, sid, Toast.LENGTH_LONG).show();

                        if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                                ".google" +
                                ".android.tv")){
                            movetoseries(movieid, movietype, "tv", curpageannex);

                        }else{
                            movetoseries(movieid, movietype, "other", curpageannex);
                        }

                    }else if((movietype.equals("Season") || movietype.equals("Series")) && (sid != null || sid != "")){
                        //Toast.makeText(Home.this, sid, Toast.LENGTH_LONG).show();
                        req = "sep";
                        request = "episodes";
                        if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                                ".google" +
                                ".android.tv")){
                            movetoepisodes(movieid, sid , movietype, "tv", curpageannex, req);

                        }else{
                            movetoepisodes(movieid, sid , movietype, "other", curpageannex, req);
                        }
                    }

                    else {
                        request = "parts";

                        if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                                ".google" +
                                ".android.tv")){
                            movetoparts(movieid, "tv", curpageannex);

                        }else{

                            movetoparts(movieid, "other", curpageannex);
                        }
                    }
                }

            }
        });


        listViewannex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = ((TextView)view.findViewById(R.id.mfile));
                TextView textView2 = ((TextView)view.findViewById(R.id.mtitle));
                TextView textView3 = ((TextView)view.findViewById(R.id.mmid));
                TextView textView4 = ((TextView)view.findViewById(R.id.mdesc));
                TextView textView5 = ((TextView)view.findViewById(R.id.mdate));
                TextView textView6 = ((TextView)view.findViewById(R.id.mpid));
                TextView textView7 = ((TextView)view.findViewById(R.id.mfid));
                TextView textView8 = ((TextView)view.findViewById(R.id.mtype));
                TextView seasonid = ((TextView)view.findViewById(R.id.msid));
                TextView shortname = view.findViewById(R.id.shname);
                TextView subscr = view.findViewById(R.id.msub);
                TextView price = ((TextView)view.findViewById(R.id.mprice));
                TextView paidstatus = ((TextView)view.findViewById(R.id.paidbtn));
                TextView expDate = ((TextView)view.findViewById(R.id.mexp));
                TextView snametxt = ((TextView)view.findViewById(R.id.sesname));
                TextView epnametxt = ((TextView)view.findViewById(R.id.epname));
                TextView onametxt = ((TextView)view.findViewById(R.id.finalhiddentitle));

                String sesname = snametxt.getText().toString();
                String epname = epnametxt.getText().toString();
                String ofname = onametxt.getText().toString();
                
                saveRecordDetails(ofname,sesname,epname);
                
                String expiryDate = expDate.getText().toString();
                String moviefile = textView.getText().toString();
                String movietitle = textView2.getText().toString() ;
                movieid = textView3.getText().toString() ;
                String moviedesc = textView4.getText().toString();
                String partdate = textView5.getText().toString();
                 partid = textView6.getText().toString();
                String favistatus = textView7.getText().toString();
                movietype = textView8.getText().toString();
                sname = shortname.getText().toString();
                meprice = price.getText().toString();
                sid = seasonid.getText().toString();
                subscription = subscr.getText().toString();
                currentlistview = "listViewannex";
                if(movietitle.contains("Part") || movietitle.contains("Episode")) {
                   //Toast.makeText(Home.this, movietype, Toast.LENGTH_LONG).show();
                    if(paidstatus.getVisibility() == View.VISIBLE){
                        //  Toast.makeText(Home.this, "You haven't paid for this movie",
                        // Toast.LENGTH_LONG).show();
                        if(movietype.equalsIgnoreCase("Single")){
                            showpaymentdialog(movietype, movieid, "", partid, userid,meprice,
                                    movietitle);
                        }else if(movietype.equalsIgnoreCase("Season")){
                            showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                    movietitle);
                        }else if(movietype.equalsIgnoreCase("Series")){
                            showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                    movietitle);
                        }

                    }
                    else{
                        SharedPreferences rsp = getSharedPreferences("RecordDetails", Context.MODE_PRIVATE);
                        String movname = (rsp.getString("Mname", ""));
                        String sename = (rsp.getString("Sname", ""));
                        String epiname = (rsp.getString("Ename", ""));

                        final Handler recordhandler = new Handler();
                        recordhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new AsyncRecordView().execute(partid,mtype, sid, movieid, movname,
                                        sename, epiname,
                                        userid);

                            }
                        }, 1000);

                        alreadyplaying = true;
                    expidate = expiryDate;
                    srel.setVisibility(View.INVISIBLE);
                    fav = favistatus;
                    if (movietitle.contains("Part")) {
                        titletext.setText(movietitle);
                        mtitle.setText(movietitle);
                    }
                    if (movietitle.contains("Episode")) {
                        titletext.setText(sname);
                        mtitle.setText(sname);
                    }
                    imageView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    titletext.setVisibility(View.VISIBLE);
                    playlayout.setVisibility(View.VISIBLE);
                    searchView.setIconified(true);
                    searchView.onActionViewCollapsed();
                    InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                    im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    videoname = moviefile;
                    initmediaplayer();
                        final Handler searchhandler = new Handler();
                        searchhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                starttracking(expidate);
                            }
                        }, 2000);
                    playbackposition = 0;
                    savePartDetails(partid, partdate, moviedesc, favistatus, movietype, sid);
                    selectedid.setText(partid);
                    selectedsid.setText(sid);
                    selmovieid.setText(movieid);
                    selectedtype.setText(movietype);
                    //new AsyncRecordView().execute(partid, movietype, sid);

                }

                }else{
                    epirel.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    listViewannexepisodes.setVisibility(View.VISIBLE);

                   // Toast.makeText(Home.this, movieid+"\n"+sid+"\n"+movietype+"\n"+curpageannex,
                     //       Toast.LENGTH_LONG).show();

                    req = "ep";

                    if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                            ".google" +
                            ".android.tv")){
                        movetoepisodes(movieid, sid , movietype, "tv", curpageepi, req);

                    }else{
                        movetoepisodes(movieid, sid , movietype, "other", curpageepi, req);
                    }
                }


            }
        });


        listViewannexepisodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView) view.findViewById(R.id.mfile));
                TextView textView2 = ((TextView) view.findViewById(R.id.mtitle));
                TextView textView3 = ((TextView) view.findViewById(R.id.mmid));
                TextView textView4 = ((TextView) view.findViewById(R.id.mdesc));
                TextView textView5 = ((TextView) view.findViewById(R.id.mdate));
                TextView textView6 = ((TextView) view.findViewById(R.id.mpid));
                TextView textView7 = ((TextView) view.findViewById(R.id.mfid));
                TextView textView8 = ((TextView) view.findViewById(R.id.mtype));
                TextView seasonid = ((TextView) view.findViewById(R.id.msid));
                TextView subscr = ((TextView) view.findViewById(R.id.msub));
                TextView shortname = view.findViewById(R.id.shname);
                TextView price = ((TextView)view.findViewById(R.id.mprice));
                TextView paidstatus = ((TextView)view.findViewById(R.id.paidbtn));
                TextView expDate = ((TextView)view.findViewById(R.id.mexp));
               
                TextView snametxt = ((TextView)view.findViewById(R.id.sesname));
                TextView epnametxt = ((TextView)view.findViewById(R.id.epname));
                TextView onametxt = ((TextView)view.findViewById(R.id.finalhiddentitle));

                String sesname = snametxt.getText().toString();
                String epname = epnametxt.getText().toString();
                String ofname = onametxt.getText().toString();

                saveRecordDetails(ofname,sesname,epname);
                
                String expiryDate = expDate.getText().toString();
                String moviefile = textView.getText().toString();
                String movietitle = textView2.getText().toString();
                movieid = textView3.getText().toString();
                String moviedesc = textView4.getText().toString();
                String partdate = textView5.getText().toString();
                partid = textView6.getText().toString();
                String favistatus = textView7.getText().toString();
                String movietype = textView8.getText().toString();
                 sname = shortname.getText().toString();
                 meprice = price.getText().toString();
                sid = seasonid.getText().toString();
                subscription = subscr.getText().toString();
                currentlistview = "listViewannexepisodes";
                if(paidstatus.getVisibility() == View.VISIBLE){
                    //  Toast.makeText(Home.this, "You haven't paid for this movie",
                    // Toast.LENGTH_LONG).show();
                    if(movietype.equalsIgnoreCase("Single")){
                        showpaymentdialog(movietype, movieid, "", partid, userid,meprice,
                                sname);
                    }else if(movietype.equalsIgnoreCase("Season")){
                        showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                sname);
                    }else if(movietype.equalsIgnoreCase("Series")){
                        showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                sname);
                    }

                }
                else{
                    alreadyplaying = true;
                    expidate = expiryDate;
                    srel.setVisibility(View.INVISIBLE);
                    fav = favistatus;
                titletext.setText(sname);
                mtitle.setText(sname);
                imageView.setVisibility(View.GONE);
                playlayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                titletext.setVisibility(View.VISIBLE);
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                videoname = moviefile;
                initmediaplayer();
                    final Handler searchhandler = new Handler();
                    searchhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            starttracking(expidate);
                        }
                    }, 2000);
                playbackposition = 0;
                savePartDetails(partid, partdate, moviedesc, favistatus, movietype, sid);
                selectedid.setText(partid);
                selectedsid.setText(sid);
                selmovieid.setText(movieid);
                selectedtype.setText(movietype);
                    SharedPreferences rsp = getSharedPreferences("RecordDetails", Context.MODE_PRIVATE);
                    String movname = (rsp.getString("Mname", ""));
                    String sename = (rsp.getString("Sname", ""));
                    String epiname = (rsp.getString("Ename", ""));

                    final Handler recordhandler = new Handler();
                    recordhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncRecordView().execute(partid,mtype, sid, movieid, movname,
                                    sename, epiname,
                                    userid);

                        }
                    }, 1000);

            }
                }

        });

    eventListener = new Player.EventListener() {


        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


            if(playbackState == Player.STATE_READY){

                if(!isTracking){
                    starttracking(expidate);
                }
            }
            if(playbackState == ExoPlayer.STATE_ENDED){
                if(isTime) {
                    timer.cancel();
                    timerTask.cancel();
                    isTime = false;
                    isTracking = false;
                }


                playpause.setBackgroundResource(R.drawable.play_btn);

                if(screenstate) {
                        drelativeLayout.setVisibility(View.VISIBLE);
                        fullcontrolView.show();
                }
            }
        }


    };

    }

    private void saveRecordDetails(String moviename, String seasonname, String episodename){
        new PreferencesManager(Home.this).saveRecordDetails(moviename,seasonname,episodename);
    }
    
    private void starttracking(String expidate) {

        if(!expidate.equalsIgnoreCase("N/A")){


            timer = new Timer();
            isTime = true;
            resExists = true;
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    isTracking = true;
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            //Toast.makeText(Home.this, String.valueOf(mins), Toast.LENGTH_LONG)
                            // .show();
                            if(mins < 1){
                                if(frag != null){
                                    refreshfragsetfav(frag,listposition);
                                }
                                if(player != null){
                                    player.setPlayWhenReady(false);
                                }

                                timer.cancel();
                                timerTask.cancel();
                                isTime = false;
                                isTracking = false;
                                resExists = false;
                                if(movietype.equalsIgnoreCase("Single")){
                                    showpaymentdialog(movietype, movieid, "", partid, userid,meprice,
                                            sname);
                                }else if(movietype.equalsIgnoreCase("Season")){
                                    showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                            sname);
                                }else if(movietype.equalsIgnoreCase("Series")){
                                    showpaymentdialog(movietype, movieid, sid, partid, userid,meprice,
                                            sname);
                                }

                            }
                        }
                    });

                }
            };
            timer.scheduleAtFixedRate(timerTask,0,1000);





        }
    }

    private void checkdate() {
        Date pdate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String caldate = simpleDateFormat.format(pdate);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String timeserver = "time-a.nist.gov";
                    NTPUDPClient timeClient = new NTPUDPClient();
                    InetAddress inetAddress = InetAddress.getByName(timeserver);
                    TimeInfo timeInfo = timeClient.getTime(inetAddress);
                    long returntime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
                    Date time = new Date(returntime);
                    String intdate = simpleDateFormat.format(time);

                    Timestamp timestampone = Timestamp.valueOf(intdate);
                    Timestamp timestamptwo = Timestamp.valueOf(caldate);

                    timestampone.setNanos(0);
                    timestamptwo.setNanos(0);
                    timestampone.setSeconds(0);
                    timestamptwo.setSeconds(0);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(Home.this, time.toString(), Toast.LENGTH_LONG).show();
                            if(timestampone.after(timestamptwo)){
                                displayinvaliddatedialog(pdate, time);
                            }else{
                                if(alertDialog != null){
                                    alertDialog.dismiss();
                                }
                            }
                        }
                    });

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void movetoseries(String movieid, String mtype, String device, int page) {
    String url = "http://192.168.43.189/farmhousemovies/api/mainseason_and_series.php?id=" + movieid+
            "&type="+mtype+"&device="+device+"&page="+page+"&ptype=search&user="+userid;

    progressBarannex.setVisibility(View.VISIBLE);
   // Toast.makeText(Home.this, url, Toast.LENGTH_LONG).show();

        JsonArrayRequest movieReq =
                new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());

                                if(response.length()>0){
                                    // Parsing json
                                    for (int i = 0; i < response.length(); i++) {
                                        try {

                                            JSONObject obj = response.getJSONObject(i);
                                            JSONObject objcount = response.getJSONObject(0);
                                            progressBarannex.setVisibility(View.INVISIBLE);
                                            Movie movie = new Movie();
                                            titleannex.setText(objcount.getString("title"));
                                            if(obj.has("seasonname")) {
                                                movie.setSeriesName(obj.getString("seasonname"));
                                                movie.setSeriesID(obj.getString("seasonid"));
                                                movie.setPartname(obj.getString("seasonname"));
                                            }else if(obj.has("seriesname")){
                                                movie.setSeriesName(obj.getString("seriesname"));
                                                movie.setSeriesID(obj.getString("seriesid"));
                                                movie.setPartname(obj.getString("seriesname"));
                                            }
                                            movie.setTitle(obj.getString("title"));
                                            movie.setThumbnailUrl(obj.getString("image"));
                                            movie.setMovieID(obj.getString("movieID"));
                                            movie.setID(obj.getString("movieID"));
                                            movie.setMovietype(obj.getString("movietype"));
                                            remainingannex = objcount.getInt("remainingitems");
                                            curpageannex = Integer.parseInt(obj.getString("pagenum"));
                                            // adding movie to movies array
                                            movieListannex.add(movie);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }}else{

                                    listViewannex.setVisibility(View.INVISIBLE);
                                    emptyTextannex.setVisibility(View.VISIBLE);
                                    emptyTextannex.setText("No information available");
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        progressBarannex.setVisibility(View.INVISIBLE);
                        final Handler searchhandler = new Handler();
                        searchhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                emptyTextannex.setVisibility(View.VISIBLE);
                                emptyTextannex.setText("Oops, error encountered: Retry");
                                refreshbtnannex.setVisibility(View.VISIBLE);

                            }
                        }, 500);

                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    private void movetoepisodes(String movieid, String sid, String mtype, String device,
                                int page, String task) {
        if(task=="sep"){
            progressBarannex.setVisibility(View.VISIBLE);
            movieListannex.clear();
        }else{
            progressBarepi.setVisibility(View.VISIBLE);
        }


        String url = "http://192.168.43.189/farmhousemovies/api/seasons_and_series.php?id=" + movieid+
                "&seasonid="+sid+
                "&type="+mtype+"&device="+device+"&page="+page+"&ptype=search&user="+userid;

        //Toast.makeText(Home.this, url ,Toast.LENGTH_LONG).show();

        JsonArrayRequest movieReq =
                new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());

                                if(response.length()>0){
                                    // Parsing json
                                    for (int i = 0; i < response.length(); i++) {
                                        try {

                                            JSONObject obj = response.getJSONObject(i);
                                            JSONObject objcount = response.getJSONObject(0);
                                            Movie movie = new Movie();

                                            if(task=="sep"){
                                             progressBarannex.setVisibility(View.INVISIBLE);
                                            }else {
                                                progressBarepi.setVisibility(View.INVISIBLE);
                                            }

                                            if(obj.has("seasonname")) {
                                                if(task=="sep"){
                                                    titleannex.setText(objcount.getString("title") +
                                                            " - " + obj.getString("seasonname"));
                                                }else {
                                                    titleepi.setText(objcount.getString("title") + " - " + obj.getString("seasonname"));
                                                }
                                                movie.setSeriesName(obj.getString("seasonname"));
                                                movie.setSeriesID(obj.getString("seasonid"));
                                                movie.setPartname(obj.getString("partname"));
                                                movie.setSeriesShortName(obj.getString(
                                                        "seasonshortname") + " "+ obj.getString(
                                                        "episodeshortname") );

                                            }else if(obj.has("seriesname")){
                                                if(task=="sep"){
                                                    titleannex.setText(objcount.getString("title") +
                                                            " - " + obj.getString("seriesname"));
                                                }else {
                                                    titleepi.setText(objcount.getString("title") + " - " + obj.getString("seriesname"));
                                                }
                                                movie.setSeriesName(obj.getString("seriesname"));
                                                movie.setSeriesID(obj.getString("seriesid"));
                                                movie.setPartname(obj.getString("partname"));
                                                movie.setSeriesShortName(obj.getString(
                                                        "seriesshortname") + " "+ obj.getString(
                                                        "episodeshortname") );

                                            }


                                            movie.setTitle(obj.getString("title"));
                                            movie.setThumbnailUrl(obj.getString("image"));
                                            movie.setDuration(obj.getString("movieDuration"));
                                            movie.setFilename(obj.getString("moviedirectory"));
                                            movie.setID(obj.getString("moviedirectory"));
                                            movie.setDesc(obj.getString("description"));
                                            movie.setDate(obj.getString("releasedate"));
                                            movie.setpartID(obj.getString("partid"));
                                            movie.setFav(obj.getString("favstatus"));
                                            movie.setMovietype(obj.getString("movietype"));
                                            movie.setPaidstatus(obj.getString("paidstatus"));
                                            movie.setPrice(obj.getString("price"));
                                            movie.setCategory(obj.getString("category"));
                                            movie.setExpirydate(obj.getString("expirydate"));
                                            //movie.setSubscription(obj.getString(
                                                  //  "subscription_status"));
                                            if(task=="sep"){
                                                remainingannex = objcount.getInt("remainingitems");
                                                curpageannex = Integer.parseInt(obj.getString(
                                                        "pagenum"));
                                            }else {
                                                remainingepi = objcount.getInt("remainingitems");
                                                curpageepi = Integer.parseInt(obj.getString("pagenum"));
                                            }
                                            // adding movie to movies array
                                            if(task=="sep"){
                                                movieListannex.add(movie);
                                            }else {
                                                movieListannexepisodes.add(movie);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }}else {

                                    if (task == "sep") {

                                        listViewannex.setVisibility(View.INVISIBLE);
                                        emptyTextannex.setVisibility(View.VISIBLE);
                                        emptyTextannex.setText("No information available");
                                    } else {
                                        listViewannexepisodes.setVisibility(View.INVISIBLE);
                                        emptyTextepi.setVisibility(View.VISIBLE);
                                        emptyTextepi.setText("No information available");
                                    }
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                if(task=="sep"){
                                    clAdapter.notifyDataSetChanged();
                                }else {
                                    adapterannex.notifyDataSetChanged();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.d(TAG, " Error: " + error.getMessage());
                        if(task=="sep"){

                            progressBarannex.setVisibility(View.INVISIBLE);
                            final Handler searchhandler = new Handler();
                            searchhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    emptyTextannex.setVisibility(View.VISIBLE);
                                    emptyTextannex.setText("Oops, error encountered: Retry");
                                    refreshbtnannex.setVisibility(View.VISIBLE);

                                }
                            }, 500);

                        }else {
                            progressBarepi.setVisibility(View.INVISIBLE);
                            final Handler searchhandler = new Handler();
                            searchhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    emptyTextepi.setVisibility(View.VISIBLE);
                                    emptyTextepi.setText("Oops, error encountered: Retry");
                                    refreshbtnepi.setVisibility(View.VISIBLE);

                                }
                            }, 500);
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    private void begintimer(int mins) {

    }

    public void isnotvalid(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(Home.this,"Checked for new content", Toast.LENGTH_LONG).show();
                if(player.getPlayWhenReady() && player.getPlaybackState() == Player.STATE_READY ){
                  player.setPlayWhenReady(false);
                    //showpaymentdialog(videoname);
                }else if(player.getPlayWhenReady()){
                    //showpaymentdialog(videoname);
                }else{
                    //showpaymentdialog(videoname);
                }
            }
        });
    }


    private void saveDialogDetails(String movietype, String movieid, String sid, String partid,
                                   String userid, String mPrice, String mName){
        new PreferencesManager(Home.this).saveAlertDetails(movietype,movieid,sid,partid,
                userid,mPrice,mName);
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
            progressDialog = new ProgressDialog(Home.this);
            progressDialog.setMessage("Please wait...");
        }

        if (active && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        else {
            progressDialog.dismiss();
        }
    }

    private void validateEntries(String currentlistview) {

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
           setactivityname(currentlistview,"activity");
            ghMobileMoneyPresenter = new GhMobileMoneyPresenter(Home.this,
             new GhMobileMoneyFragment());

            cardPresenter = new CardPresenter(Home.this, new CardFragment());
            List<SavedPhone> checkForSavedMobileMoney = ghMobileMoneyPresenter.checkForSavedGHMobileMoney(email);
            List<SavedCard> checkForSavedCards = cardPresenter.checkForSavedCards(email);

            if (checkForSavedCards.isEmpty() && checkForSavedMobileMoney.isEmpty()){
                new thetellerManager(Home.this).setAmount(Double.parseDouble(amount))
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
                new thetellerManager(Home.this).setAmount(Double.parseDouble(amount))
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


    public void onresultreceived(String currentlistview, String result) {
        //Toast.makeText(Home.this, result, Toast.LENGTH_LONG).show();
        String finalresponse = result;

        finalcode = result.replace("{", "").replace("}", "").replace("\"", "");

        String[] strarray = finalcode.split(",");
        String scode = strarray[1];
        String[] farray = scode.split(":");
        String fcode = farray[1];
        //Toast.makeText(Home.this, fcode, Toast.LENGTH_LONG).show();

        if (Integer.parseInt(fcode) != 000) {
            //Toast.makeText(Home.this, "Failed", Toast.LENGTH_LONG).show();
            if (Integer.parseInt(fcode) == 100) {
                servermsg = "\n" + "\n" + "Transaction failed, check your balance and " +
                        "authorization " +
                        "and " +
                        "try again!";
            } else {
                servermsg = "\n" + "\n" + "Transaction failed, operation was aborted!";
            }
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(Home.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
            }
            detailsexist = true;
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
                SharedPreferences sharedPreferences = Home.this.getSharedPreferences("AlertDialogDetails",
                        Context.MODE_PRIVATE);
                String mtype = (sharedPreferences.getString("Mtype", ""));
                String mid = (sharedPreferences.getString("Mid", ""));
                String sid = (sharedPreferences.getString("Sid", ""));
                String partid = (sharedPreferences.getString("Partid", ""));
                String mprice = (sharedPreferences.getString("Mprice", ""));
                String mname = (sharedPreferences.getString("Mname", ""));


                new AsyncPay().execute(mtype, mid, partid, sid, userid, "failedpayment");
                showpaymentdialog(mtype, mid, sid, partid, userid, mprice, mname);
            }
        }

        if (Integer.parseInt(fcode) == 000) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(Home.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
            }

            movetonextscreen = true;
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
                SharedPreferences sharedPreferences = Home.this.getSharedPreferences("AlertDialogDetails",
                        Context.MODE_PRIVATE);
                String mtype = (sharedPreferences.getString("Mtype", ""));
                String mid = (sharedPreferences.getString("Mid", ""));
                String sid = (sharedPreferences.getString("Sid", ""));
                String partid = (sharedPreferences.getString("Partid", ""));
                String mprice = (sharedPreferences.getString("Mprice", ""));
                String mname = (sharedPreferences.getString("Mname", ""));


                showpaymentdialog(mtype, mid, sid, partid, userid, mprice, mname);
                new AsyncPay().execute(mtype, mid, partid, sid, userid, "successfulpayment");
            }
        }
    }


        private void showpaymentdialog(String movietype, String movieid, String sid, String partid,
                                   String userid, String mPrice,String mName) {

            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            saveDialogDetails(mtype, movieid, sid,partid,userid,mPrice,mName);
        
        AlertDialog.Builder adb = new AlertDialog.Builder(Home.this);
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
                ((LayoutInflater)Home.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.texthead,null,false);
        adb.setCustomTitle(titleview);


        TextView headmsg = mydialog.findViewById(R.id.txtreason);
        if(resExists) {
            headmsg.setVisibility(View.VISIBLE);
            headmsg.setText("Your subscription has expired, please renew it to continue");
        }
        relativeLayoutone = mydialog.findViewById(R.id.rl);
        gridLayout = mydialog.findViewById(R.id.gl);
        amount = mydialog.findViewById(R.id.mcost);
        message = mydialog.findViewById(R.id.pmsg);
        movietxt = mydialog.findViewById(R.id.dmtitle);
        submit = mydialog.findViewById(R.id.sendbtn);
        closediagbtn = mydialog.findViewById(R.id.donebtn);
        voucher = mydialog.findViewById(R.id.etVoucher);
        num = mydialog.findViewById(R.id.phonenum);
        wallets = mydialog.findViewById(R.id.spinMoMo);
        hidspin = mydialog.findViewById(R.id.hidspin);
        progressBarone = mydialog.findViewById(R.id.sBar);
        retrybtn = mydialog.findViewById(R.id.retrybtn);
        pnote = mydialog.findViewById(R.id.pnote);
        vul = mydialog.findViewById(R.id.txtvoucher);

        retrybtn.setVisibility(View.GONE);
        voucher.setVisibility(View.GONE);
        relativeLayoutone.setVisibility(View.GONE);

        pnote.setText("NB: Subscription is valid for 7 days");


            final Handler alerthandler = new Handler();
            alerthandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(detailsexist){
                        pnote.append(servermsg);
                    }
                    if(movetonextscreen){
                        pstatus = "success";
                        status = "success";
                        message.setText("Payment Successful!");
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        closediagbtn.setVisibility(View.VISIBLE);
                        gridLayout.setVisibility(View.INVISIBLE);
                    }
                }
            }, 500);
        
        this.arrayspMoMo = new String[]{"-- Select Wallet --","mtn","airteltigo","vodafone"};

        ArrayAdapter<String> adapterNCD = new ArrayAdapter<String>(Home.this,
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
                    validateEntries(currentlistview);
                    showProgressIndicator(false);

                }

            });


            //amount.setText(mPrice+" p");
        movietxt.setText(mName);




        voucher.setVisibility(View.INVISIBLE);


        closediagbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                alertDialog.cancel();
                alertDialog.dismiss();
                detailsexist = false;
                movetonextscreen = false;
                SharedPreferences nsp = Home.this.getSharedPreferences("AlertDialogDetails",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor neditor = nsp.edit();
                neditor.clear();
                neditor.apply();


                if(alreadyplaying){
                    if(status == "success") {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        currentdate = sdf.format(date);
                        cal.add(Calendar.DAY_OF_YEAR, 7);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date finaldate = cal.getTime();
                        expidate = df.format(finaldate);
                        starttracking(expidate);

                         final Handler searchhandler = new Handler();
                        searchhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                player.setPlayWhenReady(true);
                            }
                        }, 300);
                    }

                    if(frag != null){
                        refreshfragsetfav(frag,listposition);
                    }
                }else{
                    if(currentlistview=="listView"){
                        index = listView.getFirstVisiblePosition();
                        View v = listView.getChildAt(0);
                        vpos = (v== null) ? 0 : (v.getTop() - listView.getPaddingTop());
                        int totalitems = (listView.getAdapter().getCount())- 1;
                        getrefreshedrecords(totalitems, "listView", "adapter");
                    } else if(currentlistview == "listViewannex"){
                        index = listViewannex.getFirstVisiblePosition();
                        View v = listViewannex.getChildAt(0);
                        vpos = (v== null) ? 0 : (v.getTop() - listViewannex.getPaddingTop());
                        int totalitems = (listViewannex.getAdapter().getCount())- 1;
                        getrefreshedrecords(totalitems, "listViewannex", "clAdapter");
                    } else if(currentlistview == "listViewannexepisodes"){
                        index = listViewannexepisodes.getFirstVisiblePosition();
                        View v = listViewannexepisodes.getChildAt(0);
                        vpos = (v== null) ? 0 : (v.getTop() - listViewannexepisodes.getPaddingTop());
                        int totalitems = (listViewannexepisodes.getAdapter().getCount())- 1;
                        getrefreshedrecords(totalitems,"listViewannexepisodes" , "adapterannex");
                    }
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
                    relativeLayoutone.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.GONE);
                    closediagbtn.setVisibility(View.GONE);
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
                    SharedPreferences nsp = Home.this.getSharedPreferences("AlertDialogDetails",
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

    private void getrefreshedrecords(int totalitems, String lv, String ladapter) {
        if(status == "success"){

            if(lv == "listView"){
             movieList.clear();
             String url =
                     "http://192.168.43.189/farmhousemovies/api/getrefreshedsearchrecords.php?q="+textquery+
                     "&user="+userid+"&items="+totalitems;

                JsonArrayRequest movieReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());

                                if(response.length() > 0) {
                                    // Parsing json
                                    for (int i = 0; i < response.length(); i++) {
                                        try {

                                            JSONObject obj = response.getJSONObject(i);
                                            JSONObject objcount = response.getJSONObject(0);

                                            Movie movie = new Movie();
                                            if(obj.has("moviedirectory")){

                                                movie.setTitle(obj.getString("title"));
                                                movie.setThumbnailUrl(obj.getString("image"));
                                                movie.setDuration(obj.getString("movieDuration"));
                                                movie.setFilename(obj.getString("moviedirectory"));
                                                movie.setID(obj.getString("moviedirectory"));
                                                movie.setFav(obj.getString("favstatus"));
                                                movie.setMovietype(obj.getString("movietype"));
                                                movie.setDate(obj.getString("releasedate"));
                                                movie.setDesc(obj.getString("description"));
                                                // movie.setSubscription(obj.getString
                                                // ("subscription_status"));
                                                remaining = objcount.getInt("remainingitems");

                                                if(obj.has("seasonname")) {
                                                    movie.setSeriesName(obj.getString("seasonname"));
                                                    movie.setSeriesID(obj.getString("seasonid"));
                                                    movie.setPartname(obj.getString("seasonname"));
                                                }else if(obj.has("seriesname")){
                                                    movie.setSeriesName(obj.getString("seriesname"));
                                                    movie.setSeriesID(obj.getString("seriesid"));
                                                    movie.setPartname(obj.getString("seriesname"));
                                                }
                                                movie.setpartID(obj.getString("partid"));
                                                if(obj.has("category")){
                                                    movie.setPaidstatus(obj.getString("paidstatus"));
                                                    movie.setPrice(obj.getString("price"));
                                                    movie.setCategory(obj.getString("category"));
                                                }
                                            }else{
                                                if(obj.getString("title").contains("Season") && obj.getString(
                                                        "movietype").equalsIgnoreCase("Season")){
                                                    movie.setSeriesID(obj.getString("seasonid"));
                                                }else if(obj.getString("title").contains("Season") && obj.getString("movietype").equalsIgnoreCase("Series")){
                                                    movie.setSeriesID(obj.getString("seriesid"));
                                                }
                                                movie.setTitle(obj.getString("title"));
                                                movie.setThumbnailUrl(obj.getString("image"));
                                                movie.setID(obj.getString("movieID"));
                                                movie.setMovietype(obj.getString("movietype"));
                                                remaining = objcount.getInt("remainingitems");



                                            }
                                            movieList.add(movie);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    srel.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.INVISIBLE);
                                    clearbtn.setVisibility(View.GONE);
                                    annexrel.setVisibility(View.GONE);
                                    epirel.setVisibility(View.GONE);
                                    final Handler searchhandler = new Handler();
                                    searchhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                text = Html.fromHtml(textquery,
                                                        Html.FROM_HTML_MODE_LEGACY).toString();
                                            } else {
                                                text = Html.fromHtml(textquery).toString();
                                            }

                                            emptymsg.setText(text+" did not match any result.");

                                        }
                                    }, 500);
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
                                emptymsg.setVisibility(View.VISIBLE);
                                emptymsg.setText("Oops, error encountered: Retry");
                                refreshbtn.setVisibility(View.VISIBLE);

                            }
                        }, 500);

                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(movieReq);

            }
            if(lv == "listViewannex"){
            movieListannex.clear();

            if(request =="part"){
                String url = "http://192.168.43.189/farmhousemovies/api/getrefreshedsearchrecords.php?id=" + movieid+"&type=Single&user="+userid+"&items="+totalitems;

                JsonArrayRequest movieReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        JSONObject objcount = response.getJSONObject(0);
                                        Movie movie = new Movie();
                                        movie.setTitle(obj.getString("title"));
                                        movie.setPartname(obj.getString("title")+" - "+obj.getString("partname"));
                                        movie.setThumbnailUrl(obj.getString("image"));
                                        movie.setDuration(obj.getString("movieDuration"));
                                        movie.setFilename(obj.getString("moviedirectory"));
                                        movie.setID(obj.getString("movieID"));
                                        movie.setDesc(obj.getString("description"));
                                        movie.setDate(obj.getString("releasedate"));
                                        movie.setpartID(obj.getString("partid"));
                                        movie.setFav(obj.getString("favstatus"));
                                        movie.setCategory(obj.getString("category"));
                                        movie.setPrice(obj.getString("price"));
                                        movie.setPaidstatus(obj.getString("paidstatus"));
                                        movie.setMovietype(obj.getString("movietype"));
                                        //movie.setSubscription(obj.getString("subscription_status"));
                                        remainingannex = objcount.getInt("remainingitems");

                                        // adding movie to movies array
                                        movieListannex.add(movie);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                clAdapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        progressBarannex.setVisibility(View.INVISIBLE);
                        final Handler searchhandler = new Handler();
                        searchhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                emptyTextannex.setVisibility(View.VISIBLE);
                                emptyTextannex.setText("Oops, error encountered: Retry");
                                refreshbtnannex.setVisibility(View.VISIBLE);

                            }
                        }, 500);

                    }
                });
                AppController.getInstance().addToRequestQueue(movieReq);

            }else{


                String url = "http://192.168.43.189/farmhousemovies/api/getrefreshedsearchrecords.php?id=" + movieid+"&type="+mtype+"&user="+userid+"&items="+totalitems;

                JsonArrayRequest movieReq =
                        new JsonArrayRequest(url,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        Log.d(TAG, response.toString());

                                        if(response.length()>0){
                                            // Parsing json
                                            for (int i = 0; i < response.length(); i++) {
                                                try {

                                                    JSONObject obj = response.getJSONObject(i);
                                                    JSONObject objcount = response.getJSONObject(0);
                                                    Movie movie = new Movie();

                                                    if(req=="sep"){
                                                        progressBarannex.setVisibility(View.INVISIBLE);
                                                    }else {
                                                        progressBarepi.setVisibility(View.INVISIBLE);
                                                    }

                                                    if(obj.has("seasonname")) {
                                                        if(req=="sep"){
                                                            titleannex.setText(objcount.getString("title") +
                                                                    " - " + obj.getString("seasonname"));
                                                        }else {
                                                            titleepi.setText(objcount.getString("title") + " - " + obj.getString("seasonname"));
                                                        }
                                                        movie.setSeriesName(obj.getString("seasonname"));
                                                        movie.setSeriesID(obj.getString("seasonid"));
                                                        movie.setPartname(obj.getString("partname"));
                                                        movie.setSeriesShortName(obj.getString(
                                                                "seasonshortname") + " "+ obj.getString(
                                                                "episodeshortname") );

                                                    }else if(obj.has("seriesname")){
                                                        if(req=="sep"){
                                                            titleannex.setText(objcount.getString("title") +
                                                                    " - " + obj.getString("seriesname"));
                                                        }else {
                                                            titleepi.setText(objcount.getString("title") + " - " + obj.getString("seriesname"));
                                                        }
                                                        movie.setSeriesName(obj.getString("seriesname"));
                                                        movie.setSeriesID(obj.getString("seriesid"));
                                                        movie.setPartname(obj.getString("partname"));
                                                        movie.setSeriesShortName(obj.getString(
                                                                "seriesshortname") + " "+ obj.getString(
                                                                "episodeshortname") );

                                                    }


                                                    movie.setTitle(obj.getString("title"));
                                                    movie.setThumbnailUrl(obj.getString("image"));
                                                    movie.setDuration(obj.getString("movieDuration"));
                                                    movie.setFilename(obj.getString("moviedirectory"));
                                                    movie.setID(obj.getString("moviedirectory"));
                                                    movie.setDesc(obj.getString("description"));
                                                    movie.setDate(obj.getString("releasedate"));
                                                    movie.setpartID(obj.getString("partid"));
                                                    movie.setFav(obj.getString("favstatus"));
                                                    movie.setMovietype(obj.getString("movietype"));
                                                    movie.setPaidstatus(obj.getString("paidstatus"));
                                                    movie.setPrice(obj.getString("price"));
                                                    movie.setCategory(obj.getString("category"));
                                                    //movie.setSubscription(obj.getString(
                                                    //  "subscription_status"));
                                                    if(req=="sep"){
                                                        remainingannex = objcount.getInt("remainingitems");

                                                    }else {
                                                        remainingepi = objcount.getInt("remainingitems");

                                                    }
                                                    // adding movie to movies array
                                                    if(req=="sep"){
                                                        movieListannex.add(movie);
                                                    }else {
                                                        movieListannexepisodes.add(movie);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }}else {

                                            if (req == "sep") {

                                                listViewannex.setVisibility(View.INVISIBLE);
                                                emptyTextannex.setVisibility(View.VISIBLE);
                                                emptyTextannex.setText("No information available");
                                            } else {
                                                listViewannexepisodes.setVisibility(View.INVISIBLE);
                                                emptyTextepi.setVisibility(View.VISIBLE);
                                                emptyTextepi.setText("No information available");
                                            }
                                        }

                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        if(req=="sep"){
                                            clAdapter.notifyDataSetChanged();
                                        }else {
                                            adapterannex.notifyDataSetChanged();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                Log.d(TAG, " Error: " + error.getMessage());
                                if(req=="sep"){

                                    progressBarannex.setVisibility(View.INVISIBLE);
                                    final Handler searchhandler = new Handler();
                                    searchhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            emptyTextannex.setVisibility(View.VISIBLE);
                                            emptyTextannex.setText("Oops, error encountered: Retry");
                                            refreshbtnannex.setVisibility(View.VISIBLE);

                                        }
                                    }, 500);

                                }else {
                                    progressBarepi.setVisibility(View.INVISIBLE);
                                    final Handler searchhandler = new Handler();
                                    searchhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            emptyTextepi.setVisibility(View.VISIBLE);
                                            emptyTextepi.setText("Oops, error encountered: Retry");
                                            refreshbtnepi.setVisibility(View.VISIBLE);

                                        }
                                    }, 500);
                                }
                            }
                        });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(movieReq);

            }

            }

            if(lv ==  "listViewannexepisodes"){
            movieListannexepisodes.clear();

                String url = "http://192.168.43.189/farmhousemovies/api/getrefreshedsearchrecords.php?id=" + movieid+"&type="+mtype+"&user="+userid+"&items="+totalitems;


                JsonArrayRequest movieReq =
                        new JsonArrayRequest(url,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        Log.d(TAG, response.toString());

                                        if(response.length()>0){
                                            // Parsing json
                                            for (int i = 0; i < response.length(); i++) {
                                                try {

                                                    JSONObject obj = response.getJSONObject(i);
                                                    JSONObject objcount = response.getJSONObject(0);
                                                    Movie movie = new Movie();

                                                    if(req=="sep"){
                                                        progressBarannex.setVisibility(View.INVISIBLE);
                                                    }else {
                                                        progressBarepi.setVisibility(View.INVISIBLE);
                                                    }

                                                    if(obj.has("seasonname")) {
                                                        if(req=="sep"){
                                                            titleannex.setText(objcount.getString("title") +
                                                                    " - " + obj.getString("seasonname"));
                                                        }else {
                                                            titleepi.setText(objcount.getString("title") + " - " + obj.getString("seasonname"));
                                                        }
                                                        movie.setSeriesName(obj.getString("seasonname"));
                                                        movie.setSeriesID(obj.getString("seasonid"));
                                                        movie.setPartname(obj.getString("partname"));
                                                        movie.setSeriesShortName(obj.getString(
                                                                "seasonshortname") + " "+ obj.getString(
                                                                "episodeshortname") );

                                                    }else if(obj.has("seriesname")){
                                                        if(req=="sep"){
                                                            titleannex.setText(objcount.getString("title") +
                                                                    " - " + obj.getString("seriesname"));
                                                        }else {
                                                            titleepi.setText(objcount.getString("title") + " - " + obj.getString("seriesname"));
                                                        }
                                                        movie.setSeriesName(obj.getString("seriesname"));
                                                        movie.setSeriesID(obj.getString("seriesid"));
                                                        movie.setPartname(obj.getString("partname"));
                                                        movie.setSeriesShortName(obj.getString(
                                                                "seriesshortname") + " "+ obj.getString(
                                                                "episodeshortname") );

                                                    }


                                                    movie.setTitle(obj.getString("title"));
                                                    movie.setThumbnailUrl(obj.getString("image"));
                                                    movie.setDuration(obj.getString("movieDuration"));
                                                    movie.setFilename(obj.getString("moviedirectory"));
                                                    movie.setID(obj.getString("moviedirectory"));
                                                    movie.setDesc(obj.getString("description"));
                                                    movie.setDate(obj.getString("releasedate"));
                                                    movie.setpartID(obj.getString("partid"));
                                                    movie.setFav(obj.getString("favstatus"));
                                                    movie.setMovietype(obj.getString("movietype"));
                                                    movie.setPaidstatus(obj.getString("paidstatus"));
                                                    movie.setPrice(obj.getString("price"));
                                                    movie.setCategory(obj.getString("category"));
                                                    //movie.setSubscription(obj.getString(
                                                    //  "subscription_status"));
                                                    if(req=="sep"){
                                                        remainingannex = objcount.getInt("remainingitems");
                                                    }else {
                                                        remainingepi = objcount.getInt("remainingitems");
                                                    }
                                                    // adding movie to movies array
                                                    if(req=="sep"){
                                                        movieListannex.add(movie);
                                                    }else {
                                                        movieListannexepisodes.add(movie);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }}else {

                                            if (req == "sep") {

                                                listViewannex.setVisibility(View.INVISIBLE);
                                                emptyTextannex.setVisibility(View.VISIBLE);
                                                emptyTextannex.setText("No information available");
                                            } else {
                                                listViewannexepisodes.setVisibility(View.INVISIBLE);
                                                emptyTextepi.setVisibility(View.VISIBLE);
                                                emptyTextepi.setText("No information available");
                                            }
                                        }

                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        if(req=="sep"){
                                            clAdapter.notifyDataSetChanged();
                                        }else {
                                            adapterannex.notifyDataSetChanged();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                Log.d(TAG, " Error: " + error.getMessage());
                                if(req=="sep"){

                                    progressBarannex.setVisibility(View.INVISIBLE);
                                    final Handler searchhandler = new Handler();
                                    searchhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            emptyTextannex.setVisibility(View.VISIBLE);
                                            emptyTextannex.setText("Oops, error encountered: Retry");
                                            refreshbtnannex.setVisibility(View.VISIBLE);

                                        }
                                    }, 500);

                                }else {
                                    progressBarepi.setVisibility(View.INVISIBLE);
                                    final Handler searchhandler = new Handler();
                                    searchhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            emptyTextepi.setVisibility(View.VISIBLE);
                                            emptyTextepi.setText("Oops, error encountered: Retry");
                                            refreshbtnepi.setVisibility(View.VISIBLE);

                                        }
                                    }, 500);
                                }
                            }
                        });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(movieReq);
            }

            if(ladapter == "adapter"){
                adapter.notifyDataSetChanged();
            }
            if(ladapter == "clAdapter"){
                clAdapter.notifyDataSetChanged();
            }
            if(ladapter == "adapterannex"){
                adapterannex.notifyDataSetChanged();
            }

        }
    }

    private void setupSpinners() {
        wallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 3) {
                    voucher.setVisibility(View.VISIBLE);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
                    alertDialogBuilder.setTitle("Follow the steps to generate your voucher code");
                    alertDialogBuilder.setMessage("1.Dial *110#\n2.Select option 6 (Generate Voucher)\n3.You will receive an SMS with 6 digits code\n\nNB: The code is active within 5 minutes.");
                    alertDialogBuilder.setPositiveButton("Alright",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(Home.this, "Thank You", Toast.LENGTH_SHORT).show();
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

    private void checkvalidity() {
        Toast.makeText(Home.this, videoname, Toast.LENGTH_LONG).show();
    }

    private void updateprogressbar() {
        videoRunnable = new Runnable() {
            @Override
            public void run() {


                long position = player.getCurrentPosition();

                    seekBar.setProgress((int) (player.getCurrentPosition()*100/player.getDuration()));

                    dseekBar.setProgress((int) (player.getCurrentPosition()*100/player.getDuration()));


                if(player.isPlaying()){
                    videoHandler.postDelayed(this, 0);
                }

                starttime.setText("" + convertintotime((int) position));
                endtime.setText("-" + convertintotime((int) (player.getDuration() - position)));

                dstarttime.setText("" + convertintotime((int) position));
                dendtime.setText("-" + convertintotime((int) (player.getDuration() - position)));

            }
        };
        videoHandler.postDelayed(videoRunnable, 0);
    }


    private void savePartDetails(String partid, String partdate, String moviedesc,
                                 String favistatus, String mtype, String sid) {
        fav = favistatus;
        new PreferencesManager(Home.this).savePartDetails(partid,partdate,moviedesc,favistatus,
                mtype, sid);
        // Toast.makeText(Home.this, partid+" "+partdate+" "+moviedesc+" "+favistatus,
        // Toast.LENGTH_LONG ).show();

        mviewPager.getAdapter().notifyDataSetChanged();


    }





    private void movetoparts(String movieid, String device, int pagenum) {

        String urlresult = "http://192.168.43.189/farmhousemovies/api/movieparts.php?id=" + movieid+
                "&user="+userid+"&device="+device+"&page="+pagenum+"&ptype=search";

        JsonArrayRequest movieReq = new JsonArrayRequest(urlresult,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                JSONObject objcount = response.getJSONObject(0);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("title"));
                                movie.setPartname(obj.getString("title")+" - "+obj.getString("partname"));
                                movie.setThumbnailUrl(obj.getString("image"));
                                movie.setDuration(obj.getString("movieDuration"));
                                movie.setFilename(obj.getString("moviedirectory"));
                                movie.setID(obj.getString("movieID"));
                                movie.setDesc(obj.getString("description"));
                                movie.setDate(obj.getString("releasedate"));
                                movie.setpartID(obj.getString("partid"));
                                movie.setFav(obj.getString("favstatus"));
                                movie.setCategory(obj.getString("category"));
                                movie.setPrice(obj.getString("price"));
                                movie.setPaidstatus(obj.getString("paidstatus"));
                                movie.setMovietype(obj.getString("movietype"));
                                movie.setExpirydate(obj.getString("expirydate"));
                                //movie.setSubscription(obj.getString("subscription_status"));
                                remainingannex = objcount.getInt("remainingitems");
                                curpageannex = Integer.parseInt(obj.getString("pagenum"));
                                // adding movie to movies array
                                movieListannex.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        clAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressBarannex.setVisibility(View.INVISIBLE);
                final Handler searchhandler = new Handler();
                searchhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyTextannex.setVisibility(View.VISIBLE);
                        emptyTextannex.setText("Oops, error encountered: Retry");
                        refreshbtnannex.setVisibility(View.VISIBLE);

                    }
                }, 500);

            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }



    private void getSearchResults(String textquery) {

            //Toast.makeText(Home.this, query, Toast.LENGTH_LONG).show();
            final Handler searchhandler = new Handler();
            searchhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    movieList.clear();
                    movieListannex.clear();
                    movieListannexepisodes.clear();
                    listView.invalidateViews();
                    listViewannex.invalidateViews();
                    listViewannexepisodes.invalidateViews();
                    annexrel.setVisibility(View.GONE);
                    epirel.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    if(Home.this.getBaseContext().getPackageManager().hasSystemFeature("com" +
                            ".google" +
                            ".android.tv")){
                        getresultfromserver(textquery, "tv",curpage);
                    }else{
                        getresultfromserver(textquery,"other", curpage);
                    }

                }
            }, 1000);


    }

    private void getresultfromserver(String textquery, String device, int pagenum) {
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        emptymsg.setVisibility(View.VISIBLE);
        emptymsg.setText("");
        movieList.clear();

        JsonArrayRequest movieReq = new JsonArrayRequest("http://192.168.43.189/farmhousemovies/api/search" +
                ".php?q="+textquery+"&device="+device+"&page="+pagenum+"&user="+userid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if(response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    JSONObject objcount = response.getJSONObject(0);

                                    progressBar.setVisibility(View.GONE);
                                    emptymsg.setVisibility(View.INVISIBLE);
                                    clearbtn.setVisibility(View.VISIBLE);
                                    srel.setVisibility(View.VISIBLE);
                                    Movie movie = new Movie();
                                    if(obj.has("moviedirectory")){

                                        movie.setTitle(obj.getString("title"));
                                        movie.setThumbnailUrl(obj.getString("image"));
                                        movie.setDuration(obj.getString("movieDuration"));
                                        movie.setFilename(obj.getString("moviedirectory"));
                                        movie.setID(obj.getString("moviedirectory"));
                                        movie.setFav(obj.getString("favstatus"));
                                        movie.setMovietype(obj.getString("movietype"));
                                        movie.setDate(obj.getString("releasedate"));
                                        movie.setDesc(obj.getString("description"));
                                        movie.setExpirydate(obj.getString("expirydate"));
                                        // movie.setSubscription(obj.getString
                                        // ("subscription_status"));
                                        remaining = objcount.getInt("remainingitems");
                                        curpage = Integer.parseInt(obj.getString("pagenum"));
                                        if(obj.has("seasonname")) {
                                            movie.setSeriesName(obj.getString("seasonname"));
                                            movie.setSeriesID(obj.getString("seasonid"));
                                            movie.setPartname(obj.getString("seasonname"));
                                        }else if(obj.has("seriesname")){
                                            movie.setSeriesName(obj.getString("seriesname"));
                                            movie.setSeriesID(obj.getString("seriesid"));
                                            movie.setPartname(obj.getString("seriesname"));
                                        }
                                        movie.setpartID(obj.getString("partid"));
                                        if(obj.has("category")){
                                            movie.setPaidstatus(obj.getString("paidstatus"));
                                            movie.setPrice(obj.getString("price"));
                                            movie.setCategory(obj.getString("category"));
                                        }
                                    }else{
                                        if(obj.getString("title").contains("Season") && obj.getString(
                                                "movietype").equalsIgnoreCase("Season")){
                                            movie.setSeriesID(obj.getString("seasonid"));
                                        }else if(obj.getString("title").contains("Season") && obj.getString("movietype").equalsIgnoreCase("Series")){
                                            movie.setSeriesID(obj.getString("seriesid"));
                                        }
                                        movie.setTitle(obj.getString("title"));
                                        movie.setThumbnailUrl(obj.getString("image"));
                                        movie.setID(obj.getString("movieID"));
                                        movie.setMovietype(obj.getString("movietype"));
                                        remaining = objcount.getInt("remainingitems");
                                        curpage = Integer.parseInt(obj.getString("pagenum"));


                                    }
                    movieList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            srel.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            clearbtn.setVisibility(View.GONE);
                            annexrel.setVisibility(View.GONE);
                            epirel.setVisibility(View.GONE);
                            final Handler searchhandler = new Handler();
                            searchhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        text = Html.fromHtml(textquery,
                                                Html.FROM_HTML_MODE_LEGACY).toString();
                                    } else {
                                        text = Html.fromHtml(textquery).toString();
                                    }

                                    emptymsg.setText(text+" did not match any result.");

                                }
                            }, 500);
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
                        emptymsg.setVisibility(View.VISIBLE);
                        emptymsg.setText("Oops, error encountered: Retry");
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

    private void initmediaplayer() {
        menuLayout.setVisibility(View.GONE);
        if(isTime) {
            timer.cancel();
            timerTask.cancel();
            isTime = false;
        }
        //videoname = videofilename;
        if (player != null) {
            //player = null;
        } else {
            player = ExoPlayerFactory.newSimpleInstance(this);
        }
            fullcontrolView.setPlayer(player);
            controlView.setVisibility(View.VISIBLE);
            videoView.setPlayer(player);
            controlView.setPlayer(player);
            playView.setPlayer(player);
            player.setPlayWhenReady(playwhenready);
            Uri uri = Uri.parse(videoname);
            MediaSource mediaSource = buildMediaSource(uri);
            player.seekTo(currentwindow, playbackposition);
            player.prepare(mediaSource, false, false);
            player.addListener(eventListener);


        if(fav.contains("1")){
            setfav.setBackgroundResource(R.drawable.ic_favourite_checked);
        }else{
            setfav.setBackgroundResource(R.drawable.ic_favourite_unchecked);
        }

        if(subscription.equalsIgnoreCase("subscribed")){
          subscribebtn.setBackgroundResource(R.drawable.ic_subscribed);
        }else{
            subscribebtn.setBackgroundResource(R.drawable.ic_unsubscribed);
        }

    }




    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory datasourcefactory = new DefaultDataSourceFactory(this, "exoplayer" +
                "-codelab");
        return  new ProgressiveMediaSource.Factory(datasourcefactory).createMediaSource(uri);
    }


    private void saveMovieDetails(String mvn, String mvtitle){
        new PreferencesManager(Home.this).saveMovieDetails(mvn, mvtitle);
    }


   private void releaseplayer(){
        if(player != null) {
            playwhenready = player.getPlayWhenReady();
            playbackposition = player.getContentPosition();
            currentwindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
            if (isTime) {
                timerTask.cancel();
                timer.cancel();
                isTime = false;
            }
        }
   }

   @Override
   public void onStart(){
        super.onStart();

       if(videoname != null) {
           initmediaplayer();
       }
   }

    @Override
    protected void onResume(){
        int uioptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE ;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        checkdate();
        //new AsyncCheckDate().execute();

        super.onResume();
        if(videoname != null) {
            initmediaplayer();
        }
    }

    @Override
    protected void onPause(){
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
    public void onBackPressed(){
        if(!exitdiag){
            super.onBackPressed();
        }
       
    }

    @Override
    public void  onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){

         getWindow().getDecorView().setSystemUiVisibility(
                 View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
         );

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
    public void onFragmentInteraction(String data, String title, String path, String mpartid,
                                      String partdate, String moviedesc, String favstatus,
                                      String mtype, String msid, String subscribe, String mid,
                                      String lfrag, int lpos, String expiryDate, String mprice) {
        expidate = expiryDate;
        final Handler searchhandler = new Handler();
        searchhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                starttracking(expidate);
            }
        }, 2000);
        subscription = subscribe;
        movieid = mid;
        frag = lfrag;
        listposition = lpos;
        movietype = mtype;
        movieid = mid;
        sid = msid;
        partid = mpartid;
        meprice = mprice;
        sname = title;
        selmovieid.setText(mid);
        saveMovieDetails(path,title);
        savePartDetails(mpartid,partdate,moviedesc, favstatus, mtype, msid);
        //Toast.makeText(Home.this, partid, Toast.LENGTH_LONG).show();
        hiddenone.setText(mpartid);
        hiddentwo.setText(favstatus);
        //Toast.makeText(Home.this, partid + " "+ favstatus, Toast.LENGTH_LONG).show();

        selmovieid.setText(mid);
        selectedid.setText(mpartid);
        selectedsid.setText(msid);
        selectedtype.setText(mtype);
        pausing = false;
        videoname = path;
        String existinginfo;
        imageView.setVisibility(View.GONE);
        //Toast.makeText(Home.this, path, Toast.LENGTH_LONG).show();
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        titletext.setVisibility(View.VISIBLE);
       playlayout.setVisibility(View.VISIBLE);
        //mediaPlayer.start();

        if(titletext.getVisibility() == View.VISIBLE){
            existinginfo = titletext.getText().toString();
            if(existinginfo.equals(title) && titletext != null){
                //don't reload video
                return;
            }else{

            }
        }

        new AsyncRecordView().execute(mpartid,mtype,msid);
        titletext.setText(title);
        mtitle.setText(title);
        videoname = path;
        playbackposition = 0;
        initmediaplayer();

    }



    @Override
    public void onFragmentMostViewedInteraction(String data, String title, String path,
                                                String mpartid, String partdate, String moviedesc,
                                                String favstatus, String mtype, String msid,
                                                String mid, String subscribe, String lfrag, int lpos, String expiryDate, String mprice) {

        SharedPreferences rsp = getSharedPreferences("RecordDetails", Context.MODE_PRIVATE);
        String movname = (rsp.getString("Mname", ""));
        String sesname = (rsp.getString("Sname", ""));
        String epname = (rsp.getString("Ename", ""));

        final Handler recordhandler = new Handler();
        recordhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new AsyncRecordView().execute(mpartid,mtype, msid, mid, movname, sesname, epname,
                        userid);
                
            }
        }, 500);



        fav = favstatus;
        subscription = subscribe;
        movieid = mid;
        frag = lfrag;
        listposition = lpos;
        movietype = mtype;
        movieid = mid;
        sid = msid;
        partid = mpartid;
        meprice = mprice;
        sname = title;
        expidate = expiryDate;
        final Handler searchhandler = new Handler();
        searchhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                starttracking(expidate);
            }
        }, 2000);
        saveMovieDetails(path,title);
        //new AsyncRecordView().execute(mpartid,mtype,msid);
        savePartDetails(mpartid,partdate,moviedesc, favstatus, mtype, msid);
        selectedid.setText(mpartid);
        selectedsid.setText(msid);
        selmovieid.setText(mid);
        selectedtype.setText(mtype);
        pausing = false;
        videoname = path;
        String existinginfo;
        imageView.setVisibility(View.GONE);
        //Toast.makeText(Home.this, path, Toast.LENGTH_LONG).show();
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        titletext.setVisibility(View.VISIBLE);
       playlayout.setVisibility(View.VISIBLE);
        //mediaPlayer.start();

        if(titletext.getVisibility() == View.VISIBLE){
            existinginfo = titletext.getText().toString();
            if(existinginfo.equalsIgnoreCase(title) && titletext != null){
                //don't reload video
                return;
            }else{

            }
        }


        titletext.setText(title);
        mtitle.setText(title);
        videoname = path;
        playbackposition = 0;
        initmediaplayer();

    }

    @Override
    public void onRequestMostViewed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mfrag != null){
                            mfrag.reloadview();
                        }

                    }
                }, 7000);
            }
        });

    }





    @Override
    public void onseasonFragmentInteraction(String data, String title, String path, String mpartid
            , String partdate, String moviedesc, String favstatus, String mtype, String msid,
                                            String subscribe, String mid, String lfrag, int lpos, String expiryDate, String mprice) {

        SharedPreferences rsp = getSharedPreferences("RecordDetails", Context.MODE_PRIVATE);
        String movname = (rsp.getString("Mname", ""));
        String sesname = (rsp.getString("Sname", ""));
        String epname = (rsp.getString("Ename", ""));

        final Handler recordhandler = new Handler();
        recordhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new AsyncRecordView().execute(mpartid,mtype, msid, mid, movname, sesname, epname,
                        userid);

            }
        }, 500);



        fav = favstatus;
        subscription = subscribe;
        movieid = mid;
        frag = lfrag;
        listposition = lpos;
        movietype = mtype;
        movieid = mid;
        sid = msid;
        partid = mpartid;
        meprice = mprice;
        sname = title;
        expidate = expiryDate;


        final Handler searchhandler = new Handler();
        searchhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                starttracking(expidate);
            }
        }, 2000);

        saveMovieDetails(path,title);

        //Toast.makeText(Home.this, partid, Toast.LENGTH_LONG).show();
        hiddenone.setText(mpartid);
        hiddentwo.setText(favstatus);
       // Toast.makeText(Home.this, partid + "\n "+ favstatus, Toast.LENGTH_LONG).show();

        savePartDetails(mpartid,partdate,moviedesc, favstatus, mtype, msid);
        selectedid.setText(mpartid);
        selmovieid.setText(mid);
        selectedsid.setText(msid);
        selectedtype.setText(mtype);
        pausing = false;
        videoname = path;
        String existinginfo;
        imageView.setVisibility(View.GONE);
        //Toast.makeText(Home.this, path, Toast.LENGTH_LONG).show();
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        titletext.setVisibility(View.VISIBLE);
       playlayout.setVisibility(View.VISIBLE);
        //mediaPlayer.start();

        if(titletext.getVisibility() == View.VISIBLE){
            existinginfo = titletext.getText().toString();
            if(existinginfo.equalsIgnoreCase(title) && titletext != null){
                //don't reload video
                return;
            }else{

            }
        }

       // new AsyncRecordView().execute(mpartid,mtype, msid);
        titletext.setText(title);
        mtitle.setText(title);
        videoname = path;
        playbackposition = 0;
        initmediaplayer();
    }

    @Override
    public void onteleFragmentInteraction(String data, String title, String path, String mpartid,
                                          String partdate, String moviedesc, String favstatus,
                                          String mtype, String msid, String subscribe, String mid
            , String lfrag, int lpos, String expiryDate, String mprice) {
        fav = favstatus;
        movieid = mid;
        frag = lfrag;
        listposition = lpos;
        movietype = mtype;
        movieid = mid;
        sid = msid;
        partid = mpartid;
        meprice = mprice;
        sname = title;
        expidate = expiryDate;
        final Handler searchhandler = new Handler();
        searchhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                starttracking(expidate);
            }
        }, 2000);
        saveMovieDetails(path,title);
        subscription = subscribe;
        //Toast.makeText(Home.this, partid, Toast.LENGTH_LONG).show();
        hiddenone.setText(mpartid);
        hiddentwo.setText(favstatus);
        //Toast.makeText(Home.this, partid + " "+ favstatus, Toast.LENGTH_LONG).show();

        savePartDetails(mpartid,partdate,moviedesc, favstatus, mtype, msid);
        selectedid.setText(mpartid);
        selmovieid.setText(mid);
        selectedsid.setText(msid);
        selectedtype.setText(mtype);
        pausing = false;
        videoname = path;
        String existinginfo;
        imageView.setVisibility(View.GONE);
        //Toast.makeText(Home.this, path, Toast.LENGTH_LONG).show();
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        titletext.setVisibility(View.VISIBLE);
       playlayout.setVisibility(View.VISIBLE);
        //mediaPlayer.start();

        if(titletext.getVisibility() == View.VISIBLE){
            existinginfo = titletext.getText().toString();
            if(existinginfo.equalsIgnoreCase(title) && titletext != null){
                //don't reload video
                return;
            }else{

            }
        }

        new AsyncRecordView().execute(mpartid,mtype, msid);
        titletext.setText(title);
        mtitle.setText(title);
        videoname = path;
        playbackposition = 0;
        initmediaplayer();

    }

    @Override
    public void onRequestMovies() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mvfrag != null){
                            mvfrag.reloadview();
                        }

                    }
                }, 7000);
            }
        });
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {




        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }




        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:

                    return new MostViewed();



                //case 1:
                  //  return new Movies();

                case 1:
                    return new Season();



                default:
                    return new MostViewed();


            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            Fragment createdfrag = (Fragment) super.instantiateItem(container, position);
            switch (position){
                case 0:
                    mvfrag = (MostViewed) createdfrag;
                    break;
               // case 1:
                 //   mfrag = (Movies)createdfrag;
                   // break;
                case 1:
                    sfrag = (Season)createdfrag;
                    break;
            }

            return  createdfrag;
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle ( int position) {
            switch (position) {
                case 0:
                    return "Most Viewed";
                case 1:
                    return "Series";
                //case 2:
                  //  return "Series";

                default:
                    return "Most Viewed";
            }
        }
    }



    private class mSectionsPagerAdapterm extends FragmentPagerAdapter {




        mSectionsPagerAdapterm(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }




        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:


                    return new Description();

                case 1:
                    return new Comments();
                case 2:
                    return new Ratings();


                default:
                    return new Description();

            }
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle ( int position) {
            switch (position) {
                case 0:
                    return "Description";
                case 1:
                    return "Comments ";
                case 2:
                    return "Ratings";

                default:
                    return "Description";
            }
        }
    }

    public class AsyncCheckDate extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.43.189/farmhousemovies/api/datetime.php");

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
                conn.setRequestMethod("GET");
                // conn.setRequestMethod("GET");



                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);


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
                    InputStreamReader inputStream = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStream);
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    reader.close();
                    inputStream.close();

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

            if (result.contains("exception") || result.contains("unsuccessful")) {

                Log.d("Network Error: ","error connecting to server");
                Toast.makeText(Home.this, "Error connecting to server",Toast.LENGTH_LONG).show();
                new AsyncCheckDate().execute();
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try{
                    Date date = sdf.parse(result);
                    comparedate(date);
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }



        @Override
        protected void onCancelled() {
            //do nothing
        }
    }


    public class AsyncRecordView extends AsyncTask<String, String, String>
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
                url = new URL("http://192.168.43.189/farmhousemovies/api/recordviews.php");

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
                        .appendQueryParameter("partid", params[0])
                        .appendQueryParameter("category", params[1])
                        .appendQueryParameter("seasonid", params[2])
                        .appendQueryParameter("movieid", params[3])
                        .appendQueryParameter("moviename", params[4])
                        .appendQueryParameter("seasonname", params[5])
                        .appendQueryParameter("episodename", params[6])
                        .appendQueryParameter("user", params[7]);
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

            if (result.contains("exception") || result.contains("unsuccessful")) {

                Log.d("Network Error: ","error connecting to server");

            }
        }



        @Override
        protected void onCancelled() {
            //do nothing
        }
    }

    private void comparedate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY ,0);
        calendar.set(Calendar.MILLISECOND ,0);
        calendar.set(Calendar.MINUTE ,0);
        calendar.set(Calendar.SECOND ,0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
             newerdate = sdf.parse(sdf.format(calendar.getTime()));

        }catch (ParseException e){
            e.printStackTrace();
        }



        if(date.after(newerdate)){
               //displayinvaliddatedialog(newdate, time);
        }else{
            exitdiag = false;
        }
    }

    private void displayinvaliddatedialog(Date newdate, Date time) {
        if(player!=null){
            player.setPlayWhenReady(false);
        }
        exitdiag = true;
        AlertDialog.Builder adb = new AlertDialog.Builder(Home.this, R.style.myDialogTheme);
      String fdate = newdate.toString();

        adb.setTitle("Warning")
                .setMessage("Your date is inaccurate!"+"\n"+"\n"+
                        "Check your Date and Time settings."+"\n"+"\n"+"Your phone's date is "+fdate.replace("00:00:00 GMT ","")+"\n"+"\n"+"The right date is "+time)
                .setIcon(android.R.drawable.ic_dialog_alert);

        adb.setCancelable(false);
        alertDialog = adb.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(layoutParams);

    }

    public void setactivityname(String acname, String actype){
       this.cacname = acname;
       this.cactype = actype;
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 111 || resultCode == 222) {

          if(cactype.equalsIgnoreCase("fragment")){
              cactype = "";
              if(cacname.equalsIgnoreCase("MostViewed")) {
                  FragmentManager fm = getSupportFragmentManager();
                  MostViewed fragment =
                          (MostViewed) fm.findFragmentByTag("android:switcher:"+R.id.pager+":"+ viewpager.getCurrentItem());
                    fragment.onresultreceived(data.getExtras().getString("response"));
              }

              if(cacname.equalsIgnoreCase("MoviesAnnex")) {
                  FragmentManager fm = getSupportFragmentManager();
                  MoviesAnnex fragment =
                          (MoviesAnnex) fm.findFragmentByTag("mvannex");
                  fragment.onresultreceived(data.getExtras().getString("response"));
              }

              if(cacname.equalsIgnoreCase("TeleAnnex")) {
                  FragmentManager fm = getSupportFragmentManager();
                  Series fragment =
                          (Series) fm.findFragmentByTag("android:switcher:"+R.id.pager+":"+ viewpager.getCurrentItem());
                  fragment.reassignvalues("Telenovelas",data.getExtras().getString("response"));
              }

              if(cacname.equalsIgnoreCase("SeasAnnex")) {
                  FragmentManager fm = getSupportFragmentManager();
                  Series fragment =
                          (Series) fm.findFragmentByTag("android:switcher:"+R.id.pager+":"+ viewpager.getCurrentItem());
                  fragment.reassignvalues("Seasons",data.getExtras().getString("response"));
              }

                cacname = "";

          }else{
                onresultreceived(currentlistview,data.getExtras().getString("response"));
          }


        }else if(resultCode == 333){
            String abortedcode = "{\"reason\":\"aborted\",\"code\":\"900\"}";

          //Toast.makeText(Home.this, "operation aborted",
           //Toast.LENGTH_LONG).show();
            if(cactype.equalsIgnoreCase("fragment")){
                cactype = "";
                if(cacname.equalsIgnoreCase("MostViewed")) {
                    FragmentManager fm = getSupportFragmentManager();
                    MostViewed fragment =
                            (MostViewed) fm.findFragmentByTag("android:switcher:"+R.id.pager+":"+ viewpager.getCurrentItem());
                    fragment.onresultreceived(abortedcode);
                }

                if(cacname.equalsIgnoreCase("MoviesAnnex")) {
                    FragmentManager fm = getSupportFragmentManager();
                    MoviesAnnex fragment =
                            (MoviesAnnex) fm.findFragmentByTag("mvannex");
                    fragment.onresultreceived(abortedcode);
                }

                if(cacname.equalsIgnoreCase("TeleAnnex")) {
                    FragmentManager fm = getSupportFragmentManager();
                    Series fragment =
                            (Series) fm.findFragmentByTag("android:switcher:"+R.id.pager+":"+ viewpager.getCurrentItem());
                    fragment.reassignvalues("Telenovelas",abortedcode);
                }

                if(cacname.equalsIgnoreCase("SeasAnnex")) {
                    FragmentManager fm = getSupportFragmentManager();
                    Series fragment =
                            (Series) fm.findFragmentByTag("android:switcher:"+R.id.pager+":"+ viewpager.getCurrentItem());
                    fragment.reassignvalues("Seasons",abortedcode);
                }

                cacname = "";

            }else{
                onresultreceived(currentlistview,abortedcode);
            }

        }



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

                Toast.makeText(Home.this, "Error connecting to server!.",
                        Toast.LENGTH_LONG).show();

                progressBarone.setVisibility(View.INVISIBLE);
                closediagbtn.setVisibility(View.VISIBLE);
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
                url = new URL("http://192.168.43.189/farmhousemovies/api/setfavourites.php");

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
                        .appendQueryParameter("status", params[0])
                        .appendQueryParameter("partid", params[1])
                        .appendQueryParameter("userid", params[2])
                        .appendQueryParameter("mtype", params[3])
                        .appendQueryParameter("sid", params[4])
                        .appendQueryParameter("movid", params[5]);
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
        refreshfragsetfav(frag,listposition);
    }

    public void refreshfragsetfav(String frag, int listposition) {
        switch (frag){
            case  "mostviewed":
            MostViewed.refreshlist(listposition);
            break;
            case  "moviesannex":
                MoviesAnnex.refreshlist(listposition);
                break;
            case  "seasonepisodes":
                SeasonAnnexEpisodes.refreshlist(listposition);
                break;
            case  "telenovelaepisodes":
                TelenovelaAnnexEpisodes.refreshlist(listposition);
                break;
        }
    }


    public class AsyncSetSubscr extends AsyncTask<String, String, String> {

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
                url = new URL("http://192.168.43.189/farmhousemovies/api/setsubscriptions.php");

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
                        .appendQueryParameter("status", params[0])
                        .appendQueryParameter("movieid", params[1])
                        .appendQueryParameter("userid", params[2]);
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

            if(result.contains("subscribed")) {
                Log.d("Subscription Status: ","Added to subscriptions");


            }else if(result.contains("unsubscribed")){
                Log.d("Subscription Status: ","Removed from subscriptions");
            }
            else if (!result.contains("subscribed")  || result.contains("error")){

                Log.d("Save Error: ","unable to subscribe to movie");


            } else if (result.contains("exception") || result.contains("unsuccessful")) {

                Log.d("Network Error: ","error connecting to server");

            }
        }
    }
}
