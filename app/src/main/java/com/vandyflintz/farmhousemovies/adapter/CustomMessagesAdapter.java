package com.vandyflintz.farmhousemovies.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.flexbox.FlexboxLayout;
import com.vandyflintz.farmhousemovies.CircularImageView;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.EMessage;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class CustomMessagesAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<EMessage> messageItems;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String userid;
    EventListener eventListener;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TextView modifiedvalue, unreferencedtv;
    String msgString, originalcomment, availablenames, availnew, namesgot, splitter, vTag ;
    String[] allnameslist, names;
    int comsplit, i, j, k;;
    Spanned text;
    List<String> stringList;
    TextView title, message, time, date;
    RelativeLayout relativeLayout, relativeLayoutone;
    Button toggletime;
    FlexboxLayout flexboxLayout;
    CircularImageView thumbNail;

    public CustomMessagesAdapter(Activity activity, List<EMessage> messageItems ) {
        this.activity = activity;
        this.messageItems = messageItems;
    }

    public CustomMessagesAdapter(Activity activity, List<EMessage> messageItems ,
                                 EventListener eventlistener) {
        this.activity = activity;
        this.messageItems = messageItems;
        this.eventListener = eventlistener;
    }

    public CustomMessagesAdapter() {

    }



    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int location) {
        return messageItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        RecyclerView.ViewHolder holder;

        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }


        if (convertView == null) {
            row = inflater.inflate(R.layout.messages_rows, parent, false);
        }else{
            row = convertView;
        }

            TextView title = (TextView) row.findViewById(R.id.ntitle);
            TextView message = (TextView) row.findViewById(R.id.message);
            TextView time = (TextView) row.findViewById(R.id.time);
            TextView date = (TextView) row.findViewById(R.id.datecom);
            Button toggletime = row.findViewById(R.id.timebtn);
            FlexboxLayout flexboxLayout = row.findViewById(R.id.commentsec);
            RelativeLayout relativeLayout = row.findViewById(R.id.maincon);
            RelativeLayout relativeLayoutone = row.findViewById(R.id.mainconone);
            sharedPreferences = activity.getSharedPreferences("LoginDetails",
                    Context.MODE_PRIVATE);
            userid = (sharedPreferences.getString("Contact", ""));

            CircularImageView thumbNail = row.findViewById(R.id.mthumbnail);
            // getting movie data for the row
            EMessage msg = messageItems.get(position);
            EMessage namesmsg = messageItems.get(0);


            // title and message
            Spanned text =
                    Html.fromHtml("<strong style='margin-bottom:9dp;'>" + msg.getTitle() + "</strong>");

            if (userid.equals(msg.getContact())) {
                title.setVisibility(View.GONE);
                thumbNail.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                relativeLayoutone.setLayoutParams(params);
                relativeLayoutone.setBackgroundResource(R.drawable.commentbubbletwo);
            } else {

                title.setText(text);

                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String vTag = title.getText().toString();
                        eventListener.onEvent(vTag);
                    }
                });

                // thumbnail image
                thumbNail.setImageUrl(msg.getThumbnailUrl(), imageLoader);
                thumbNail.setTag(msg.getContact());
                thumbNail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String vTag = thumbNail.getTag().toString();
                        eventListener.onProfilepicture(vTag);
                    }
                });


            }
            String originalcomment = msg.getMessage();
            String availablenames = namesmsg.getRegisterednames();

            String msgString;

            int comsplit = availablenames.lastIndexOf(",");
            String availnew = new StringBuilder(availablenames).replace(comsplit, comsplit + 1, "").toString();
            String[] allnameslist = availnew.split(",");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                msgString = Html.fromHtml(originalcomment, Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                msgString = Html.fromHtml(originalcomment).toString();
            }


            TextView modifiedvalue;
            if (msgString.contains("<text>")) {
                String namesgot = "", splitter = "";
                int i, j, k;


                String[] names = StringUtils.substringsBetween(msgString, "<text>", "</text>");

                List<String> stringList = new ArrayList<String>(Arrays.asList(names));

                for (String value : stringList) {
                    modifiedvalue = new TextView(activity.getApplicationContext());
                    if (trim(value).startsWith("@")) {

                        modifiedvalue.setTag(value);
                        modifiedvalue.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.corange));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            modifiedvalue.setText(Html.fromHtml("<strong>" + value + "&nbsp;" +
                                            "</strong>",
                                    Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            modifiedvalue.setText(Html.fromHtml("<strong>" + value + "&nbsp;" + "</strong>"));
                        }

                        String vTag = (String) modifiedvalue.getTag();

                        modifiedvalue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eventListener.onEvent(vTag);
                            }
                        });

                    } else {
                        modifiedvalue.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.cwhite));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            modifiedvalue.setText(Html.fromHtml(value + "&nbsp;", Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            modifiedvalue.setText(Html.fromHtml(value + "&nbsp;"));
                        }

                    }


                    modifiedvalue.setTextSize(14);
                    modifiedvalue.setSingleLine(false);
                    modifiedvalue.setElegantTextHeight(true);
                    modifiedvalue.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

                    flexboxLayout.addView(modifiedvalue);

                }

            } else {

                TextView unreferencedtv = new TextView(activity.getApplicationContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    unreferencedtv.setText(Html.fromHtml(originalcomment + "&nbsp;",
                            Html.FROM_HTML_MODE_LEGACY));
                } else {
                    unreferencedtv.setText(Html.fromHtml(originalcomment + "&nbsp;"));
                }

                unreferencedtv.setTextSize(14);
                unreferencedtv.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.cwhite));
                flexboxLayout.addView(unreferencedtv);
            }


            time.setText(msg.getTime());
            date.setText(msg.getDate());
            time.setVisibility(View.GONE);
            date.setVisibility(View.GONE);

            toggletime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (time.getVisibility() == View.VISIBLE && date.getVisibility() == View.VISIBLE) {
                        time.setVisibility(View.GONE);
                        date.setVisibility(View.GONE);
                    } else {
                        time.setVisibility(View.VISIBLE);
                        date.setVisibility(View.VISIBLE);
                    }
                }
            });








        return row;
    }



    public interface EventListener {
        void onEvent(String data);
        void onProfilepicture(String con);
    }

}
