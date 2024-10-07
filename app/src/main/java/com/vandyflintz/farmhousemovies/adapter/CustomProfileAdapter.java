package com.vandyflintz.farmhousemovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.vandyflintz.farmhousemovies.CircularImageView;
import com.vandyflintz.farmhousemovies.MainProfile;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.EMessage;

import java.util.List;

public class CustomProfileAdapter extends BaseAdapter {

    Fragment frg;
    private Activity activity;
    private LayoutInflater inflater;
    private List<EMessage> messageItems;
    private Context context;
    private List<EMessage> movieItem;
    EventListenerone elistener;
    MainProfile fragment;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomProfileAdapter(Activity activity, List<EMessage> movieItem) {
        this.activity = activity;
        this.movieItem = movieItem;
    }

    public CustomProfileAdapter(Activity activity, List<EMessage> movieItem,
                                MainProfile fragment) {
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
        View row = convertView;


        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }


        if (convertView == null)
            convertView = inflater.inflate(R.layout.picturesrow, parent, false);

            TextView title = (TextView) convertView.findViewById(R.id.mtitle);
            ImageView imageViewone = (ImageView) convertView.findViewById(R.id.pictureview);
            TextView time = (TextView) convertView.findViewById(R.id.timetext);
            CircularImageView thumbNail = convertView
                    .findViewById(R.id.mthumbnail);

            EMessage m = movieItem.get(position);

            Spanned text =
                    Html.fromHtml("<strong style='margin-bottom:9dp;'>" + m.getTitle() + "</strong>");
            thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

            // imageView.setImageUrl(m.getImageurl(), imageLoader);
            //imageView.setTag();
            Picasso.with(activity).load(m.getImageurl()).into(imageViewone);
            String imgname =
                    m.getImageurl().substring(m.getImageurl().lastIndexOf("/") + 1);
            imageViewone.setTag(m.getContact() + ";" + imgname);
            imageViewone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String imtag = imageViewone.getTag().toString();
                        fragment.showprofiledialog(imtag);

                }
            });



            title.setText(text);

            Spanned texttime =
                    Html.fromHtml("<strong style='margin-bottom:9dp;'>" + m.getTime() + "</strong>");



            if (m.getTime() == null) {
                time.setVisibility(View.GONE);
            } else {
                time.setText(texttime);
            }


        return convertView;
    }

    public interface EventListenerone {
        void onLike(String data);
        void onDisplayProfilepicture(String con);
    }
}