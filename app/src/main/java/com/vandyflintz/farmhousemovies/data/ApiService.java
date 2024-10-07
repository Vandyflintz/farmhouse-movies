package com.vandyflintz.farmhousemovies.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/v1.1/transaction/process")
    Call<String> chargeCard(@Body com.vandyflintz.farmhousemovies.card.ChargeRequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/v1.1/transaction/process")
    Call<String> chargeMomo(@Body com.vandyflintz.farmhousemovies.ghmobilemoney.ChargeRequestBody body);
}