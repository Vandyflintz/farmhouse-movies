package com.vandyflintz.farmhousemovies;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.vandyflintz.farmhousemovies.adapter.CustomListAdapter;
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
 * Use the {@link Season#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Season extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Season() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Season.
     */
    // TODO: Rename and change types and number of parameters
    public static Season newInstance(String param1, String param2) {
        Season fragment = new Season();
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

    private static final String url = "http://192.168.43.189/farmhousemovies/api/index" +
            ".php?type=Season";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
    TextView emptyText;
    ProgressBar progressBar;
    Button refreshbtn, loadmore;
    View footerview;
    int pagenum, curpage, remaining;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_season, container, false);

        listView = (ListView) v.findViewById(R.id.listmovies);
        emptyText = v.findViewById(R.id.emptytext);
        adapter = new CustomListAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
        progressBar = v.findViewById(R.id.sBar);
        refreshbtn = v.findViewById(R.id.refreshbtn);
        swipeRefreshLayout = v.findViewById(R.id.seriesfrag);
        swipeRefreshLayout.setOnRefreshListener(this);
        // Showing progress dialog before making http request

        refreshbtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);

        pagenum = 1;
        curpage = 1;


        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    getrecords(curpage, "tv", "fetch");
                }else{
                    getrecords(curpage, "other", "fetch");
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        footerview =
                ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        loadmore = footerview.findViewById(R.id.btnloadmore);


        if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                ".android.tv")){
            getrecords(curpage, "tv", "fetch");
        }else{
            getrecords(curpage, "other", "fetch");
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
                curpage += 1;
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)progressBar.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                progressBar.setLayoutParams(layoutParams);
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    getrecords(curpage, "tv", "fetch");
                }else{
                    getrecords(curpage, "other", "fetch");
                }
            }
        });

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                emptyText.setVisibility(View.GONE);
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    getrecords(curpage, "tv", "refresh");
                }else{
                    getrecords(curpage, "other", "refresh");
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView) view.findViewById(R.id.mid));
                String info = textView.getText().toString();
                //Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();

                if (info.matches("")) {

                    //do nothing
                } else {
                    SeasonAnnex fg = new SeasonAnnex();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction trans = fm.beginTransaction();
                    Bundle scanresbundle = new Bundle();
                    scanresbundle.putString("MovieID",
                            "http://192.168.43.189/farmhousemovies/api/mainseason_and_series" +
                                    ".php?id=" + info+
                                    "&type=Season");
                    fg.setArguments(scanresbundle);
                    trans.replace(R.id.seasoninf, fg);
                    Log.d(TAG, "Season id: " + info);
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);

                    trans.commit();

                }
            }
        });


        return v;
    }

    private void getrecords(int pagenum, String device, String request) {
        // Creating volley request obj
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest movieReq = new JsonArrayRequest(url+"&page="+pagenum+
                "&device="+device+"&request="+request,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if(response.length()>0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    JSONObject objcount = response.getJSONObject(0);
                                    progressBar.setVisibility(View.GONE);
                                    emptyText.setVisibility(View.GONE);
                                    refreshbtn.setVisibility(View.GONE);
                                    Movie movie = new Movie();
                                    movie.setTitle(obj.getString("title"));
                                    movie.setThumbnailUrl(obj.getString("image"));
                                    movie.setID(obj.getString("movieID"));
                                    movie.setYear(obj.getString("releasedate"));
                                    curpage = Integer.parseInt(obj.getString("pagenum"));
                                    remaining = objcount.getInt("remainingitems");
                                    // adding movie to movies array
                                    movieList.add(movie);
                                    //begintimer();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{
                           // listView.setVisibility(View.INVISIBLE);
                            emptyText.setVisibility(View.VISIBLE);
                            emptyText.setText("No information available");
                            progressBar.setVisibility(View.INVISIBLE);

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

    private void refreshmview() {
        movieList.clear();
        refreshbtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        emptyText.setVisibility(View.GONE);
        if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                ".android.tv")){
            getrecords(curpage, "tv","refresh");
        }else{
            getrecords(curpage, "other", "refresh");
        }
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


}
