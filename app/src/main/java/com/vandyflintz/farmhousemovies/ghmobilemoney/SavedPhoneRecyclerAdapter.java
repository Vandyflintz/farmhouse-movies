package com.vandyflintz.farmhousemovies.ghmobilemoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.data.Callbacks;
import com.vandyflintz.farmhousemovies.data.SavedPhone;

public class SavedPhoneRecyclerAdapter extends RecyclerView.Adapter<SavedPhoneRecyclerAdapter.PhoneViewHolder>{

    private List<SavedPhone> savedPhones;
    private Callbacks.SavedPhoneSelectedListener savedPhoneSelectedListener;

    public void set(List<SavedPhone> cards) {
        this.savedPhones = cards;
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.mobile_money_number_item, parent, false);
        return new PhoneViewHolder(v);
    }


    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position) {
        holder.bind(savedPhones.get(position));
    }

    @Override
    public int getItemCount() {
        return savedPhones.size();
    }

    public void setSavedPhoneSelectedListener(Callbacks.SavedPhoneSelectedListener savedPhoneSelectedListener) {
        this.savedPhoneSelectedListener = savedPhoneSelectedListener;
    }


    public class PhoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemTv;
        private ImageView r_switchIv;
        SavedPhone savedPhone;


        PhoneViewHolder(View v) {
            super(v);
            itemTv = (TextView) v.findViewById(R.id.phoneNumberTv);
            r_switchIv = (ImageView) v.findViewById(R.id.r_switchIv);
            v.setOnClickListener(this);
        }

        public void bind(SavedPhone savedPhone) {
            this.savedPhone = savedPhone;
            itemTv.setText(this.savedPhone.getPhoneNumber());
            r_switchIv.setImageResource(this.savedPhone.getNetworkImage());
        }

        @Override
        public void onClick(View v) {
            savedPhoneSelectedListener.onPhoneSelected(savedPhone);
        }
    }
}
