package com.vandyflintz.farmhousemovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.Ratings;
import com.vandyflintz.farmhousemovies.model.Movie;

import java.util.List;

public class CustomRatingsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> ratingItem;
    TextView overalltv, onestartv, twostarstv, threestarstv, fourstarstv, fivestarstv, totaltv,
            usertv, datetv;
    RatingBar userratingBar, allRatingBar;
    double totalusers, fivestarrating, fourstarrating, threestarrating, twostarrating,
            onestarrating;
    int fivestars, fourstars, threestars, twostars, onestar;
    RatingReviews ratingReviews;
    RelativeLayout relativeLayout;
    TextView results, textView, people, underline, emptytext, movietype;
    Ratings fragment;
    ImageButton morebtn;


    public CustomRatingsAdapter(Activity activity, List<Movie> ratingItem) {
        this.activity = activity;
        this.ratingItem = ratingItem;
    }

    public CustomRatingsAdapter(Activity activity, List<Movie> ratingItem, Ratings fragment) {
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
        if (convertView == null)
            convertView = inflater.inflate(R.layout.ratingpanel, parent, false);



        overalltv = convertView.findViewById(R.id.overall);
        totaltv = convertView.findViewById(R.id.totalgot);
        onestartv = convertView.findViewById(R.id.onestar);
        twostarstv = convertView.findViewById(R.id.twostar);
        threestarstv = convertView.findViewById(R.id.threestar);
        fourstarstv = convertView.findViewById(R.id.fourstar);
        fivestarstv = convertView.findViewById(R.id.fivestar);
        usertv = convertView.findViewById(R.id.userstar);
        datetv = convertView.findViewById(R.id.datestar);
        results = convertView.findViewById(R.id.txtuserrating);
        ratingReviews = convertView.findViewById(R.id.rating_reviews);
        userratingBar = convertView.findViewById(R.id.rating_bar);
        allRatingBar = convertView.findViewById(R.id.ratingBar);
        textView = convertView.findViewById(R.id.textView);
        people = convertView.findViewById(R.id.textView2);
        morebtn = convertView.findViewById(R.id.morebtn);
        movietype = convertView.findViewById(R.id.mtype);
        // getting movie data for the row
        Movie m = ratingItem.get(position);

        // title

        int rem = Integer.parseInt (m.getRemraters());

        if(rem < 1){
           morebtn.setVisibility(View.INVISIBLE);
        }

        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.displayotherratings();
            }
        });

        overalltv.setText(m.getOverallrating());
        totaltv.setText(m.getTotalraters());
        onestartv.setText(m.getOnestarraters());
        twostarstv.setText(m.getTwostarraters());
        threestarstv.setText(m.getThreestarraters());
        fourstarstv.setText(m.getFourstarraters());
        fivestarstv.setText(m.getFivestarraters());
        usertv.setText(m.getUserrating());
        datetv.setText(m.getDaterated());
        movietype.setText(m.getMovieType());

        String overalls, totals, ones, twos, threes, fours, fives, users, dates;

        overalls = overalltv.getText().toString();
        totals = totaltv.getText().toString();
        ones = onestartv.getText().toString();
        twos =   twostarstv.getText().toString();
        threes = threestarstv.getText().toString();
        fours = fourstarstv.getText().toString();
        fives = fivestarstv.getText().toString();

        allRatingBar.setRating(Float.valueOf(overalls));
        textView.setText(overalltv.getText().toString());
        people.setText(totals);

        totalusers = Double.valueOf( totaltv.getText().toString());
        fivestarrating = Double.valueOf(fives);
        fourstarrating = Double.valueOf(fours);
        threestarrating = Double.valueOf(threes);
        twostarrating = Double.valueOf(twos);
        onestarrating = Double.valueOf(ones);

        fivestars = (int) Math.rint((fivestarrating/totalusers * 100));
        fourstars = (int) Math.rint((fourstarrating/totalusers * 100));
        threestars = (int) Math.rint((threestarrating/totalusers * 100));
        twostars = (int) Math.rint((twostarrating/totalusers * 100));
        onestar = (int) Math.rint((onestarrating/totalusers * 100));





        String userrating = usertv.getText().toString();

        if(userrating.equals("")){
            userratingBar.setVisibility(View.GONE);
            results.setVisibility(View.INVISIBLE);
        }else{
            userratingBar.setVisibility(View.VISIBLE);
            results.setVisibility(View.VISIBLE);
            userratingBar.setRating(Float.valueOf( usertv.getText().toString()));
            userratingBar.setIsIndicator(true);
        }



        results.setText(datetv.getText().toString());

        createratingbar(fivestars,fourstars,threestars,twostars,onestar);
        return convertView;
    }

    private void createratingbar(int fivestars, int fourstars, int threestars, int twostars, int onestar) {
        int raters[] = new int[]{
                fivestars, fourstars, threestars, twostars, onestar
        };

        Pair colors[] = new Pair[]{
                new Pair<>(Color.parseColor("#0c96c7"), Color.parseColor("#00fe77")),
                new Pair<>(Color.parseColor("#7b0ab4"), Color.parseColor("#ff069c")),
                new Pair<>(Color.parseColor("#fe6522"), Color.parseColor("#fdd116")),
                new Pair<>(Color.parseColor("#104bff"), Color.parseColor("#67cef6")),
                new Pair<>(Color.parseColor("#ff5d9b"), Color.parseColor("#ffaa69"))
        };

        StringBuilder builder = new StringBuilder();
        for (int i : raters){
            builder.append(""+i+" ");
        }

      //  Toast.makeText(this.activity, builder, Toast.LENGTH_LONG).show();

        ratingReviews.createRatingBars(100, BarLabels.STYPE3, colors, raters);

    }

}
