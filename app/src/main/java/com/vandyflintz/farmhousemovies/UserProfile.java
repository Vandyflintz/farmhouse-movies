package com.vandyflintz.farmhousemovies;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserProfile extends AppCompatActivity implements MainProfile.OnFragmentInteractionListener {

    public ViewPager viewpager;
    public TabLayout tabLayout;
    public GestureDetector.SimpleOnGestureListener listener;
    public FCViewPager fcViewPager;
    Toolbar toolbar;
    public SectionsPagerAdapter mSectionsPagerAdapter;
    AlertDialog alertDialog;
    public boolean exitdiag = false;

   public FragmentRefreshListener getFragmentRefreshListener(){
       return fragmentRefreshListener;
   }

   public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener){
       this.fragmentRefreshListener = fragmentRefreshListener;
   }

    public FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uioptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uioptions);
        setContentView(R.layout.activity_user_profile);

        checkdate();

        viewpager = findViewById(R.id.mpager);
        tabLayout = findViewById(R.id.mtablayout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.profilemenu);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);

        //fcViewPager =  new FCViewPager(this);

        createTabicons();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.home){
                    FavouritesNew frag = new FavouritesNew();
                    frag.clearplayer();
                    onBackPressed();
                }
                return false;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if(position == 1){
                    tab.getIcon().setColorFilter(Color.parseColor("#FFC31A"),
                            PorterDuff.Mode.SRC_IN);
                }else if(position == 2){
                    tab.getIcon().setColorFilter(Color.parseColor("#FF0000"),
                            PorterDuff.Mode.SRC_IN);
                }else {
                    tab.getIcon().setColorFilter(Color.parseColor("#BF360C"),
                            PorterDuff.Mode.SRC_IN);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                    tab.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);



            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    public  void showTabLayout(){
        tabLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);

    }




    public  void hideTabLayout(){
        tabLayout.setVisibility(View.GONE);
        //fcViewPager.setEnableswipe(false);
        toolbar.setVisibility(View.GONE);
    }

    private void createTabicons() {


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_person_white);

        tabLayout.getTabAt(1).setIcon(R.drawable.ic_money);

        tabLayout.getTabAt(2).setIcon(R.drawable.ic_fav_white);


        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#BF360C"),
                PorterDuff.Mode.SRC_IN);


        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onResume(){
        checkdate();
        super.onResume();
    }

    @Override
    public void goback() {

    }

    private void checkdate() {
        Date pdate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String caldate = simpleDateFormat.format(pdate);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String timeserver = "time-a.nist.gov";
                    NTPUDPClient timeClient = new NTPUDPClient();
                    InetAddress inetAddress = InetAddress.getByName(timeserver);
                    TimeInfo timeInfo = timeClient.getTime(inetAddress);
                    long returntime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
                    Date time = new Date(returntime);
                    String intdate = simpleDateFormat.format(time);

                    Timestamp timestampone = Timestamp.valueOf(intdate);
                    Timestamp timestamptwo = Timestamp.valueOf(caldate);

                    timestampone.setNanos(0);
                    timestamptwo.setNanos(0);
                    timestampone.setSeconds(0);
                    timestamptwo.setSeconds(0);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(Home.this, time.toString(), Toast.LENGTH_LONG).show();
                            if(timestampone.after(timestamptwo)){
                                displayinvaliddatedialog(pdate, time);
                            }else{
                                if(alertDialog != null){
                                    alertDialog.dismiss();
                                }
                            }
                        }
                    });

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void displayinvaliddatedialog(Date newdate, Date time) {
        exitdiag = true;
        AlertDialog.Builder adb = new AlertDialog.Builder(UserProfile.this, R.style.myDialogTheme);
        String fdate = newdate.toString();

        adb.setTitle("Warning")
                .setMessage("Your date is inaccurate!"+"\n"+"\n"+
                        "Check your Date and Time settings."+"\n"+"\n"+"Your phone's date is "+fdate.replace("00:00:00 GMT ","")+"\n"+"\n"+"The right date is "+time)
                .setIcon(android.R.drawable.ic_dialog_alert);

        adb.setCancelable(false);
        alertDialog = adb.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(layoutParams);

    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {




        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }




        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:

                   return new MainProfile();

                case 1:
                    return new Season_Sub_Level_One();

                case 2:
                    return new FavouritesNew();


                default:
                    return new MainProfile();
            }
        }




        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed(){
        FavouritesNew frag = new FavouritesNew();
        frag.clearplayer();
        if(!exitdiag) {
            super.onBackPressed();
            UserProfile.this.finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // Fragment frag =
                //getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id
        // .pager+":"+ viewpager.getCurrentItem());


        if (resultCode == 111 || resultCode == 222) {
            if(getFragmentRefreshListener()!=null){
                getFragmentRefreshListener().onRefresh(data.getExtras().getString("response"));
            }

        } else  if (resultCode == 333) {
            String abortedcode = "{\"reason\":\"aborted\",\"code\":\"900\"}";
            if(getFragmentRefreshListener()!=null){
                getFragmentRefreshListener().onRefresh(abortedcode);
            }
        }
    }

    public interface FragmentRefreshListener{
        void onRefresh(String sentcode);
    }

    @Override
    public void  onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );

        }
    }

}