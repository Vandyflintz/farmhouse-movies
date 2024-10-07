package com.vandyflintz.farmhousemovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.vandyflintz.farmhousemovies.adapter.CustomSubscriptionsAdapter;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Telenovela_Sub_Active_Seasons#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Telenovela_Sub_Active_Seasons extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Telenovela_Sub_Active_Seasons() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Telenovela_Sub_Active_Seasons.
     */
    // TODO: Rename and change types and number of parameters
    public static Telenovela_Sub_Active_Seasons newInstance(String param1, String param2) {
        Telenovela_Sub_Active_Seasons fragment = new Telenovela_Sub_Active_Seasons();
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

    private static List<Movie> movieList = new ArrayList<Movie>();
    private static ListView listView;
    private static CustomSubscriptionsAdapter adapter;
    static String userid;
    Button button, refreshbtn , loadmore;
    TextView emptyText, test;
    static int curpage;
    static int remaining;
    static ProgressBar progressBar;
    View footerview;
    Handler timehandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_telenovela__sub__active__seasons, container,
                false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));
        listView = (ListView) v.findViewById(R.id.listView);
        emptyText = v.findViewById(R.id.emptytext);
        adapter = new CustomSubscriptionsAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
        progressBar = v.findViewById(R.id.sBar);
        refreshbtn = v.findViewById(R.id.refreshbtn);

        footerview =
                ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        loadmore = footerview.findViewById(R.id.btnloadmore);


        refreshbtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);


        curpage = 1;

        movieList.clear();

        listView.addFooterView(footerview);
        footerview.setVisibility(View.GONE);
        loadmore.setVisibility(View.GONE);
        if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                ".android.tv")){
            getrecords(curpage, "tv");
        }else{
            getrecords(curpage, "other");
        }



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
                refreshbtn.setVisibility(View.GONE);
                emptyText.setVisibility(View.INVISIBLE);
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    getrecords(curpage, "tv");
                }else{
                    getrecords(curpage, "other");
                }
            }
        });

        return v;
    }

    private void getrecords(int pagenum, String device) {
        // Creating volley request obj
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest movieReq =
                new JsonArrayRequest(" http://192.168.43.189/farmhousemovies/api/subscriptions_records" +
                        ".php?user="+userid +"&device="+device+"&page="+pagenum+"&type=series" +
                        "&secs=full&status=active",
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
                                            movie.setSubTitle(obj.getString("subtitle"));
                                            movie.setExpirydate(obj.getString("expirydate"));
                                            movie.setStartdate(obj.getString("startdate"));
                                            movie.setFormattedExpirydate(obj.getString("fexpirydate"));
                                            movie.setFormattedStartdate(obj.getString("fstartdate"));
                                            movie.setThumbnailUrl(obj.getString("image"));

                                            remaining = objcount.getInt("remainingitems");
                                            curpage = Integer.parseInt(obj.getString("pagenum"));


                                            movieList.add(movie);

                                            refreshlistview();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }else{
                                    progressBar.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.INVISIBLE);
                        emptyText.setVisibility(View.VISIBLE);
                        emptyText.setText("Oops, Error occured - Retry");
                        refreshbtn.setVisibility(View.VISIBLE);

                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }



    private void refreshlistview() {
        timehandler.postDelayed(updatelistview,5000);
    }

    private Runnable updatelistview = new Runnable() {
        @Override
        public void run() {

            adapter.notifyDataSetChanged();
            timehandler.postDelayed(this,5000);
        }
    };

}