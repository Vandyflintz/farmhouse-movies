package com.vandyflintz.farmhousemovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
 * Activities that contain this fragment must implement the
 * {@link TelenovelaAnnex.OnTeleFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TelenovelaAnnex#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelenovelaAnnex extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnTeleFragmentInteractionListener mListener;

    public TelenovelaAnnex() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TelenovelaAnnex.
     */
    // TODO: Rename and change types and number of parameters
    public static TelenovelaAnnex newInstance(String param1, String param2) {
        TelenovelaAnnex fragment = new TelenovelaAnnex();
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
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
    String urlresult, userid, finalfilename;
    Button button, refreshbtn , loadmore;
    TextView emptyText, title;
    RelativeLayout relativeLayout;
    int pagenum, curpage, remaining;
    ProgressBar progressBar;
    View footerview;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_telenovela_annex, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));

        urlresult = getArguments().getString("MovieID");
        listView = (ListView) v.findViewById(R.id.listmoviesparts);
        emptyText = v.findViewById(R.id.emptytext);
        title = v.findViewById(R.id.titletxt);
        relativeLayout = v.findViewById(R.id.backhead);
        adapter = new CustomListAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
        button = (Button)v.findViewById(R.id.backbtn);


        progressBar = v.findViewById(R.id.sBar);
        refreshbtn = v.findViewById(R.id.refreshbtn);
        // Showing progress dialog before making http request

        swipeRefreshLayout = v.findViewById(R.id.seriesannexfrag);
        swipeRefreshLayout.setOnRefreshListener(this);

        footerview =
                ((LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview,null,false);
        loadmore = footerview.findViewById(R.id.btnloadmore);



        refreshbtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);


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

        pagenum = 1;
        curpage = 1;

        listView.addFooterView(footerview);
        footerview.setVisibility(View.GONE);
        loadmore.setVisibility(View.GONE);
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
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)progressBar.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                progressBar.setLayoutParams(layoutParams);
                curpage += 1;
                if(getActivity().getBaseContext().getPackageManager().hasSystemFeature("com.google" +
                        ".android.tv")){
                    getrecords(curpage, "tv", "refresh");
                }else{
                    getrecords(curpage, "other", "refresh");
                }
            }
        });

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
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
                TextView textView = ((TextView)view.findViewById(R.id.mid));
                TextView textView2 = ((TextView)view.findViewById(R.id.msid));

                String movieid = textView.getText().toString();
                String seasonid = textView2.getText().toString() ;

                if (movieid.matches("") || seasonid.matches("")) {
                     //   Toast.makeText(getActivity(),movieid+"\n"+seasonid, Toast.LENGTH_LONG)
                    //   .show();
                    //do nothing
                } else {
                    TelenovelaAnnexEpisodes fg = new TelenovelaAnnexEpisodes();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction trans = fm.beginTransaction();
                    Bundle scanresbundle = new Bundle();
                    scanresbundle.putString("MovieID",
                            "http://192.168.43.189/farmhousemovies/api/seasons_and_series.php?id=" + movieid+"&seriesid="+seasonid+
                                    "&type=Series");
                    scanresbundle.putString("MID", movieid);
                    scanresbundle.putString("SID", seasonid);
                    fg.setArguments(scanresbundle);
                    trans.add(fg, "teleannex");
                    trans.replace(R.id.teleinf, fg);

                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);

                    trans.commit();

                }

            }
        });


        movieList.clear();

        return v;
    }


    public void refreshmvview(){
        movieList.clear();
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


    private void getrecords(int pagenum, String device, String request) {

        // Creating volley request obj
        JsonArrayRequest movieReq =
                new JsonArrayRequest(urlresult+"&user="+userid+"&page="+pagenum+
                        "&device="+device+"&request="+request+"&ptype=normal",
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
                                            JSONObject objcount = response.getJSONObject(0);
                                            Movie movie = new Movie();
                                            title.setText(objcount.getString("title"));
                                            movie.setSeriesName(obj.getString("seriesname"));
                                            movie.setSeriesID(obj.getString("seriesid"));
                                            movie.setPartname(obj.getString("seriesname"));
                                            movie.setTitle(obj.getString("seriesname"));
                                            movie.setThumbnailUrl(obj.getString("image"));
                                            movie.setYear("");
                                            movie.setID(obj.getString("movieID"));
                                            remaining = objcount.getInt("remainingitems");
                                            curpage = Integer.parseInt(obj.getString("pagenum"));
                                            // adding movie to movies array
                                            movieList.add(movie);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }}else{

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
                        Log.d(TAG, "Telenovela Annex Error: " + error.getMessage());



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


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshmvview();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnTeleFragmentInteractionListener {
        // TODO: Update argument type and name
        void onteleFragmentInteraction(String data, String title, String path, String partid,
                                       String partdate, String moviedesc, String favstatus, String movietype, String sid);
    }
}
