package com.vandyflintz.farmhousemovies.data;


import com.vandyflintz.farmhousemovies.responses.ChargeResponse;

public class Callbacks {

    public interface OnChargeRequestComplete {
        void onSuccess(ChargeResponse response, String responseAsJSONString);
        void onError(String message, String responseAsJSONString);
    }

    public interface SavedCardSelectedListener {
        void onCardSelected(SavedCard savedCard);
    }

    public interface SavedPhoneSelectedListener {
        void onPhoneSelected(SavedPhone savedPhone);
    }

}
