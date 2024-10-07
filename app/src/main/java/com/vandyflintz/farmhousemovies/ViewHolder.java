package com.vandyflintz.farmhousemovies;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.vandyflintz.farmhousemovies.data.ItemModel;

import com.vandyflintz.farmhousemovies.data.ItemModel;

public abstract class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindType(ItemModel item);


}