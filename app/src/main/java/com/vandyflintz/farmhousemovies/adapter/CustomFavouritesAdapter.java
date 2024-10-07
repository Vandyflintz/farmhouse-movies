package com.vandyflintz.farmhousemovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vandyflintz.farmhousemovies.FavouritesNew;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.Movie;

import java.util.List;

public class CustomFavouritesAdapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItem;
    private Context context;
    FavouritesNew fragment;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomFavouritesAdapter(Activity activity, List<Movie> movieItem, FavouritesNew fragment) {
        this.activity = activity;
        this.movieItem = movieItem;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return movieItem.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.favourites_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.mthumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.mtitle);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        TextView movieid = (TextView) convertView.findViewById(R.id.mmid);
        TextView moviefile= (TextView) convertView.findViewById(R.id.mfile);
        TextView moviedesc = (TextView) convertView.findViewById(R.id.mdesc);
        TextView moviedate= (TextView) convertView.findViewById(R.id.mdate);
        TextView moviepartid= (TextView) convertView.findViewById(R.id.mpid);
        TextView moviefavstatus = (TextView) convertView.findViewById(R.id.mfid);
        TextView movietype = (TextView) convertView.findViewById(R.id.mtype);
        TextView movieprice = (TextView) convertView.findViewById(R.id.mprice);
        TextView newmovieid = (TextView) convertView.findViewById(R.id.mid);
        TextView expirydate = (TextView) convertView.findViewById(R.id.mexpirydate);
        TextView seasonid = (TextView) convertView.findViewById(R.id.msid);
        TextView money = (TextView) convertView.findViewById(R.id.paidbtn);
        Button play =   convertView.findViewById(R.id.playvideo);
        Button del = convertView.findViewById(R.id.delvideo);
        // getting movie data for the row
        Movie m = movieItem.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

       // View.OnClickListener playBtnListener = null;
        //play.setOnClickListener(playBtnListener);
        newmovieid.setText(m.getMovieID());
        expirydate.setText(m.getExpirydate());
        movieprice.setText(m.getPrice());
        movietype.setText(m.getMovieType());
        title.setText(m.getTitle());
        duration.setText(m.getDuration());
        movieid.setText(m.getID());
        moviefile.setText(m.getFilename());
        moviedesc.setText(m.getDesc());
        moviedate.setText(m.getDate());
        moviepartid.setText(m.getpartID());
        moviefavstatus.setText(m.getFav());
        seasonid.setText(m.getSeriesID());

        if(m.getCategory().equalsIgnoreCase("free") || m.getPaidstatus().equalsIgnoreCase("paid") || m.getPrice().equals("0")){
            money.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        }else{
            money.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }

        String mpath = movieid.getText().toString();
        String mtitle = title.getText().toString();
        String mtype = movietype.getText().toString();
        String mid = newmovieid.getText().toString();
        String sid = seasonid.getText().toString();
        String partid = moviepartid.getText().toString();
        String price = movieprice.getText().toString();
        String expdate = expirydate.getText().toString();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.playvideo(mpath, mtitle, mtype, mid, sid, partid, price, expdate, position);
            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragment.getpaymentdetails(mtype, mid, sid, partid, price, mtitle,position);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.removeVideo(partid,  sid, mid ,  mtype);
            }
        });

        return convertView;
    }




}
