package com.vandyflintz.farmhousemovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.Movie;

import java.util.List;

public class CustomSearchAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    private Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomSearchAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
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
            convertView = inflater.inflate(R.layout.searchresult_rows, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.sthumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.stitle);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        TextView movieid = (TextView) convertView.findViewById(R.id.mmid);
        TextView moviefile= (TextView) convertView.findViewById(R.id.mfile);
        TextView moviedesc = (TextView) convertView.findViewById(R.id.mdesc);
        TextView moviedate= (TextView) convertView.findViewById(R.id.mdate);
        TextView moviepartid= (TextView) convertView.findViewById(R.id.mpid);
        TextView moviefavstatus = (TextView) convertView.findViewById(R.id.mfid);
        TextView movietype = (TextView) convertView.findViewById(R.id.mtype);
        TextView seasonid = convertView.findViewById(R.id.msid);
        TextView movienid = (TextView) convertView.findViewById(R.id.mid);
        TextView shortname = convertView.findViewById(R.id.shname);
        TextView hidtitle = convertView.findViewById(R.id.hiddentitle);
        TextView imageButton = convertView.findViewById(R.id.paidbtn);
        TextView finaltitle = convertView.findViewById(R.id.finalhiddentitle);
        TextView subscr = convertView.findViewById(R.id.msub);
        TextView category = convertView.findViewById(R.id.mcat);
        TextView price = convertView.findViewById(R.id.mprice);
        TextView expirydate = convertView.findViewById(R.id.mexp);


        // getting movie data for the row
        Movie m = movieItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        subscr.setText(m.getSubscription());
        category.setText(m.getCategory());
        price.setText(m.getPrice());
        expirydate.setText(m.getExpirydate());
        if(m.getCategory() != null){
            if(m.getCategory().equalsIgnoreCase("free") || m.getPaidstatus().equalsIgnoreCase("paid") || m.getPrice().equals("0")){
                imageButton.setVisibility(View.GONE);
            }else{
                imageButton.setVisibility(View.VISIBLE);
            }
        }else{
            imageButton.setVisibility(View.GONE);
        }

        // title
        movietype.setText(m.getMovieType());
        title.setText(m.getTitle());
        duration.setText(m.getDuration());
        movieid.setText(m.getID());
        moviefile.setText(m.getFilename());
        moviedesc.setText(m.getDesc());
        moviedate.setText(m.getDate());
        moviepartid.setText(m.getpartID());
        moviefavstatus.setText(m.getFav());
        hidtitle.setText(m.getTitle()+" - "+m.getPartname());
        shortname.setText(m.getTitle()+" - "+m.getSeriesShortName());
        finaltitle.setText(m.getTitle());
        seasonid.setText(m.getSeriesID());
        movienid.setText(m.getMovieID());
        return convertView;
    }
}
