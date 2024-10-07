package com.vandyflintz.farmhousemovies;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.taufiqrahman.reviewratings.RatingReviews;
import com.vandyflintz.farmhousemovies.adapter.CustomRatingsAdapter;
import com.vandyflintz.farmhousemovies.adapter.OtherRatingsAdapter;
import com.vandyflintz.farmhousemovies.app.AppController;
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
import java.util.List;

import static com.android.volley.VolleyLog.TAG;
import static com.vandyflintz.farmhousemovies.Comments.uioptions;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ratings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ratings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Ratings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ratings.
     */
    // TODO: Rename and change types and number of parameters
    public static Ratings newInstance(String param1, String param2) {
        Ratings fragment = new Ratings();
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
    TextView results, textView, people, underline, emptytext;
    RatingBar userratingBar, allRatingBar;
    double totalusers, fivestarrating, fourstarrating, threestarrating, twostarrating,
            onestarrating;
    int fivestars, fourstars, threestars, twostars, onestar;
    String partid, userid;
    RequestQueue requestQueue;
    RatingReviews ratingReviews;
    String firstval,secondval,thirdval,fourthval,fifthval,sixthval
            ,seventhval;

    GridLayout gridLayout;
    Button ratingbtn;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    float ratinggot;
    JsonArrayRequest movieReqone;
    String dataurl, dataoneurl, mtype, seasonid;
    ListView listView, otherlistView;
    private CustomRatingsAdapter adapter;
    OtherRatingsAdapter oradapter;
    RelativeLayout relativeLayout;
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private List<Movie> movieListone = new ArrayList<Movie>();
    AlertDialog alertdialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_ratings, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("MoviePartDetails",
                Context.MODE_PRIVATE);
        partid = (sp.getString("MovieID", ""));
        mtype = (sp.getString("MovieType", ""));
        seasonid = (sp.getString("SeasonID", ""));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        userratingBar = v.findViewById(R.id.rating_bar);
        underline = v.findViewById(R.id.underline);
        emptytext = v.findViewById(R.id.emptyrating);
        ratingbtn = v.findViewById(R.id.submitRating);
        listView = v.findViewById(R.id.listratings);

        relativeLayout = v.findViewById(R.id.relcon);
        adapter = new CustomRatingsAdapter(getActivity(), movieList, this);

        listView.setAdapter(adapter);

        ratingbtn.setVisibility(View.INVISIBLE);
        pDialog = new ProgressDialog(getActivity());

        userratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                   // Toast.makeText(getActivity(), ""+rating,Toast.LENGTH_LONG).show();
                  ratinggot = rating;
                  if(rating < 0.5){
                      ratingbtn.setVisibility(View.INVISIBLE);
                  }else{
                      ratingbtn.setVisibility(View.VISIBLE);
                      ratingbtn.setOnClickListener(v1 -> new AsyncRate().execute(String.valueOf(rating), userid, partid, seasonid, mtype));
                  }
                }
            }
        });




        dataurl = "http://192.168.43.189/farmhousemovies/api/ratings.php?userid="+userid+"&partid" +
                "="+partid+"&mtype="+mtype+"&otherid="+seasonid;



        readratedata(dataurl);


        return v;
    }


    public void displayotherratings(){
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        View mydialog =
                getLayoutInflater().inflate(R.layout.allotherratingspanel,
                        (ViewGroup) null);

        adb.setView(mydialog);

        ImageButton backbtn = mydialog.findViewById(R.id.backbtn);
        otherlistView = mydialog.findViewById(R.id.otherlistratings);
        oradapter = new OtherRatingsAdapter(getActivity(), movieListone, this);
        otherlistView.setAdapter(oradapter);


        otherlistView.invalidateViews();
        pDialog = new ProgressDialog(getActivity());

        dataoneurl = "http://192.168.43.189/farmhousemovies/api/ratings.php?userid="+userid+"&movieid" +
                "="+partid+"&mtype="+mtype+"&otherid="+seasonid;
        getotherratings(dataoneurl);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.dismiss();
            }
        });

        alertdialog = adb.create();
        alertdialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertdialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertdialog.getWindow().setAttributes(layoutParams);
        alertdialog.getWindow().getDecorView().setSystemUiVisibility(uioptions);
        alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertdialog.setCanceledOnTouchOutside(false);
    }

    public void getotherratings(String url) {
       // pDialog.setMessage("Loading...");
        //pDialog.show();
        //pDialog.setCancelable(false);
        movieListone.clear();


         movieReqone = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        if(response.length()>0){
                                // Toast.makeText(getActivity(), "Others exist", Toast
                            // .LENGTH_LONG).show();
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.setTitle(obj.getString("title"));
                                    movie.setContact(obj.getString("contact"));
                                    movie.setThumbnailUrl(obj.getString("dp"));
                                    movie.setUserrating(obj.getString("rating"));
                                    movie.setTime(obj.getString("time"));
                                    // adding movie to movies array
                                    movieListone.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{

                            otherlistView.setVisibility(View.GONE);

                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        oradapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage() , Toast.LENGTH_LONG).show();

                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReqone);
    }

    private void readratedata(String url) {


        movieList.clear();
        listView.invalidateViews();

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        if(response.length()>0){

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Movie movie = new Movie();

                                    if(obj.has("username")){
                                        movie.setFivestarraters(obj.getString("fivestarraters"));
                                        movie.setFourstarraters(obj.getString("fourstarraters"));
                                        movie.setThreestarraters(obj.getString("threestarraters"));
                                        movie.setTwostarraters(obj.getString("twostarraters"));
                                        movie.setOnestarraters(obj.getString("onestarraters"));
                                        movie.setTotalraters(obj.getString("totalraters"));
                                        movie.setOverallrating(obj.getString("overallrating"));
                                        movie.setUserrating(obj.getString("userrating"));
                                        movie.setDaterated(obj.getString("daterated"));
                                        movie.setRemraters(obj.getString("remainingusers"));
                                        relativeLayout.setVisibility(View.GONE);
                                    }else{
                                        movie.setFivestarraters(obj.getString("fivestarraters"));
                                        movie.setFourstarraters(obj.getString("fourstarraters"));
                                        movie.setThreestarraters(obj.getString("threestarraters"));
                                        movie.setTwostarraters(obj.getString("twostarraters"));
                                        movie.setOnestarraters(obj.getString("onestarraters"));
                                        movie.setTotalraters(obj.getString("totalraters"));
                                        movie.setOverallrating(obj.getString("overallrating"));
                                        movie.setRemraters(obj.getString("remainingusers"));
                                    }




                                    // adding movie to movies array
                                    movieList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{

                            listView.setVisibility(View.GONE);
                            emptytext.setVisibility(View.VISIBLE);
                            emptytext.setText("No ratings available, rate now");
                           // gridLayout.setVisibility(View.GONE);
                            underline.setVisibility(View.GONE);
                            userratingBar.setRating(Float.parseFloat("0.0"));
                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage() , Toast.LENGTH_LONG).show();

                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void showprofiledialog(String vTag) {

    }

    public void showprofilenamedialog(String vTag) {

    }


    public class AsyncRate extends AsyncTask<String, String, String>
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
                url = new URL("http://192.168.43.189/farmhousemovies/api/ratings.php");

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
                        .appendQueryParameter("rating", params[0])
                        .appendQueryParameter("userid", params[1])
                        .appendQueryParameter("partid", params[2])
                        .appendQueryParameter("seasonid", params[3])
                        .appendQueryParameter("mtype", params[4]);
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

            if(result.contains("rated")) {
                Log.d("Rating Status: ", "Successfully rated");
                Toast.makeText(getActivity(), "Successfully rated",
                        Toast.LENGTH_LONG).show();




                Ratings fg = new Ratings();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.ratingfrag, fg);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.commit();
                //readratedata(dataurl);


            }
            else if (!result.contains("rated")  || result.contains("error")){

                Log.d("Rating Error: ","unable to complete request");
                Toast.makeText(getActivity(), "Error processing rating, try again later.",
                        Toast.LENGTH_LONG).show();

            } else if (result.contains("exception") || result.contains("unsuccessful")) {

                Log.d("Network Error: ","error connecting to server");

            }
        }



        @Override
        protected void onCancelled() {
            //do nothing
        }
    }
}
