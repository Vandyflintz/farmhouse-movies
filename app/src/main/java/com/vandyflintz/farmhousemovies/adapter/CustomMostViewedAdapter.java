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

public class CustomMostViewedAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItem;
    private Context context;
    String filename;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomMostViewedAdapter(Activity activity, List<Movie> movieItem) {
        this.activity = activity;
        this.movieItem = movieItem;
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
            convertView = inflater.inflate(R.layout.parts, null);

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
        TextView hidtitle = convertView.findViewById(R.id.hiddentitle);
        TextView imageButton = convertView.findViewById(R.id.paidbtn);
        TextView finaltitle = convertView.findViewById(R.id.finalhiddentitle);
        TextView movietype = convertView.findViewById(R.id.mtype);
        TextView seasonid = convertView.findViewById(R.id.msid);
        TextView category = convertView.findViewById(R.id.mcat);
        TextView price = convertView.findViewById(R.id.mprice);
        TextView movienid = (TextView) convertView.findViewById(R.id.mid);
        TextView subscr = (TextView) convertView.findViewById(R.id.msub);
        TextView expirydate = convertView.findViewById(R.id.mexp);
        // getting movie data for the row
        Movie m = movieItem.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        subscr.setText(m.getSubscription());
        seasonid.setText(m.getSeriesID());
        String mainname = m.getTitle();
        String partname = m.getPartname();
        expirydate.setText(m.getExpirydate());
        if(mainname.equals(partname)){
            filename = mainname;
        }else{
            filename = mainname +" - " + partname;
        }

        category.setText(m.getCategory());
        price.setText(m.getPrice());
        movietype.setText(m.getMovieType());
        title.setText(filename);
        movienid.setText(m.getMovieID());
        //duration.setText(m.getDuration()+" ("+m.getView()+")");
        duration.setText(m.getDuration());
        movieid.setText(m.getID());
        moviefile.setText(m.getFilename());
        moviedesc.setText(m.getDesc());
        moviedate.setText(m.getDate());
        moviepartid.setText(m.getpartID());
        moviefavstatus.setText(m.getFav());
        hidtitle.setText(m.getTitle()+" - "+m.getPartname());
        finaltitle.setText(m.getTitle());

        if(m.getCategory().equalsIgnoreCase("free") || m.getPaidstatus().equalsIgnoreCase("paid") || m.getPrice().equals("0")){
            imageButton.setVisibility(View.GONE);
        }else if( m.getPaidstatus().equalsIgnoreCase("unpaid") && m.getCategory().equalsIgnoreCase("paid")){
            imageButton.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

}
