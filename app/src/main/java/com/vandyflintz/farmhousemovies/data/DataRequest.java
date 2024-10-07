package com.vandyflintz.farmhousemovies.data;

import java.util.List;

import com.vandyflintz.farmhousemovies.Payload;
import com.vandyflintz.farmhousemovies.ghmobilemoney.ChargeRequestBody;

public interface DataRequest {

    interface NetworkRequest {
        void chargeCard(Payload payload, com.vandyflintz.farmhousemovies.card.ChargeRequestBody chargeRequestBody, Callbacks.OnChargeRequestComplete callback);
        void chargeMomo(Payload payload, ChargeRequestBody chargeRequestBody, Callbacks.OnChargeRequestComplete callback);
    }

    interface SharedPrefsRequest {
        void saveCardDetsToSave(CardDetsToSave cardDetsToSave);
        CardDetsToSave retrieveCardDetsToSave();
        void saveACard(SavedCard card, String SECKEY, String email);
        List<SavedCard> getSavedCards(String email);
        void saveAPhone(SavedPhone phone, String SECKEY, String email);
        List<SavedPhone> getSavedGHMobileMoney(String phoneNumber);
    }
}
