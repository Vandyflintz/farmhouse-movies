package com.vandyflintz.farmhousemovies.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class NewCommentsAdapter extends RecyclerView.Adapter<NewCommentsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<EMessage> messageItems, namesmessageItems;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String userid;
    EventListener eventListener;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @NonNull
    @Override
    public NewCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.messages_rows, parent, false);

        NewCommentsAdapter.ViewHolder vh = new NewCommentsAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewCommentsAdapter.ViewHolder holder, int position) {

        sharedPreferences = context.getSharedPreferences("LoginDetails",
                Context.MODE_PRIVATE);
        userid = (sharedPreferences.getString("Contact", ""));


        // title and message
        Spanned text =
                Html.fromHtml("<strong style='margin-bottom:9dp;'>" + messageItems.get(position).getTitle() + "</strong>");

        if (userid.equals(messageItems.get(position).getContact())) {
            holder.title.setVisibility(View.GONE);
            holder.thumbnail.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


            holder.relativeLayoutone.setLayoutParams(params);
            holder.relativeLayoutone.setBackgroundResource(R.drawable.commentbubbletwo);
        } else {

            holder.title.setText(text);

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vTag = holder.title.getText().toString();
                    eventListener.onEvent(vTag);
                }
            });

            // thumbnail image
            holder.thumbnail.setImageUrl(messageItems.get(position).getThumbnailUrl(), imageLoader);
            holder.thumbnail.setTag(messageItems.get(position).getContact());
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vTag = holder.thumbnail.getTag().toString();
                    eventListener.onProfilepicture(vTag);
                }
            });


        }
        String originalcomment = messageItems.get(position).getMessage();
        String availablenames = messageItems.get(0).getRegisterednames();

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
        if (msgString.contains("<text>")){
            String namesgot = "", splitter = "";
            int i, j, k;


            String[] names = StringUtils.substringsBetween(msgString, "<text>", "</text>");

            List<String> stringList = new ArrayList<String>(Arrays.asList(names));

            for (String value : stringList) {
                modifiedvalue = new TextView(context.getApplicationContext());
                if (trim(value).startsWith("@")) {

                    modifiedvalue.setTag(value);
                    modifiedvalue.setTextColor(context.getApplicationContext().getResources().getColor(R.color.corange));
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
                    modifiedvalue.setTextColor(context.getApplicationContext().getResources().getColor(R.color.cwhite));
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

                holder.flexboxLayout.addView(modifiedvalue);

            }

        } else{

            TextView unreferencedtv = new TextView(context.getApplicationContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                unreferencedtv.setText(Html.fromHtml(originalcomment + "&nbsp;",
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                unreferencedtv.setText(Html.fromHtml(originalcomment + "&nbsp;"));
            }

            unreferencedtv.setTextSize(14);
            unreferencedtv.setTextColor(context.getApplicationContext().getResources().getColor(R.color.cwhite));
            holder.flexboxLayout.addView(unreferencedtv);
        }


        holder.time.setText(messageItems.get(position).getTime());
        holder.date.setText(messageItems.get(position).getDate());
        holder.time.setVisibility(View.GONE);
        holder.date.setVisibility(View.GONE);

        holder.toggletime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.time.getVisibility() == View.VISIBLE && holder.date.getVisibility() == View.VISIBLE) {
                    holder.time.setVisibility(View.GONE);
                    holder.date.setVisibility(View.GONE);
                } else {
                    holder.time.setVisibility(View.VISIBLE);
                    holder.date.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, message, time, date;
        public Button toggletime;
        public FlexboxLayout flexboxLayout;
        public RelativeLayout relativeLayout, relativeLayoutone;
        public CircularImageView thumbnail;

        public ViewHolder(@NonNull View v) {
            super(v);
             title = (TextView) v.findViewById(R.id.ntitle);
             message = (TextView) v.findViewById(R.id.message);
             time = (TextView) v.findViewById(R.id.time);
             date = (TextView) v.findViewById(R.id.datecom);
             toggletime = v.findViewById(R.id.timebtn);
             flexboxLayout = v.findViewById(R.id.commentsec);
             relativeLayout = v.findViewById(R.id.maincon);
             relativeLayoutone = v.findViewById(R.id.mainconone);
             thumbnail = v.findViewById(R.id.mthumbnail);
        }

        @Override
        public void onClick(View v) {

        }
    }
    public void add(int position, EMessage item){
        messageItems.add(position, item);
        namesmessageItems.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(EMessage item){
        int position = messageItems.indexOf(item);
        messageItems.remove(position);
        notifyItemRemoved(position);
    }

    public interface EventListener {
        void onEvent(String data);
        void onProfilepicture(String con);
    }
    public NewCommentsAdapter(Context context, List<EMessage> messageItems,
             NewCommentsAdapter.EventListener eventListener){
        this.context = context;
        this.messageItems = messageItems;
        this.eventListener = eventListener;
    }
}
