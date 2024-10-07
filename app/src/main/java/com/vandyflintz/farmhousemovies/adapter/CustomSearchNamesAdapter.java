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

public class CustomSearchNamesAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    private Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomSearchNamesAdapter(Activity activity, List<Movie> movieItems) {
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
            convertView = inflater.inflate(R.layout.searchnames_rows, null);

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.sthumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.ntitle);

        // getting movie data for the row
        Movie m = movieItems.get(position);

        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        // title
        title.setText(m.getTitle());

        return convertView;
    }
}
