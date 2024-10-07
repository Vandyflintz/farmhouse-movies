package com.vandyflintz.farmhousemovies;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    Context context;


    PreferencesManager (Context context){
        this.context = context;
    }

    public void saveMovieDetails( String mvn, String title){
        SharedPreferences sp = context.getSharedPreferences("MovieDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("MoviePath", mvn);
        editor.putString("MovieTitle", title);
        editor.commit();
    }

    public void savePartDetails(String mid, String mdate, String mdesc, String favstatus,
                                String mtype, String seasonid){
        SharedPreferences sp = context.getSharedPreferences("MoviePartDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("MovieID", mid);
        editor.putString("MovieDate", mdate);
        editor.putString("MovieDescription", mdesc);
        editor.putString("Favstatus", favstatus);
        editor.putString("MovieType", mtype);
        editor.putString("SeasonID",seasonid);
        editor.commit();
    }

    public void saveAlertDetails(String movietype, String movieid, String sid, String partid,
                                 String userid, String mPrice, String mName){
        SharedPreferences sp = context.getSharedPreferences("AlertDialogDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Mtype",movietype);
        editor.putString("Mid",movieid);
        editor.putString("Sid",sid);
        editor.putString("Paertid",partid);
        editor.putString("Userid",userid);
        editor.putString("Mprice",mPrice);
        editor.putString("Mname",mName);
        editor.commit();
    }

    public void saveRecordDetails(String movietitle, String seasonname, String episodename){
        SharedPreferences sp = context.getSharedPreferences("RecordDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Mname",movietitle);
        editor.putString("Sname",seasonname);
        editor.putString("Ename",episodename);
        editor.commit();
    }

    public void saveLoginDetails( String userid, String password){
        SharedPreferences sp = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Contact", userid);
        editor.putString("Password", password);

        editor.commit();
    }

    public boolean isUserLoggedOut(){
        SharedPreferences sp = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isUserIdEmpty = sp.getString("Contact", "").isEmpty();
        boolean isPasswordEmpty = sp.getString("Password", "").isEmpty();

        return  isUserIdEmpty || isPasswordEmpty ;
    }

}
