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

public class CustomTrailerAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItem;
    private Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomTrailerAdapter(Activity activity, List<Movie> movieItem) {
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
            convertView = inflater.inflate(R.layout.news_row_one, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.mthumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView more = (TextView) convertView.findViewById(R.id.more);
        TextView movieid = (TextView) convertView.findViewById(R.id.mpid);
        TextView moviefile= (TextView) convertView.findViewById(R.id.mpath);
        TextView moviedesc = (TextView) convertView.findViewById(R.id.mdesc);
        TextView moviecast = (TextView) convertView.findViewById(R.id.mcast);
        TextView moviepic = (TextView) convertView.findViewById(R.id.mpic);
        TextView moviedate = (TextView) convertView.findViewById(R.id.mdate);
        ;
        // getting movie data for the row
        Movie m = movieItem.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // View.OnClickListener playBtnListener = null;
        //play.setOnClickListener(playBtnListener);



        title.setText(m.getTitle());
        movieid.setText(m.getID());
        moviefile.setText(m.getFilename());
        moviedesc.setText(m.getDesc());
        moviecast.setText(m.getCast());
        moviepic.setText(m.getPic());
        moviedate.setText(m.getDate());

        return convertView;
    }

}
