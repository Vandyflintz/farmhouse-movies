package com.vandyflintz.farmhousemovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.vandyflintz.farmhousemovies.CircularImageView;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.Ratings;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.Movie;

import java.util.List;

public class OtherRatingsAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> ratingItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Ratings fragment;

    public OtherRatingsAdapter(Activity activity, List<Movie> ratingItem) {
        this.activity = activity;
        this.ratingItem = ratingItem;
    }

    public OtherRatingsAdapter(Activity activity, List<Movie> ratingItem, Ratings fragment) {
        this.activity = activity;
        this.ratingItem = ratingItem;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return ratingItem.size();
    }

    @Override
    public Object getItem(int location) {
        return ratingItem.get(location);
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
        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        if (convertView == null)
            convertView = inflater.inflate(R.layout.allotherratings, parent, false);

        TextView username = (TextView) convertView.findViewById(R.id.uname);
        RatingBar allRatingBar = convertView.findViewById(R.id.ratingBar);
        TextView time = (TextView) convertView.findViewById(R.id.timetext);
        CircularImageView thumbNail = convertView
                .findViewById(R.id.mthumbnail);



        Movie m = ratingItem.get(position);

        Float pRating = Float.parseFloat(m.getUserrating());
        LayerDrawable starscolor = (LayerDrawable)allRatingBar.getProgressDrawable();

        allRatingBar.setRating(Float.parseFloat(m.getUserrating()));
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        thumbNail.setTag(m.getContact());
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vTag = thumbNail.getTag().toString();
                fragment.showprofiledialog(vTag);
            }
        });

        Spanned text =
                Html.fromHtml("<strong>" + m.getTitle() + "</strong>");


        username.setText(text);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vTag = username.getText().toString();
                fragment.showprofilenamedialog(vTag);
            }
        });

        Spanned texttime =
                Html.fromHtml("<strong>" + m.getTime() + "</strong>");
        time.setText(texttime);


        return convertView;
    }
}
