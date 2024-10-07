package com.vandyflintz.farmhousemovies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vandyflintz.farmhousemovies.adapter.CustomMessagesAdapter;
import com.vandyflintz.farmhousemovies.adapter.CustomSearchNamesAdapter;
import com.vandyflintz.farmhousemovies.adapter.NewCommentsAdapter;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.EMessage;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Comments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Comments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Comments extends Fragment implements NewCommentsAdapter.EventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public Comments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Comments.
     */
    // TODO: Rename and change types and number of parameters
    public static Comments newInstance(String param1, String param2) {
        Comments fragment = new Comments();
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

    EditText editText, comment;
    Button button;
    TextView textView, tv, nametv, namereftv, ucom, utime, udate;
    ListView listView, lv, nameslistview;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter radapter;
    Context context;
    AlertDialog alertDialog, alertDialogone;
    static  int uioptions;
   String mesgString, partid, userid, mtype, seasonid;
    public List<Movie> movieList = new ArrayList<Movie>();
    public List<EMessage> messageList = new ArrayList<EMessage>();
    public List<EMessage> namesList = new ArrayList<EMessage>();
    public CustomSearchNamesAdapter adapter;
    public CustomMessagesAdapter namesAdapter;
    public NewCommentsAdapter.EventListener listener;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private ProgressDialog pDialog;
    RequestQueue requestQueue;
    RelativeLayout relativeLayout, relativeLayoutone;
    LinearLayout commentscon;
    GridLayout commentspanel;
    Button sendbtn, cancelbtn;
    Handler handler = new Handler();
    Runnable runnable;
    View parentview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        uioptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ;
        editText = v.findViewById(R.id.commentinput);
        button = v.findViewById(R.id.submitComment);
       // listView = v.findViewById(R.id.messagepanel);
        textView = v.findViewById(R.id.emptycomment);
        tv = v.findViewById(R.id.hiddeninput);
        recyclerView=v.findViewById(R.id.messagepanel);
        nametv = v.findViewById(R.id.hiddennameinput);
        namereftv = v.findViewById(R.id.hiddennamerefinput);
        adapter = new CustomSearchNamesAdapter(getActivity(), movieList);
        parentview = v.findViewById(R.id.commentfrag);
        commentscon = v.findViewById(R.id.commentscon);
        commentspanel = v.findViewById(R.id.commentpanel);
        ucom = v.findViewById(R.id.txtusercom);
        utime = v.findViewById(R.id.txtusertime);
        udate = v.findViewById(R.id.txtuserdate);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        mesgString = tv.getText().toString();
        SharedPreferences sp = getActivity().getSharedPreferences("MoviePartDetails",
                Context.MODE_PRIVATE);
        partid = (sp.getString("MovieID", ""));
        mtype = (sp.getString("MovieType", ""));
        seasonid = (sp.getString("SeasonID", ""));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));

        button.setVisibility(View.INVISIBLE);
        //messagesAdapter = new CustomMessagesAdapter(getActivity(), messageList, listener);
        namesAdapter = new CustomMessagesAdapter(getActivity(), namesList);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listener =  this;
        radapter = new NewCommentsAdapter(getActivity(), messageList, listener);
        recyclerView.setAdapter(radapter);
        //lv.setAdapter(messagesAdapter);
        textView.setVisibility(View.INVISIBLE);
        commentscon.setVisibility(View.GONE);


        getcomments();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new AsyncComment().execute(partid, editText.getText().toString() ,userid,
                      nametv.getText().toString(), mtype, seasonid);

            }
        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==0){
                    button.setVisibility(View.INVISIBLE);
                }else{
                    button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnClickListener(v1 -> {
            popmessagewindow(tv.getText().toString());
            //Toast.makeText(getActivity(), tv.getText().toString(),
                    //Toast.LENGTH_LONG).show();
        });

       /* handler.postDelayed(runnable, 1000);

        runnable = new Runnable() {
            @Override
            public void run() {

                messagesAdapter.notifyDataSetChanged();

                handler.postDelayed(this, 1000);
            }
        };*/






        //new AsyncComments().execute();

        return v;
    }

    private void getcomments() {


        JsonArrayRequest movieReqo = new JsonArrayRequest("http://192.168.43" +
                ".189/farmhousemovies/api/comments" +
                ".php?id="+partid+"&mtype="+mtype+"&otherid="+seasonid+"&userid="+userid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if(response.length() > 0) {

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);


                                    EMessage mess = new EMessage();
                                    commentscon.setVisibility(View.VISIBLE);
                                    commentspanel.setVisibility(View.GONE);
                                    textView.setVisibility(View.INVISIBLE);
                                    mess.setTitle(obj.getString("name"));
                                    mess.setEmail(obj.getString("email"));
                                    mess.setMessage(obj.getString("comment"));
                                    mess.setTime(obj.getString("time"));
                                    mess.setDate(obj.getString("date"));
                                    mess.setContact(obj.getString("contact"));
                                    mess.setThumbnailUrl(obj.getString("dp"));
                                    mess.setRegisterednames(obj.getString("allnames"));

                                    ucom.setText(obj.getString("comment"));
                                    udate.setText(obj.getString("time"));
                                    utime.setText(obj.getString("date"));

                                    // adding comments to messages array

                                    messageList.add(mess);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{
                            commentscon.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Tell us your opinion about the movie.");
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        radapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        //requestQueue.add(movieReqo);
        AppController.getInstance().addToRequestQueue(movieReqo);
    }


    public void refreshcomments(){

                   getcomments();
    }

    public void popmessagewindow(String message){

            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(),
                    R.style.myDialogTheme);


            View mydialog =
                    getLayoutInflater().inflate(R.layout.fragment_dialog_fragment_one,
                            (ViewGroup) null);



            adb.setView(mydialog);
            EditText comment = (EditText)mydialog.findViewById(R.id.comment);
            Button sendbtn = (Button)mydialog.findViewById(R.id.sendcomment);
            Button cancelbtn = (Button)mydialog.findViewById(R.id.cancelcomment);
            ListView namesview = mydialog.findViewById(R.id.listnames);
            relativeLayout = mydialog.findViewById(R.id.listnamescon);
            adapter = new CustomSearchNamesAdapter(getActivity(), movieList);
            namesview.setAdapter(adapter);


            namesview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = ((TextView)view.findViewById(R.id.ntitle));
                    String namesgot = textView.getText().toString();
                    String originalstring = comment.getText().toString();

                    int cursorposition = comment.getSelectionEnd();

                    int endindex = originalstring.lastIndexOf("@")+1;
                    String newname = originalstring.substring(0, endindex);
                    nametv.append("@"+namesgot+":");
                    comment.getText().replace(endindex, cursorposition, "").toString();
                    comment.append(namesgot+" ");



                    movieList.clear();
                    namesview.invalidateViews();
                    adapter.notifyDataSetChanged();
                    relativeLayout.setVisibility(View.GONE);
                }
            });

                 relativeLayout.setVisibility(View.GONE);

                comment.setText(message);


                comment.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_SPACE){
                           if(relativeLayout.getVisibility()== View.VISIBLE){
                               relativeLayout.setVisibility(View.GONE);
                           }

                        }





                        return false;
                    }
                });




            comment.addTextChangedListener(new TextWatcher() {
                int textl;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    textl = comment.getText().length();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int nl = s.length();
                 try{
                    if(textl > nl && relativeLayout.getVisibility() == View.VISIBLE){
                        String originalstring = comment.getText().toString();
                        String names = originalstring.substring(originalstring.lastIndexOf("@")+1);
                        String namesleft =
                                originalstring.substring(originalstring.lastIndexOf("@"));
                        if( namesleft.length() > 1) {
                            //searchnames(names);
                        }else if(namesleft.length() < 2 && namesleft.length() > 0){
                            //showallnames();
                        }
                        else if(namesleft.length() < 1){
                            relativeLayout.setVisibility(View.GONE);
                        }



                    }
                }catch (Exception e){
                    //
                     relativeLayout.setVisibility(View.GONE);
                }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                   try{
                    String letter = String.valueOf(s.charAt(start + before));

                    if(letter.contains("@")) {
                        //firenameslist();

                    }
                    else if(relativeLayout.getVisibility() == View.VISIBLE) {
                        String originalstring = comment.getText().toString();
                        String names = originalstring.substring(originalstring.lastIndexOf("@")+1);
                        //searchnames(names);
                    }
                }catch (Exception e){
                       //
                   }


                }

                private void showallnames() {

                    movieList.clear();
                    namesview.invalidateViews();
                    adapter.notifyDataSetChanged();
                    JsonArrayRequest movieReq = new JsonArrayRequest("http://192.168.43" +
                            ".189/farmhousemovies/api/searchnames" +
                            ".php?q=@"+"&id="+partid+"&user="+userid,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d(TAG, response.toString());

                                    if(response.length() > 0) {
                                        // Parsing json
                                        for (int i = 0; i < response.length(); i++) {
                                            try {

                                                JSONObject obj = response.getJSONObject(i);

                                                Movie movie = new Movie();


                                                movie.setTitle(obj.getString("name"));
                                                movie.setThumbnailUrl(obj.getString("dp"));

                                                // adding names to movies array
                                                movieList.add(movie);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }else{

                                        relativeLayout.setVisibility(View.GONE);
                                    }

                                    // notifying list adapter about data changes
                                    // so that it renders the list view with updated data
                                    adapter.notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Log.d(TAG, "Error: " + error.getMessage());

                        }
                    });

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(movieReq);
                }

                private void searchnames(String names) {
                    movieList.clear();
                    namesview.invalidateViews();
                    adapter.notifyDataSetChanged();

                    JsonArrayRequest movieReq = new JsonArrayRequest("http://192.168.43" +
                            ".189/farmhousemovies/api/searchnames" +
                            ".php?q="+names+"&id="+partid+"&user="+userid,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d(TAG, response.toString());

                                    if(response.length() > 0) {
                                        // Parsing json
                                        for (int i = 0; i < response.length(); i++) {
                                            try {

                                                JSONObject obj = response.getJSONObject(i);

                                                Movie movie = new Movie();


                                                    movie.setTitle(obj.getString("name"));
                                                     movie.setThumbnailUrl(obj.getString("dp"));

                                                // adding names to movies array
                                                movieList.add(movie);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }else{

                                        relativeLayout.setVisibility(View.GONE);
                                    }

                                    // notifying list adapter about data changes
                                    // so that it renders the list view with updated data
                                    adapter.notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Log.d(TAG, "Error: " + error.getMessage());

                        }
                    });

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(movieReq);
                }

                private void firenameslist() {
                  //Toast.makeText(getActivity(), userid +" "+partid, Toast.LENGTH_LONG).show();
                    relativeLayout.setVisibility(View.VISIBLE);
                   // showallnames();
                }


            });


            sendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getActivity().runOnUiThread(new Runnable() {
                        String enteredComment = comment.getText().toString();
                        @Override
                        public void run() {
                            editText.setText(enteredComment);
                            tv.setText(enteredComment);
                            //send text to original edittext

                            alertDialog.dismiss();

                            InputMethodManager imm =
                                    (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                        }
                    });






                }
            });

            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment.setCursorVisible(false);

                    alertDialog.dismiss();

                }
            });

            alertDialog = adb.create();

            alertDialog.getWindow().getDecorView().setSystemUiVisibility(uioptions);
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);


        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm =
                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }
        });

    }

    @Override
    public void onEvent(String data) {
        shownameprofile(data);
       // Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProfilepicture(String con) {
        showprofiledialog(con);
    }

    private void showprofiledialog(String con) {
        String url = "http://192.168.43.189/farmhousemovies/api/profileinfo.php?id="+con+"&userid="+userid;
        //show profile picture dialog
        //Toast.makeText(getActivity(), con, Toast.LENGTH_LONG).show();

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);

                                    String username = obj.getString("user");
                                    String img = obj.getString("dp");
                                    String requeststatus = obj.getString("friendstatus");
                                    String contact = obj.getString("contact");

                                  showinfo(username,img,requeststatus,contact);



                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                Toast.makeText(getActivity(), error.getMessage() , Toast.LENGTH_LONG).show();

                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    private void showinfo(String username, String img, String requeststatus, String contact) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());


        View mydialog =
                getLayoutInflater().inflate(R.layout.profile_pic_dialog,
                        (ViewGroup) null);

        adb.setView(mydialog);

        TextView minname, maxname, blocktxt, unblocktxt;
        ImageView minimage, maximage;
        RelativeLayout maxrel, minrel, relativeLayout;
        Button backbtn, sendrequestbtn, msgbtn, infobtn;
        ImageButton btnmsg, btnblocked, btnfriends, btnsendrequest, btnpending, btnaccept;

        blocktxt = mydialog.findViewById(R.id.blockbtn);
        unblocktxt = mydialog.findViewById(R.id.unblockbtn);
        btnsendrequest = mydialog.findViewById(R.id.sendreqbtn);
        btnblocked = mydialog.findViewById(R.id.blockedbtn);
        btnpending = mydialog.findViewById(R.id.pendingbtn);
        btnaccept = mydialog.findViewById(R.id.acceptbtn);
        btnfriends = mydialog.findViewById(R.id.alreadyfriendsbtn);
        relativeLayout = mydialog.findViewById(R.id.overallcon);
        minname = mydialog.findViewById(R.id.uname);
        maxname = mydialog.findViewById(R.id.fullscreenname);
        minimage = mydialog.findViewById(R.id.profileimg);
        maximage = mydialog.findViewById(R.id.fullscreenprofile);
        maxrel = mydialog.findViewById(R.id.fullscreenrel);
        backbtn = mydialog.findViewById(R.id.backbtn);
        msgbtn = mydialog.findViewById(R.id.msgbtn);
        sendrequestbtn = mydialog.findViewById(R.id.addfriendbtn);
        infobtn = mydialog.findViewById(R.id.infobtn);
        minrel = mydialog.findViewById(R.id.mainprofile);

        btnblocked.setVisibility(View.GONE);
        btnsendrequest.setVisibility(View.GONE);
        btnfriends.setVisibility(View.GONE);
        btnpending.setVisibility(View.GONE);
        btnaccept.setVisibility(View.GONE);
        blocktxt.setVisibility(View.GONE);
        unblocktxt.setVisibility(View.GONE);
        sendrequestbtn.setVisibility(View.GONE);
        maxrel.setVisibility(View.INVISIBLE);
        minname.setText(username);
        maxname.setText(username);

        switch (requeststatus) {
            case "friends":
                btnpending.setVisibility(View.GONE);
                btnblocked.setVisibility(View.GONE);
                btnsendrequest.setVisibility(View.GONE);
                btnfriends.setVisibility(View.VISIBLE);
                btnaccept.setVisibility(View.GONE);
                blocktxt.setVisibility(View.VISIBLE);
                unblocktxt.setVisibility(View.GONE);
                break;
            case "blocked":
                btnpending.setVisibility(View.GONE);
                btnblocked.setVisibility(View.VISIBLE);
                btnsendrequest.setVisibility(View.GONE);
                btnfriends.setVisibility(View.GONE);
                btnaccept.setVisibility(View.GONE);
                blocktxt.setVisibility(View.GONE);
                unblocktxt.setVisibility(View.GONE);
                break;
            case "ublocked":
                btnpending.setVisibility(View.GONE);
                btnblocked.setVisibility(View.GONE);
                btnsendrequest.setVisibility(View.GONE);
                btnfriends.setVisibility(View.GONE);
                btnaccept.setVisibility(View.GONE);
                blocktxt.setVisibility(View.GONE);
                unblocktxt.setVisibility(View.VISIBLE);
                break;
            case "pending_acceptance":
                btnpending.setVisibility(View.GONE);
                btnblocked.setVisibility(View.GONE);
                btnsendrequest.setVisibility(View.GONE);
                btnfriends.setVisibility(View.GONE);
                btnaccept.setVisibility(View.VISIBLE);
                blocktxt.setVisibility(View.GONE);
                unblocktxt.setVisibility(View.GONE);
                break;

            case "not_friends_but_pending":
                btnpending.setVisibility(View.VISIBLE);
                btnblocked.setVisibility(View.GONE);
                btnsendrequest.setVisibility(View.GONE);
                btnfriends.setVisibility(View.GONE);
                btnaccept.setVisibility(View.GONE);
                blocktxt.setVisibility(View.GONE);
                unblocktxt.setVisibility(View.GONE);
                break;
            case "not friends":
                btnpending.setVisibility(View.GONE);
                btnblocked.setVisibility(View.GONE);
                btnsendrequest.setVisibility(View.VISIBLE);
                btnfriends.setVisibility(View.GONE);
                btnaccept.setVisibility(View.GONE);
                blocktxt.setVisibility(View.GONE);
                unblocktxt.setVisibility(View.GONE);
                break;
        }


        Picasso.with(getActivity()).load(img).into(minimage);
        Picasso.with(getActivity()).load(img).into(maximage);


        msgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OtherUsers.class);
                i.putExtra("option","message");
                i.putExtra("user",username);
                alertDialogone.dismiss();
                startActivity(i);
            }
        });

        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OtherUsers.class);
                i.putExtra("option","view");
                i.putExtra("user",username);
                alertDialogone.dismiss();
                startActivity(i);
            }
        });


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertDialogone.dismiss();
            }
        });

        minrel.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) minrel.getLayoutParams();

                int percentwidth = (int) (width*.80);
                params.height = height;
                params.width = percentwidth;
                minrel.setLayoutParams(params);
            }
        });

        minimage.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) minimage.getLayoutParams();
                int percentheight = (int) (height*.40);
                int percentwidth = width;
                params.height = percentheight;
               params.width = percentwidth;
                minimage.setLayoutParams(params);
            }
        });



        maximage.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) maximage.getLayoutParams();
                int percentheight = (int) (height*.80);
                params.width = width;
                params.height = percentheight;
                maximage.setLayoutParams(params);
            }
        });

        minimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogone.setCanceledOnTouchOutside(false);
                maxrel.setVisibility(View.VISIBLE);
                alertDialogone.setCancelable(false);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxrel.setVisibility(View.INVISIBLE);
                alertDialogone.setCancelable(true);
                alertDialogone.setCanceledOnTouchOutside(true);
            }
        });

        alertDialogone = adb.create();
        alertDialogone.getWindow().getDecorView().setSystemUiVisibility(uioptions);
        alertDialogone.show();
        alertDialogone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = alertDialogone.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialogone.setCanceledOnTouchOutside(true);
        alertDialogone.setCancelable(true);




    }

    public void shownameprofile(String num) {
        //Toast.makeText(getActivity(), data , Toast.LENGTH_LONG).show();

        Intent i = new Intent(getActivity(), OtherUsers.class);
        i.putExtra("option","view");
        i.putExtra("user",num);
        startActivity(i);
    }


    public class AsyncComment extends AsyncTask<String, String, String>
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
                url = new URL("http://192.168.43.189/farmhousemovies/api/comments.php");

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
                        .appendQueryParameter("comment", params[1])
                        .appendQueryParameter("userid", params[2])
                        .appendQueryParameter("refnames", params[3])
                        .appendQueryParameter("mtype", params[4])
                        .appendQueryParameter("seasonid", params[5]);
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

            if(result.contains("commented")) {
                tv.setText("");
                editText.setText("");
                commentspanel.setVisibility(View.GONE);
                messageList.clear();
                Log.d("Commented Status: ", "Successfully commented");
                Toast.makeText(getActivity(), "Comment made",
                        Toast.LENGTH_LONG).show();

                        refreshcomments();


            }
            else if (!result.contains("commented")  || result.contains("error")){

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

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

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
