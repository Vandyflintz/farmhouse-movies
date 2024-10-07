package com.vandyflintz.farmhousemovies.ghmobilemoney;

import java.util.List;

import com.vandyflintz.farmhousemovies.Payload;
import com.vandyflintz.farmhousemovies.data.SavedPhone;

public interface GhMobileMoneyContract {

    interface View {
        void showProgressIndicator(boolean active);
        void showPollingIndicator(boolean active);
        void onPaymentError(String message);
        void showToast(String message);
        void onPaymentSuccessful(String status, String responseAsString);
        void onPaymentFailed(String message, String responseAsJSONString);
        void showSavedPhoneList(List<SavedPhone> savedPhoneList);
    }

    interface UserActionsListener {
        void chargeGhMobileMoney(Payload payload, String apiKey);
        void onSavedPhonesClicked(String phoneNumber);
        List<SavedPhone> checkForSavedGHMobileMoney(String email);
        void saveThisPhone(String phoneNumber, String apiKey, String phoneNumeber, String network);
    }
}
