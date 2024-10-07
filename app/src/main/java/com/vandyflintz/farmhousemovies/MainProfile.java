package com.vandyflintz.farmhousemovies;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.vandyflintz.farmhousemovies.adapter.CustomProfileAdapter;
import com.vandyflintz.farmhousemovies.adapter.ProfileAdapter;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.EMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
 * Use the {@link MainProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainProfile extends Fragment implements CustomProfileAdapter.EventListenerone, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainProfile.OnFragmentInteractionListener mListener;

    public MainProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static MainProfile newInstance(String param1, String param2) {
        MainProfile fragment = new MainProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ListView listView;
    ImageButton btnmsg, btnblocked, btnfriends, btnsendrequest, btnpending, btnaccept;
    public static ImageView imageView;
    static LinearLayout piccon;
    LinearLayout othercon;
    Button magnify, upload, toggle;
    TextView username, headline, baseline, useremail, userphone, friendscount, editinfo, blocktxt
            , unblocktxt, emptytext;
    RelativeLayout relcon;
    public CustomProfileAdapter adapter;
    String command,  result;
    String partid;
    static String userid;
    public List<EMessage> messageList = new ArrayList<EMessage>();
    private ProgressDialog pDialog;
    public static final int REQUEST_IMAGE = 100;
    static ProgressDialog pdLoading;
    ProgressBar progressBar;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public static Bitmap bitmap;
    public static Bitmap blurredBitmap;
    static String filename;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter radapter;
    public RecyclerView.LayoutManager layoutManager;
    public ProfileAdapter.EventListener elistener;
    public CustomProfileAdapter.EventListenerone listener;
    public String imgtag;
    Bundle arguments;
    AlertDialog commentsdialog, subprofiledialog;
    ExpandableListView expandableListView;
    Button homebtn;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_main_profile, container, false);
        listView = v.findViewById(R.id.picturespanel);
        btnmsg = v.findViewById(R.id.msgbtn);
        swipeRefreshLayout = v.findViewById(R.id.profilefrag);
        swipeRefreshLayout.setOnRefreshListener(this);
        btnfriends = v.findViewById(R.id.alreadyfriendsbtn);
        btnsendrequest = v.findViewById(R.id.sendreqbtn);
        btnblocked = v.findViewById(R.id.blockedbtn);
        btnpending = v.findViewById(R.id.pendingbtn);
        emptytext = v.findViewById(R.id.emptytext);
        btnaccept = v.findViewById(R.id.acceptbtn);
        imageView = v.findViewById(R.id.imguploader);
        piccon = v.findViewById(R.id.picCon);
        magnify = v.findViewById(R.id.magbtn);
        upload = v.findViewById(R.id.cambtn);
        toggle = v.findViewById(R.id.togglebtn);
        relcon = v.findViewById(R.id.relcon);
        username = v.findViewById(R.id.username);
        useremail = v.findViewById(R.id.useremail);
        userphone = v.findViewById(R.id.userphone);
        headline = v.findViewById(R.id.headline);
        baseline = v.findViewById(R.id.baseline);
        friendscount = v.findViewById(R.id.friendscount);
        editinfo = v.findViewById(R.id.editinfotxt);
        othercon = v.findViewById(R.id.othercon);
        blocktxt = v.findViewById(R.id.blockbtn);
        unblocktxt = v.findViewById(R.id.unblockbtn);
        homebtn = v.findViewById(R.id.btnhome);
        progressBar = v.findViewById(R.id.sBar);
        pdLoading = new ProgressDialog(getActivity());
        piccon.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        btnblocked.setVisibility(View.GONE);
        btnsendrequest.setVisibility(View.GONE);
        btnfriends.setVisibility(View.GONE);
        btnpending.setVisibility(View.GONE);
        btnaccept.setVisibility(View.GONE);
        othercon.setVisibility(View.GONE);
        blocktxt.setVisibility(View.GONE);
        unblocktxt.setVisibility(View.GONE);
        emptytext.setVisibility(View.INVISIBLE);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact",""));

        adapter = new CustomProfileAdapter(getActivity(), messageList, this);
        listView.setAdapter(adapter);


        arguments = getArguments();
        if(arguments != null && arguments.containsKey("user")){
            result = getArguments().getString("user");
            displayuserprofile(result);
            homebtn.setVisibility(View.VISIBLE);
        }else{
            displaypersonalprofile(userid);
            homebtn.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result == null){

                    if (piccon.getVisibility() == View.VISIBLE) {
                        piccon.setVisibility(View.GONE);

                    } else {
                        piccon.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relcon.getVisibility()== View.VISIBLE){
                    relcon.setVisibility(View.GONE);
                    headline.setVisibility(View.INVISIBLE);
                    baseline.setVisibility(View.INVISIBLE);
                    relcon.setVisibility(View.GONE);
                    toggle.setBackgroundResource(R.drawable.reveal_btn);
                }else{
                    relcon.setVisibility(View.VISIBLE);
                    headline.setVisibility(View.VISIBLE);
                    baseline.setVisibility(View.VISIBLE);
                    toggle.setBackgroundResource(R.drawable.hide_btn);
                }
            }
        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goback();
            }
        });



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
              /*  if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    swipeRefreshLayout.setEnabled(true);
                    //Toast.makeText(getActivity(),"scrolling ended", Toast.LENGTH_LONG).show();
                }
               /* if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING ||
               scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    swipeRefreshLayout.setEnabled(false);
                }*/
              if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE || scrollState == 0){
                  swipeRefreshLayout.setEnabled(true);
              }else{
                  swipeRefreshLayout.setEnabled(false);
              }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Toast.makeText(getActivity(),"scrolling started", Toast.LENGTH_LONG).show();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }


                        }).check();
            }
        });


        magnify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showprofiledialog(imgtag);
                piccon.setVisibility(View.GONE);
            }
        });

        return v;
    }

    private void enlargeimage(String img) {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());


        View mydialog =
                getLayoutInflater().inflate(R.layout.profile_pic_dialog,
                        (ViewGroup) null);

        adb.setView(mydialog);

        ImageView maximage;
        Button updatebtn;
        RelativeLayout maxrel, minrel, relativeLayout;
        Button backbtn;

        relativeLayout = mydialog.findViewById(R.id.overallcon);
        maximage = mydialog.findViewById(R.id.fullscreenprofile);
        maxrel = mydialog.findViewById(R.id.fullscreenrel);
        backbtn = mydialog.findViewById(R.id.backbtn);
        minrel = mydialog.findViewById(R.id.mainprofile);
        updatebtn = mydialog.findViewById(R.id.updatebtn);
        minrel.setVisibility(View.GONE);
        Picasso.with(getActivity()).load("http://192.168.43.189/farmhousemovies/api/dp/"+img).into(maximage);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subprofiledialog.dismiss();
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
                int percentheight = (int) (height*.60);
                params.width = width;
                params.height = percentheight;
                maximage.setLayoutParams(params);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             subprofiledialog.dismiss();
            }
        });


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity(), R.style.myDialogTheme).setTitle("Message")
                        .setMessage("Sure about using it as your profile picture?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int buttonselected) {
                           new AsyncUpdateImage().execute(userid,img);
                            }
                        }).setNegativeButton(android.R.string.no, null).setCancelable(true).show();
            }
        });

        subprofiledialog = adb.create();
        subprofiledialog.getWindow().getDecorView().setSystemUiVisibility(uioptions);
        subprofiledialog.show();
        subprofiledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = subprofiledialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        subprofiledialog.setCanceledOnTouchOutside(true);
        subprofiledialog.setCancelable(true);
    }


    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(),
                new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }


    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        //adding user contact if filename is unavailable

        intent.putExtra("user", userid);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void displaypersonalprofile(String userid) {

        messageList.clear();
        listView.invalidateViews();


        String url = "http://192.168.43.189/farmhousemovies/api/gallery.php?id="+userid;
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
                                EMessage msg = new EMessage();
                                progressBar.setVisibility(View.INVISIBLE);
                                emptytext.setVisibility(View.INVISIBLE);
                                String username = obj.getString("user");
                                String img = obj.getString("dp");
                                String contact = obj.getString("contact");
                                String friends = obj.getString("friends");
                                String email = obj.getString("email");


                                if(obj.has("picture")){
                                    msg.setTitle(obj.getString("user"));
                                    msg.setThumbnailUrl(obj.getString("dp"));
                                    msg.setImageurl(obj.getString("picture"));
                                    msg.setTime(obj.getString("time"));
                                    msg.setContact(obj.getString("contact"));
                                }else{
                                    msg.setTitle(obj.getString("user"));
                                    msg.setThumbnailUrl(obj.getString("dp"));
                                    msg.setImageurl(obj.getString("dp"));
                                    msg.setContact(obj.getString("contact"));
                                }

                                messageList.add(msg);
                                displaypersonalinfo(username,img,contact,friends,email);


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
                progressBar.setVisibility(View.INVISIBLE);

                final Handler searchhandler = new Handler();
                searchhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptytext.setVisibility(View.VISIBLE);
                        emptytext.setText("Oops, error encountered: Retry");

                    }
                }, 500);

                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    private void displaypersonalinfo(String uname, String img, String contact, String friends,
                              String email) {
        Picasso.with(getActivity().getApplicationContext()).load(img).transform(new CircleTransform()).into(imageView);
        String imgname =
                img.substring(img.lastIndexOf("/") + 1);
        imgtag = contact+";"+imgname;
        username.setText(uname);
        userphone.setText(contact);
        useremail.setText(email);
        friendscount.setText(friends);

    }

    private void displayuserprofile(String user) {
       //Toast.makeText(getActivity(), user +" to json", Toast.LENGTH_LONG).show();
        othercon.setVisibility(View.VISIBLE);
        String url = "http://192.168.43.189/farmhousemovies/api/gallery.php?userid="+userid+"&other="+user;
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
                                EMessage movie = new EMessage();

                                String username = obj.getString("user");
                                String img = obj.getString("dp");
                                String contact = obj.getString("contact");
                                String friends = obj.getString("friends");
                                String email = obj.getString("email");
                                String status = obj.getString("status");

                                if(obj.has("picture")){
                                    movie.setTitle(obj.getString("user"));
                                    movie.setThumbnailUrl(obj.getString("dp"));
                                    movie.setImageurl(obj.getString("picture"));
                                    movie.setTime(obj.getString("time"));
                                    movie.setContact(obj.getString("contact"));
                                }else{
                                    movie.setTitle(obj.getString("user"));
                                    movie.setThumbnailUrl(obj.getString("dp"));
                                    movie.setImageurl(obj.getString("dp"));
                                    movie.setContact(obj.getString("contact"));
                                }

                                messageList.add(movie);
                                displayinfo(username,img,contact,friends,email, status);


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
                Log.d(TAG, "Error: " + error.getMessage());
                //Toast.makeText(getActivity(), error.getMessage() , Toast.LENGTH_LONG).show();

                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void displayinfo(String uname, String img, String contact, String friends, String email, String status) {
        Picasso.with(getActivity().getApplicationContext()).load(img).transform(new CircleTransform()).into(imageView);

        username.setText(uname);
        userphone.setText(contact);
        useremail.setText(email);
        friendscount.setText(friends);

        String imgname =
                img.substring(img.lastIndexOf("/") + 1);
        imgtag = contact+";"+imgname;

        if(contact.trim().equals(userid)){
            editinfo.setVisibility(View.VISIBLE);
            othercon.setVisibility(View.GONE);
        }else{
            editinfo.setVisibility(View.GONE);
            othercon.setVisibility(View.VISIBLE);
        }

        switch (status) {
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

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public void onLike(String data) {

    }

    @Override
    public void onDisplayProfilepicture(String con) {
        showprofiledialog(con);
    }



    public void showprofiledialog(String con) {
       // Toast.makeText(getActivity(), con + " view", Toast.LENGTH_LONG).show();

        String imgname =
                con.substring(con.indexOf(";") + 1);
        String contact =
                con.split(";")[0];
        //opendialog(contact, imgname);
        enlargeimage(imgname);
    }

    private void opendialog(String contact, String imgname) {

        ImageView profimg;
        TextView profname;
        ImageButton next, prev, expand, minimize;
        Button commentbtn;
        EditText comment;


        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        View mydialog =
                getLayoutInflater().inflate(R.layout.picture_comments,
                        (ViewGroup) null);

        adb.setView(mydialog);

        profimg = mydialog.findViewById(R.id.profileimg);
        profname = mydialog.findViewById(R.id.uname);

      Picasso.with(getActivity()).load("http://192.168.43.189/farmhousemovies/api/dp/"+imgname).into(profimg);
      profname.setText(contact);

        commentsdialog = adb.create();
        commentsdialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(commentsdialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        commentsdialog.getWindow().setAttributes(layoutParams);
        commentsdialog.getWindow().getDecorView().setSystemUiVisibility(uioptions);
        commentsdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        commentsdialog.setCanceledOnTouchOutside(false);


        commentsdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm =
                        (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }
        });
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
        messageList.clear();
        listView.invalidateViews();
        if(arguments != null && arguments.containsKey("user")){
            result = getArguments().getString("user");
            displayuserprofile(result);
            homebtn.setVisibility(View.VISIBLE);
        }else{
            displaypersonalprofile(userid);
            homebtn.setVisibility(View.GONE);
        }
    }

    public interface OnFragmentInteractionListener {
        void goback();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainProfile.OnFragmentInteractionListener) {
            mListener = (MainProfile.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public class CircleTransform implements Transformation{

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size)/2;
            int y = (source.getHeight() - size)/2;

            Bitmap squaredbitmap = Bitmap.createBitmap(source, x, y, size, size);
            if(squaredbitmap != source){
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredbitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size/ 2f;
            canvas.drawCircle(r,r,r,paint);

            squaredbitmap.recycle();

            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


    private Bitmap  makecircularimage(Bitmap bitmap) {
        float radius = bitmap.getWidth() > bitmap.getHeight() ? ((float) bitmap.getHeight())/ 2f:
                ((float) bitmap.getWidth())/ 2f;
        Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        canvas.drawCircle(bitmap.getWidth()/ 2, bitmap.getHeight()/2, radius, paint);
        imageView.setImageBitmap(canvasBitmap);
        piccon.setVisibility(View.GONE);

        messageList.clear();
        listView.invalidateViews();
        if(arguments != null && arguments.containsKey("user")){
            result = getArguments().getString("user");
            displayuserprofile(result);
            homebtn.setVisibility(View.VISIBLE);
        }else{
            displaypersonalprofile(userid);
            homebtn.setVisibility(View.GONE);
        }

        return canvasBitmap;
    }

    private static String getBytesImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedimage = Base64.encodeToString(imageBytes, 0);
        return encodedimage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    //filepath = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                            uri);
                    filename = data.getExtras().getString("filename");

                    new AsyncUploadImage().execute();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(getActivity(), "File not received",
                    Toast.LENGTH_LONG).show();
        }


    }

    public class AsyncUploadImage extends AsyncTask<Void,Void,String> {


        HttpURLConnection conn;
        URL url = null;
        String filetobeuploaded;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tPlease wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }


        @Override
        protected String doInBackground(Void... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.43.189/farmhousemovies/api/imgupload.php");



            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                Log.d("log_tag", "Error in http connection "+e.toString());
                e.printStackTrace();
                return "exception";
            }try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                //String boundary = "------------"+System.currentTimeMillis();
                //conn.setRequestProperty("Content-type","multipart/form-data;boundary="+boundary);

                filetobeuploaded = getBytesImage(bitmap);
                // conn.setRequestMethod("GET");



                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("updateprofilepicture", filetobeuploaded)
                        .appendQueryParameter("imgname", filename)
                        .appendQueryParameter("user", userid);
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

            pdLoading.dismiss();

            if (result.contains("successful")) {
                makecircularimage(bitmap);
                Toast.makeText(getActivity(), "Profile picture updated successfully",
                        Toast.LENGTH_LONG).show();

            }else if (result.contains("file") || result.contains("error")){

                Toast.makeText(getActivity(), result,Toast.LENGTH_LONG).show();
            }else if (result.contains("exception") || result.contains("unsuccessful")) {

                Toast.makeText(getActivity(), "Error connecting to server!.",
                        Toast.LENGTH_LONG).show();

            }else if(result != null){

                Toast.makeText(getActivity(), result,Toast.LENGTH_LONG).show();
            }

        }
    }

    public class AsyncUpdateImage extends AsyncTask<String,String,String> {


        HttpURLConnection conn;
        URL url = null;
        String filetobeuploaded;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tPlease wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }


        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.43.189/farmhousemovies/api/imgupload.php?user="+params[0]+
                        "&imgname="+params[1]);



            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                Log.d("log_tag", "Error in http connection "+e.toString());
                e.printStackTrace();
                return "exception";
            }try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("imgname", params[1])
                        .appendQueryParameter("user", params[0]);
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

            pdLoading.dismiss();

            if (result.contains("successful")) {
                displaypersonalprofile(userid);
                subprofiledialog.dismiss();
                Toast.makeText(getActivity(), "Profile picture updated successfully",
                        Toast.LENGTH_LONG).show();

            }else if (result.contains("file") || result.contains("error")){

                Toast.makeText(getActivity(), result,Toast.LENGTH_LONG).show();
            }else if (result.contains("exception") || result.contains("unsuccessful")) {

                Toast.makeText(getActivity(), "Error connecting to server!.",
                        Toast.LENGTH_LONG).show();

            }else if(result != null){

                Toast.makeText(getActivity(), result,Toast.LENGTH_LONG).show();
            }

        }
    }

}
