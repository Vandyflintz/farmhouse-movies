package com.vandyflintz.farmhousemovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.Movie;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomSubscriptionsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItem;
    Fragment fragment;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomSubscriptionsAdapter(Activity activity, List<Movie> movieItem) {
        this.activity = activity;
        this.movieItem = movieItem;
    }

    public CustomSubscriptionsAdapter(Activity activity, List<Movie> movieItem, Fragment fragment) {
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
            convertView = inflater.inflate(R.layout.sub_parts, parent,false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.mthumbnail);


        TextView title = (TextView) convertView.findViewById(R.id.maintitle);
        TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
        TextView fromdate = (TextView) convertView.findViewById(R.id.datefromtxt);
        TextView todate = (TextView) convertView.findViewById(R.id.datetotxt);
        TextView remaining = (TextView) convertView.findViewById(R.id.timeremainingtxt);

        Movie m = movieItem.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        todate.setText(m.getFormattedExpirydate());
        fromdate.setText(m.getFormattedStartdate());
        title.setText(m.getTitle());
        subtitle.setText(m.getSubTitle());


        String enddate = m.getExpirydate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dbdate = null;
        Date fdate = null;
        try {
            dbdate = simpleDateFormat.format(simpleDateFormat.parse(enddate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date pdate = Calendar.getInstance().getTime();
        try {
            fdate = simpleDateFormat.parse(dbdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = fdate.getTime() - pdate.getTime();
        long mins = TimeUnit.MILLISECONDS.toMinutes(millis);

        if(mins < 1){
            remaining.setText("Elapsed");
        }else{
        Period period = new Period(fdate.getTime(), pdate.getTime(), PeriodType.dayTime());
            PeriodFormatter periodFormatter =
                    new PeriodFormatterBuilder().appendDays().appendSuffix("day","days").appendHours().appendSuffix("hr","hrs").appendMinutes().appendSuffix("min","mins").toFormatter();
        remaining.setText((periodFormatter.print(period)).replaceAll("-"," "));
        }



        return convertView;
    }


}
