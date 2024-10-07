package com.vandyflintz.farmhousemovies;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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
import java.util.List;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    Button registerbtn, uploadbtn, skipbtn, donebtn, nextbtn, backbtn;
    public static EditText mEmailView, mPasswordView, mUsernameView, mContactView;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private View mProgressView;
    AlertDialog alertDialog;
    public static ImageView imageView;
    public  Uri filepath;
    String picturepath, filename, ba1;
    public int pick_image_request = 1;
    public static final String upload_key = "image";
    public Bitmap bitmap;
    RelativeLayout relativeLayout;
    public static Cursor cursor;
    public static String userid="", encodedid="";
    public String email,password,contact,uname;
    ProgressDialog pdLoading;
    private static final String TAG = SignupActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        setContentView(R.layout.activity_signup);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mUsernameView = findViewById(R.id.username);
        mContactView = findViewById(R.id.contact);
        relativeLayout = findViewById(R.id.imglayout);
        nextbtn = findViewById(R.id.nextbtn);
        uploadbtn = findViewById(R.id.uploadbtn);
        registerbtn = findViewById(R.id.signupbtn);
        skipbtn = findViewById(R.id.skipbtn);
        donebtn = findViewById(R.id.donebtn);
        imageView = findViewById(R.id.imguploader);
        backbtn = findViewById(R.id.backbtn);

        relativeLayout.setVisibility(View.INVISIBLE);
        registerbtn.setVisibility(View.INVISIBLE);
        donebtn.setVisibility(View.GONE);
        AndroidWorkaround.assistActivity(this);

        pdLoading = new ProgressDialog(SignupActivity.this);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmailView.getText().toString();
                 password = mPasswordView.getText().toString();
                 contact = mContactView.getText().toString();
                 uname = mUsernameView.getText().toString();
                 userid = mContactView.getText().toString();
                View focusView = null;
                String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(uname.length()==0){
                    mUsernameView.setError("Field is required");
                    focusView = mUsernameView;
                }else if(contact.length()==0 || contact.length()<10) {
                    mContactView.setError("Contact cannot be less than 10 digits");
                    focusView = mContactView;
                }else
                if(email.length()==0 || !email.matches(emailpattern)){
                    mEmailView.setError("Email address is invalid");
                    focusView = mEmailView;
                }else if(password.length()==0) {
                    mPasswordView.setError("Field is required");
                    focusView = mPasswordView;
                }else{

                    relativeLayout.setVisibility(View.VISIBLE);


                    if(uploadbtn.getVisibility() == View.INVISIBLE || uploadbtn.getVisibility() == View.GONE){
                       uploadbtn.setVisibility(View.VISIBLE);
                    }

                    if(backbtn.getVisibility() == View.INVISIBLE || backbtn.getVisibility() == View.GONE){
                        backbtn.setVisibility(View.VISIBLE);
                    }

                    if(skipbtn.getVisibility() == View.INVISIBLE || skipbtn.getVisibility() == View.GONE){
                        skipbtn.setVisibility(View.VISIBLE);
                    }

                        registerbtn.setVisibility(View.INVISIBLE);


                }



            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncRegister().execute(uname,email,contact, password, filename);
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadbtn.setVisibility(View.GONE);
                donebtn.setVisibility(View.GONE);
                skipbtn.setVisibility(View.INVISIBLE);
                filename = "";
                imageView.setImageResource(R.drawable.ic_person_white);
                registerbtn.setVisibility(View.VISIBLE);
            }
        });


        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),
                pick_image_request);*/
                Dexter.withActivity(SignupActivity.this)
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



        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               uploadprofilepicture(filename);
            }
        });

        ImagePickerActivity.clearCache(this);

    }

    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void uploadprofilepicture(final String filename) {
       // String  filetobeuploaded = String.valueOf(getBytesImageone(bitmap));
        //Log.d("file", filetobeuploaded);
       new AsyncUploadImage().execute();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
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
        Intent intent = new Intent(SignupActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        //adding user contact if filename is unavailable
        String contact = mContactView.getText().toString();
        intent.putExtra("user", contact);

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
        Intent intent = new Intent(SignupActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    //filepath = data.getData();
                   bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                            uri);
                    filename = data.getExtras().getString("filename");
                    makecircularimage(bitmap);
                    donebtn.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SignupActivity.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(SignupActivity.this, "File not received",
                    Toast.LENGTH_LONG).show();
        }


    }

    public static String getFileNameByUri(Context context, Uri uri) {



        String filename="";
        Uri filepathuri = uri;
        String contact = mContactView.getText().toString();
        if(uri.getScheme().toString().compareTo("content")== 0) {
            cursor = context.getContentResolver().query(uri, null, null, null, null);


            if (Objects.requireNonNull(cursor).moveToFirst()) {
                int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                filepathuri = Uri.parse(cursor.getString(column_index));
                if (filepathuri == null) {
                    filename = contact + ".png";
                } else {
                    filename = Objects.requireNonNull(filepathuri.getLastPathSegment()).toString();
                }
            }

        }else if(uri.getScheme().compareTo("file")==0){
            filename = Objects.requireNonNull(filepathuri.getLastPathSegment()).toString();
        }else {
            filename = filename+"_"+filepathuri.getLastPathSegment();
        }

        return  filename;
    }

    private static Bitmap  makecircularimage(Bitmap bitmap) {
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
        return canvasBitmap;
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
                        .appendQueryParameter("profilepicture", filetobeuploaded)
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

            //pdLoading.dismiss();

            if (result.contains("successful")) {
                new AsyncRegister().execute(uname,email,contact, password, filename);
            }else if (result.contains("file") || result.contains("error")){
                pdLoading.dismiss();
                Toast.makeText(SignupActivity.this, result,Toast.LENGTH_LONG).show();
            }else if (result.contains("exception") || result.contains("unsuccessful")) {
                pdLoading.dismiss();
                Toast.makeText(SignupActivity.this, "Error connecting to server!.",
                        Toast.LENGTH_LONG).show();

            }else if(result != null){
                pdLoading.dismiss();
                Toast.makeText(SignupActivity.this, result,Toast.LENGTH_LONG).show();
            }

        }
    }

    public class AsyncRegister extends AsyncTask<String, String, String>
    {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.43.189/farmhousemovies/api/register.php");


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
               // String boundary = "------------"+System.currentTimeMillis();
                //conn.setRequestProperty("Content-type","multipart/form-data;boundary="+boundary);

                // conn.setRequestMethod("GET");



                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("emailaddress", params[1])
                        .appendQueryParameter("contact", params[2])
                                .appendQueryParameter("password", params[3])
                        .appendQueryParameter("profimg", params[4]);
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

            if(result.contains("exists")) {
                Toast.makeText(SignupActivity.this, "Phone number already exists",
                        Toast.LENGTH_LONG).show();

            }else if(result.contains("successful")){
                /* Here launching another activity when login successful, and headed for the passengers activity.
                 */

                Toast.makeText(SignupActivity.this, "Account successfully created",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                SignupActivity.this.finish();
                startActivity(intent);

            }
            else if (!result.contains("exists") || !result.contains("successful")){

                // If username and password does not match display a error message
                Toast.makeText(SignupActivity.this, "Error creating account, try later",
                        Toast.LENGTH_LONG).show();

            } else if (result.contains("exception") || result.contains("unsuccessful")) {

                Toast.makeText(SignupActivity.this, "Error connecting to server!.",
                        Toast.LENGTH_LONG).show();

            }
        }






        /**
         * Attempts to sign in or register the account specified by the login form.
         * If there are form errors (invalid email, missing fields, etc.), the
         * errors are presented and no actual login attempt is made.
         */


        private boolean isEmailValid(String email) {
            //TODO: Replace this with your own logic
            return email.contains("@");
        }

        private boolean isPasswordValid(String password) {
            //TODO: Replace this with your own logic
            return password.length() > 4;
        }

        /**
         * Shows the progress UI and hides the login form.
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        private void showProgress(final boolean show) {
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

            }
        }






        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    private String getBytesImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedimage = Base64.encodeToString(imageBytes, 0);
        return encodedimage;
    }


    private byte[] getBytesImageone(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
    }

    @Override
    public void onDetachedFromWindow(){
        if(pdLoading != null && pdLoading.isShowing())
        pdLoading.dismiss();
        super.onDetachedFromWindow();
    }

    @Override
    public void  onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hidenavigation();
        }
    }

    @SuppressLint("PrivateApi")
    private void setFlagsOnThePeekView(){

    }

    private void hidenavigation() {
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
    }


}
