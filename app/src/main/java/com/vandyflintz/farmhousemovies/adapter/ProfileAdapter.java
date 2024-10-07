package com.vandyflintz.farmhousemovies.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.vandyflintz.farmhousemovies.CircularImageView;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.app.AppController;
import com.vandyflintz.farmhousemovies.model.EMessage;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private Context context;
    private List<EMessage> messageList;
    GestureDetector gd;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    EventListener eventListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.picturesrow, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    public interface EventListener {
        void onLike(String data);
        void onProfilepicture(String con);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Spanned text =
                Html.fromHtml("<strong style='margin-bottom:9dp;'>" + messageList.get(position).getTitle() + "</strong>");
        holder.thumbNail.setImageUrl(messageList.get(position).getThumbnailUrl(), imageLoader);
        //holder.imageView.setImageUrl(messageList.get(position).getImageurl(), imageLoader);
        Picasso.with(context).load(messageList.get(position).getImageurl()).into(holder.imageView);
        String imgname =
                messageList.get(position).getImageurl().substring(messageList.get(position).getImageurl().lastIndexOf("/")+1);
        holder.imageView.setTag(messageList.get(position).getContact()+";"+imgname);
        Spanned texttime =
                Html.fromHtml("<strong style='margin-bottom:9dp;'>" + messageList.get(position).getTime() + "</strong>");
        holder.title.setText(text);


        if( messageList.get(position).getTime() == null){
          holder.timetxt.setVisibility(View.GONE);
        }else{
            holder.timetxt.setText(texttime);
        }

      holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imtag = holder.imageView.getTag().toString();
                eventListener.onProfilepicture(imtag);
            }
        });



        /*holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });
            */

    gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

    });

     gd.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            String imtag = holder.imageView.getTag().toString();
            eventListener.onLike(imtag);
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            // Toast.makeText(getActivity(),"Double tap" ,Toast.LENGTH_LONG).show();

            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }
    });
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public ImageView imageView;
        public TextView timetxt;
        public CircularImageView thumbNail;
        public View v;


        public ViewHolder(@NonNull View v) {
            super(v);

            title =  v.findViewById(R.id.mtitle);
            this.imageView = v.findViewById(R.id.pictureview);
            timetxt = (TextView) v.findViewById(R.id.timetext);
            thumbNail = v.findViewById(R.id.mthumbnail);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            this.v = v;
            int id = v.getId();
            if(id == R.id.pictureview){
                String mytag = v.getTag().toString();
                eventListener.onProfilepicture(mytag);
            }

        }

    }


    public void add(int position, EMessage item){
        messageList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(EMessage item){
        int position = messageList.indexOf(item);
        messageList.remove(position);
        notifyItemRemoved(position);
    }


   public ProfileAdapter(Context context, List<EMessage> messageList){
       this.context = context;
       this.messageList = messageList;
   }

    public ProfileAdapter(Context context, List<EMessage> messageList, EventListener eventListener){
        this.context = context;
        this.messageList = messageList;
        this.eventListener = eventListener;
    }

}
