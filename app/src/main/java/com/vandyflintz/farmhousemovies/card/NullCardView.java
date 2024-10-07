package com.vandyflintz.farmhousemovies.card;

import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.List;

import com.vandyflintz.farmhousemovies.data.SavedCard;

public class NullCardView extends Fragment implements View.OnClickListener, CardContract.View {

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showProgressIndicator(boolean active) {

    }

    @Override
    public void onPaymentError(String message) {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onVBVAuthModelUsed(String authUrlCrude, String responseAsJSONString, String txRef) {

    }

    @Override
    public void onPaymentSuccessful(String status, String responseAsString) {

    }

    @Override
    public void onPaymentFailed(String status, String responseAsString) {

    }

    @Override
    public void showSavedCards(List<SavedCard> cards) {

    }

}
