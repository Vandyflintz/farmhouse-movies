package com.vandyflintz.farmhousemovies.ghmobilemoney;

import android.content.Context;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import com.vandyflintz.farmhousemovies.Payload;
import com.vandyflintz.farmhousemovies.R;
import com.vandyflintz.farmhousemovies.Utils;
import com.vandyflintz.farmhousemovies.data.Callbacks;
import com.vandyflintz.farmhousemovies.data.NetworkRequestImpl;
import com.vandyflintz.farmhousemovies.data.SavedPhone;
import com.vandyflintz.farmhousemovies.data.SharedPrefsRequestImpl;
import com.vandyflintz.farmhousemovies.responses.ChargeResponse;

public class GhMobileMoneyPresenter implements GhMobileMoneyContract.UserActionsListener {
    private Context context;
    private GhMobileMoneyContract.View mView;

    public GhMobileMoneyPresenter(Context context, GhMobileMoneyContract.View mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void chargeGhMobileMoney(final Payload payload, final String apiKey) {
        String cardRequestBodyAsString = Utils.convertChargeRequestPayloadToJson(payload).trim().replaceAll("\\n", "");

        Log.d("encrypted", cardRequestBodyAsString);

        ChargeRequestBody body = new ChargeRequestBody();
        body.setClient(Utils.minorUnitAmount(payload.getAmount()), "000200", payload.getTxRef(), payload.getNarration(), payload.getMerchant_id(), payload.getPhonenumber(), payload.getNetwork(), payload.getVoucherCode());

        mView.showProgressIndicator(true);

        new NetworkRequestImpl().chargeMomo(payload, body, new Callbacks.OnChargeRequestComplete() {
            @Override
            public void onSuccess(ChargeResponse response, String responseAsJSONString) {

                mView.showPollingIndicator(false);
                mView.showProgressIndicator(false);

                if (response != null) {
                    Log.d("resp", responseAsJSONString);

                    String status = response.getStatus();
                    String code = response.getCode();
                    String reason = response.getReason();
                    String txRef = response.getTxRef();

                    if (code.equals("000")) {
                        mView.onPaymentSuccessful(code, responseAsJSONString);
                    }
                    else {
                        mView.showProgressIndicator(false);
                        mView.onPaymentFailed(status, responseAsJSONString);
                    }
                }
                else {
                    mView.onPaymentError("No response data was returned");
                }

            }

            @Override
            public void onError(String message, String responseAsJSONString) {
                mView.showProgressIndicator(false);
                mView.onPaymentError(message);
            }
        });
    }

    @Override
    public void onSavedPhonesClicked(String email) {

        SharedPrefsRequestImpl sharedMgr = new SharedPrefsRequestImpl(context);

        List<SavedPhone> phoneList = sharedMgr.getSavedGHMobileMoney(email);

        mView.showSavedPhoneList(phoneList);
    }

    @Override
    public List<SavedPhone> checkForSavedGHMobileMoney(String email) {
        SharedPrefsRequestImpl sharedMgr = new SharedPrefsRequestImpl(context);

        List<SavedPhone> savedGHMobileMoney = sharedMgr.getSavedGHMobileMoney(email);

        if (savedGHMobileMoney.size() == 0) {
            return Collections.emptyList();
        }else {
            return savedGHMobileMoney;
        }

    }

    @Override
    public void saveThisPhone(String email, String secretKey, String phoneNumber, String network) {
        SharedPrefsRequestImpl sharedPrefsRequest = new SharedPrefsRequestImpl(context);

        try {
            SavedPhone phone = new SavedPhone();
            phone.setPhoneNumber(phoneNumber);
            phone.setNetwork(network);
            switch (network){
                case "MTN":
                    phone.setNetworkImage(R.drawable.mtn_momo);
                    break;
                case "TIGO":
                    phone.setNetworkImage(R.drawable.airtel_tigo_momo);
                    break;
                case "AIRTEL":
                    phone.setNetworkImage(R.drawable.airtel_tigo_momo);
                    break;
                case "VODAFONE":
                    phone.setNetworkImage(R.drawable.v_cash);
                default:
                    break;
            }
            sharedPrefsRequest.saveAPhone(phone, secretKey, email);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}


