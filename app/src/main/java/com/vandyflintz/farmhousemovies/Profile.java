package com.vandyflintz.farmhousemovies;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Profile extends AppCompatActivity implements MainProfile.OnFragmentInteractionListener  {

    public  ViewPager viewpager;
   public  TabLayout tabLayout;
    public SectionsPagerAdapter mSectionsPagerAdapter;
    public GestureDetector.SimpleOnGestureListener listener;
    GestureDetector gd;
    public String screenstate;
    public FCViewPager fcViewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);

        setContentView(R.layout.activity_profile);

        final Context context;

        viewpager = (FCViewPager)findViewById(R.id.mpager);
        tabLayout = findViewById(R.id.mtablayout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.profilemenu);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);

        fcViewPager =  new FCViewPager(this);

        clearallbundles();

        createTabicons();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.home){
                    onBackPressed();
                }
                return false;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position != 1) {
                    tab.getIcon().setColorFilter(Color.parseColor("#BF360C"),
                            PorterDuff.Mode.SRC_IN);
                }

                else{
                    tabLayout.getTabAt(position).setCustomView(null);

                    View v = LayoutInflater.from(Profile.this).inflate(R.layout.custom_tab, null);

                    TextView messagestext = (TextView)v.findViewById(R.id.tabtxt);
                    ImageView messagesimg = v.findViewById(R.id.tabimg);
                    messagestext.setText("100");
                    messagesimg.setImageResource(R.drawable.ic_message_selected);

                    tabLayout.getTabAt(position).setCustomView(v);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position != 1) {
                    tab.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                }

                else{
                    tabLayout.getTabAt(position).setCustomView(null);
                    View v = LayoutInflater.from(Profile.this).inflate(R.layout.custom_tab, null);

                    TextView messagestext = (TextView)v.findViewById(R.id.tabtxt);
                    ImageView messagesimg = v.findViewById(R.id.tabimg);
                    messagestext.setText("100");
                    messagesimg.setImageResource(R.drawable.ic_messages);

                    tabLayout.getTabAt(position).setCustomView(v);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    private void clearallbundles() {
        MainProfile fg = new MainProfile();
        Bundle scanresbundle = new Bundle();
        scanresbundle.putString("user","");
        fg.setArguments(scanresbundle);
    }

    public  void showTabLayout(){
        tabLayout.setVisibility(View.VISIBLE);


    }




    public  void hideTabLayout(){
        tabLayout.setVisibility(View.GONE);
        fcViewPager.setEnableswipe(false);

    }





    private void createTabicons() {


        View v = LayoutInflater.from(Profile.this).inflate(R.layout.custom_tab, null);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_person_white);


        TextView messagestext = (TextView)v.findViewById(R.id.tabtxt);
        ImageView messagesimg = v.findViewById(R.id.tabimg);
        messagestext.setText("100");
        //messagestext.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_messages,0,  0, 0 );
        messagesimg.setImageResource(R.drawable.ic_messages);

        tabLayout.getTabAt(1).setCustomView(v);

        tabLayout.getTabAt(2).setIcon(R.drawable.ic_friends);

        tabLayout.getTabAt(3).setIcon(R.drawable.ic_fav_white);


        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#BF360C"),
                PorterDuff.Mode.SRC_IN);


        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

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
    public void goback() {

    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {




        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }




        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:


                    return new MainProfile();

                case 1:
                    return new InboxMsg();
                case 2:
                    return new Friends();

                case 3:
                    return new FavouritesNew();


                default:
                    return new MainProfile();

            }
        }




        @Override
        public int getCount() {
            return 4;
        }



    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
       // Profile.this.finish();
    }

}
