package com.vandyflintz.farmhousemovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.vandyflintz.farmhousemovies.adapter.CustomPartsListAdapter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesAnnex.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesAnnex#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesAnnex extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public MoviesAnnex() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesAnnex.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesAnnex newInstance(String param1, String param2) {
        MoviesAnnex fragment = new MoviesAnnex();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private ProgressDialog pDialog;
    private static List<Movie> movieList = new ArrayList<Movie>();
    private static ListView listView;
    private static CustomPartsListAdapter adapter;
    String urlresult;
    static String userid;
    String finalfilename, pstatus,servermsg;
    Button button, refreshbtn , loadmore;
    TextView emptyText, title;
    RelativeLayout relativeLayoutone;
    int pagenum;
    static int curpage;
    static int remaining;
    static ProgressBar progressBar;
    View footerview;
    public static int vpos, index;
    public static Context context = null;
    AlertDialog alertDialog;
    RelativeLayout relativeLayout, gridLayout;
    TextView amount, message, movietxt, hidspin, pnote, vul;
    Button submit;
    Button closebtn;
    Button retrybtn;
    static Button allsubbtn;
    EditText num, voucher;
    Spinner wallets;
    String[] arrayspMoMo;
    ProgressBar progressBarone;
    String amt;
    String walletnum;
    String vouchernum;
    String phonenum;
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
    static String totalprice;
    public static final int CONNECTION_TIMEOUT=50000;
    public static final int READ_TIMEOUT=55000;
    public  static String movieid, category, moviename, availability;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies_annex, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));

        urlresult = getArguments().getString("MovieID");
        movieid = getArguments().getString("MID");
        listView = (ListView) v.findViewById(R.id.listmoviesparts);
        emptyText = v.findViewById(R.id.emptytext);
        title = v.findViewById(R.id.titletxt);
        allsubbtn = v.findViewById(R.id.suballbtn);
        relativeLayoutone = v.findViewById(R.id.backhead);
        adapter = new CustomPartsListAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
        button = (Button)v.findViewById(R.id.backbtn);

        progressBar = v.findViewById(R.id.sBar);
        refreshbtn = v.findViewById(R.id.refreshbtn);
        // Showing progress dialog before making http request

        context = getActivity();

        allsubbtn.setVisibility(View.GONE);
        footerview =
                ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        loadmore = footerview.findViewById(R.id.btnloadmore);


        refreshbtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);



        pagenum = 1;
        curpage = 1;

        movieList.clear();
        listView.invalidateViews();
        listView.addFooterView(footerview);
        footerview.setVisibility(View.GONE);
        loadmore.setVisibility(View.GONE);
        if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                ".android.tv")){
            getrecords(curpage, "tv");
        }else{
            getrecords(curpage, "other");
        }


        allsubbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpaymentdialog("SingleFull", movieid, "", "", userid,totalprice,
                        moviename);
            }
        });

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
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)progressBar.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                progressBar.setLayoutParams(layoutParams);
                curpage += 1;
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    getrecords(curpage, "tv");
                }else{
                    getrecords(curpage, "other");
                }
            }
        });

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    getrecords(curpage, "tv");
                }else{
                    getrecords(curpage, "other");
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if(fm.getBackStackEntryCount() > 0){
                    fm.popBackStack();
                }else{
                    //do nothing
                }
            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView)view.findViewById(R.id.mfile));
                TextView textView2 = ((TextView)view.findViewById(R.id.mtitle));
                TextView textView3 = ((TextView)view.findViewById(R.id.mmid));
                TextView movienid = ((TextView)view.findViewById(R.id.mid));
                TextView textView4 = ((TextView)view.findViewById(R.id.mdesc));
                TextView textView5 = ((TextView)view.findViewById(R.id.mdate));
                TextView textView6 = ((TextView)view.findViewById(R.id.mpid));
                TextView textView7 = ((TextView)view.findViewById(R.id.mfid));
                TextView textView8 = ((TextView)view.findViewById(R.id.finalhiddentitle));
                TextView textView9 = ((TextView)view.findViewById(R.id.hiddentitle));
                TextView textView10 = ((TextView)view.findViewById(R.id.mtype));
                TextView paidstatus = ((TextView)view.findViewById(R.id.paidbtn));
                TextView seasonid = ((TextView)view.findViewById(R.id.msid));
                TextView subscr = ((TextView)view.findViewById(R.id.msub));
                TextView price = ((TextView)view.findViewById(R.id.mprice));
                TextView expDate = ((TextView)view.findViewById(R.id.mexp));

                String expiryDate = expDate.getText().toString();
                String moviefile = textView.getText().toString();
                String movietitle = textView2.getText().toString() ;
                String moviepath = textView3.getText().toString() ;
                String moviedesc = textView4.getText().toString();
                String partdate = textView5.getText().toString();
                String partid = textView6.getText().toString();
                String favistatus = textView7.getText().toString();
                String meprice = price.getText().toString();
                String finalname = textView8.getText().toString();
                String oname = textView9.getText().toString();
                String mtype = textView10.getText().toString();
                String sid = seasonid.getText().toString();
                String movieid = movienid.getText().toString();
                String subscription = subscr.getText().toString();
                String lfrag = "moviesannex";
                int lpos = position;
                if(moviefile.matches("")){
                    //do nothing
                }else {

                    if(finalname.equals(movietitle)){
                        finalfilename = movietitle;
                    }else{
                        finalfilename = oname;
                    }

                    if(paidstatus.getVisibility() == View.VISIBLE){
                        showpaymentdialog(mtype, movieid, "", partid, userid,meprice,
                                finalfilename);

                    }else{


                        mListener.onFragmentInteraction(moviefile, finalfilename, moviepath, partid,
                                partdate,  moviedesc, favistatus, mtype, sid, subscription, movieid,
                                lfrag, lpos, expiryDate, meprice);


                    }




                   // Toast.makeText(getActivity(), favistatus, Toast.LENGTH_LONG).show();

                }

            }
        });

        return v;
    }

    private void saveDialogDetails(String movietype, String movieid, String sid, String partid,
                                   String userid, String mPrice, String mName){
        new PreferencesManager(getActivity()).saveAlertDetails(movietype,movieid,sid,partid,
                userid,mPrice,mName);
    }

    private void showpaymentdialog(String movietype, String movieid, String sid, String partid,
                                   String userid, String mPrice, String mName) {

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
                    getrefreshedrecords(totalitems);
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
            ((Home)getActivity()).setactivityname("MoviesAnnex","fragment");
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
                showpaymentdialog(mtype,mid,sid,partid,userid,mprice,mname);
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


                showpaymentdialog(mtype,mid,sid,partid,userid,mprice,mname);
                new AsyncPay().execute(mtype,mid,partid, sid, userid,"successfulpayment");
            }
        }

    }

    private void setupSpinners() {
        wallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 3) {
                    voucher.setVisibility(View.VISIBLE);
                    vul.setVisibility(View.VISIBLE);
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
                    vul.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                voucher.setVisibility(View.GONE);
                vul.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static void refreshlist(int listposition){
        //vpos = listposition;
        index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        vpos = (v== null) ? 0 : (v.getTop() - listView.getPaddingTop());
        int totalitems = (listView.getAdapter().getCount()) - 1;
        getrefreshedrecords(totalitems);
    }

    private static void getrefreshedrecords( int totalitems) {
        listView.invalidateViews();
        movieList.clear();
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest movieReq =
                new JsonArrayRequest("http://192.168.43.189/farmhousemovies/api/getrefreshedresults" +
                        ".php?id="+movieid+
                        "&user="+userid+
                        "&rtype=movies&items="+totalitems,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if(response.length()>0){

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    progressBar.setVisibility(View.GONE);
                                    JSONObject obj = response.getJSONObject(i);
                                    JSONObject objcount = response.getJSONObject(0);
                                    Movie movie = new Movie();
                                    movie.setTitle(obj.getString("title"));
                                    movie.setPartname(obj.getString("partname"));
                                    movie.setThumbnailUrl(obj.getString("image"));
                                    movie.setDuration(obj.getString("movieDuration"));
                                    movie.setFilename(obj.getString("movieFile"));
                                    movie.setID(obj.getString("moviedirectory"));
                                    movie.setDesc(obj.getString("description"));
                                    movie.setDate(obj.getString("releasedate"));
                                    movie.setpartID(obj.getString("partid"));
                                    movie.setFav(obj.getString("favstatus"));
                                    movie.setMovietype(obj.getString("movietype"));
                                    movie.setCategory(obj.getString("category"));
                                    movie.setPrice(obj.getString("price"));
                                    movie.setPaidstatus(obj.getString("paidstatus"));
                                    movie.setSubscription(obj.getString("subscription_status"));
                                    movie.setMovieID(obj.getString("movieID"));
                                    movie.setOverallprice(objcount.getString("totalprice"));
                                    movie.setExpirydate(obj.getString("expirydate"));
                                    movie.setFullmovieavailability(obj.getString("full_release_status"));
                                    movieid = obj.getString("movieID");
                                    moviename = objcount.getString("title");
                                    category = objcount.getString("category");
                                    remaining = objcount.getInt("remainingitems");
                                    totalprice = objcount.getString("totalprice");
                                    availability = objcount.getString("full_release_status");
                                    // adding movie to movies array
                                    movieList.add(movie);

                                    if(objcount.getString("full_release_status")==
                                            "available") {
                                        final Handler searchhandler = new Handler();
                                        searchhandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                displaybtn();
                                            }
                                        }, 500);
                                    }

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
                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }



    public static void scrolltoitem() {
        listView.setSelectionFromTop(index, vpos);
    }

    private void getrecords(int pagenum, String device) {
        // Creating volley request obj
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, urlresult+"&user="+userid+"&page="+pagenum+
                "&device="+device+"&ptype=movies");
        JsonArrayRequest movieReq = new JsonArrayRequest(urlresult+"&user="+userid+"&page="+pagenum+
                "&device="+device+"&ptype=movies",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        if(response.length()>0){

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    progressBar.setVisibility(View.GONE);
                                    JSONObject obj = response.getJSONObject(i);
                                    JSONObject objcount = response.getJSONObject(0);
                                    Movie movie = new Movie();
                                    title.setText(objcount.getString("title"));
                                    movie.setTitle(obj.getString("title"));
                                    movie.setPartname(obj.getString("partname"));
                                    movie.setThumbnailUrl(obj.getString("image"));
                                    movie.setDuration(obj.getString("movieDuration"));
                                    movie.setFilename(obj.getString("movieFile"));
                                    movie.setID(obj.getString("moviedirectory"));
                                    movie.setDesc(obj.getString("description"));
                                    movie.setDate(obj.getString("releasedate"));
                                    movie.setpartID(obj.getString("partid"));
                                    movie.setFav(obj.getString("favstatus"));
                                    movie.setMovietype(obj.getString("movietype"));
                                    movie.setCategory(obj.getString("category"));
                                    movie.setPrice(obj.getString("price"));
                                    movie.setPaidstatus(obj.getString("paidstatus"));
                                    movie.setSubscription(obj.getString("subscription_status"));
                                    movie.setMovieID(obj.getString("movieID"));
                                    movie.setOverallprice(objcount.getString("totalprice"));
                                    movie.setExpirydate(obj.getString("expirydate"));
                                    movie.setFullmovieavailability(obj.getString("full_release_status"));
                                    movieid = obj.getString("movieID");
                                    moviename = objcount.getString("title");
                                    category = objcount.getString("category");
                                    remaining = objcount.getInt("remainingitems");
                                    totalprice = objcount.getString("totalprice");
                                    curpage = Integer.parseInt(obj.getString("pagenum"));
                                    availability = objcount.getString("full_release_status");
                                    getchilddata(obj.getString("partid"));
                                    fillchilddate(obj.getString("releasedate"));
                                    fillchilddesc(obj.getString("description"));

                                    // adding movie to movies array
                                    movieList.add(movie);
                                    final Handler searchhandler = new Handler();
                                    searchhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            displaybtn();
                                        }
                                    }, 500);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{

                            listView.setVisibility(View.INVISIBLE);
                            emptyText.setVisibility(View.VISIBLE);
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


                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    private static void displaybtn() {
        if(!category.equalsIgnoreCase("free") || !availability.equalsIgnoreCase("available")){
            allsubbtn.setVisibility(View.VISIBLE);
        }
    }

    public String getchilddata(String partid) {
        return partid;
    }

    private String fillchilddesc(String moviedesc) {
        return moviedesc;
    }

    private String fillchilddate(String partdate) {
        return partdate;
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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
    public interface OnDataPassed {
        // TODO: Update argument type and name
         public void onDataPassed(String data, String title);
    }

    public void refreshmaview(){
        Toast.makeText(getActivity(),"MoviesAnnex recorded a payment", Toast.LENGTH_LONG).show();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
       public void onFragmentInteraction(String data, String title, String path, String partid,
                                         String partdate, String moviedesc, String favstatus, String mtype, String sid, String subscription, String movieid, String lfrag, int lpos, String expiryDate, String meprice);
        //public void onDataPassed(String data);

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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach(){
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
        super.onDetach();

    }
}
