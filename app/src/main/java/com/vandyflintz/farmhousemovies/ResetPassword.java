package com.vandyflintz.farmhousemovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ResetPassword extends AppCompatActivity {
    String npass, cpass, user;
    Button resetbtn;
    EditText phone, npassword, cpassword;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetbtn = findViewById(R.id.resetbtn);
        phone = findViewById(R.id.username);
        npassword = findViewById(R.id.npassword);
        cpassword = findViewById(R.id.cpassword);


        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                npass = npassword.getText().toString();
                cpass = cpassword.getText().toString();
                user = phone.getText().toString();

                if(!npass.equals(cpass)){
                    Toast.makeText(ResetPassword.this, "Passwords don't match!", Toast.LENGTH_LONG).show();
                }else{
                    new AsyncReset().execute(user,cpass);
                }
            }
        });


    }


    public class AsyncReset extends AsyncTask<String, String, String>
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
                url = new URL("http://192.168.43.189/farmhousemovies/api/resetpassword.php");

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
                        .appendQueryParameter("user", params[0])
                        .appendQueryParameter("passw", params[1]);
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

            if(result.contains("negative")){

                Toast.makeText(ResetPassword.this, "Account does not exist ",
                        Toast.LENGTH_LONG).show();
            }

           else if(result.contains("updated")) {


                Toast.makeText(ResetPassword.this, "Password successfully reset",
                        Toast.LENGTH_LONG).show();

                Intent i = new Intent(ResetPassword.this, SigninActivity.class);
                startActivity(i);
            }
            else if (!result.contains("updated")  || result.contains("error")){

                Log.d("Rating Error: ","unable to complete request");
                Toast.makeText(ResetPassword.this, "Error processing request, try again later.",
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
