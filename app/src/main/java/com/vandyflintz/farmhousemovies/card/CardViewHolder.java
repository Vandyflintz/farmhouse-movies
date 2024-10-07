package com.vandyflintz.farmhousemovies.card;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vandyflintz.farmhousemovies.R;

public class CardViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTitle;
    public CardViewHolder(final View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.title);
    }
}