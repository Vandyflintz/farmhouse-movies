package com.vandyflintz.farmhousemovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

public class OtherUsers extends AppCompatActivity implements MainProfile.OnFragmentInteractionListener {

    ViewPager viewPager;
    public SectionsPagerAdapter mSectionsPagerAdapter;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        setContentView(R.layout.activity_other_users);



        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if (extras.containsKey("option")) {
            String option = extras.getString("option");
             user = extras.getString("user");

            if(Objects.equals(option, "view")){
               displayprofile(user);
            }else{
                displaymessage(user);
            }
            }

        }

    }

    private void displaymessage(String user) {

        ChatInterface fg = new ChatInterface();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        Bundle scanresbundle = new Bundle();
        scanresbundle.putString("user",user);
        fg.setArguments(scanresbundle);
        trans.replace(R.id.mothercon, fg);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();

    }

    private void displayprofile(String username) {

        MainProfile fg = new MainProfile();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        Bundle scanresbundle = new Bundle();
        scanresbundle.putString("user",username);
        fg.setArguments(scanresbundle);
        trans.replace(R.id.mothercon, fg);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();

    }

    @Override
    public void goback() {
        onBackPressed();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        @Override
        public Fragment getItem(int position) {
            MainProfile fg = new MainProfile();
            Bundle scanresbundle = new Bundle();
            scanresbundle.putString("user",user);
            fg.setArguments(scanresbundle);
            return fg;
        }


        @Override
        public int getCount() {
            return 1;
        }


    }

    @Override
    public void  onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hidenavigation(getWindow());
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
    public void onBackPressed(){
        super.onBackPressed();
    }
}