package com.tappay.service.webservice.FlutterWave;

import com.tappay.service.webservice.FlutterWave.Model.transaction.RaveTransaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IRave {
    String AUTHORIZATION=System.getenv("rave_secret");
    @Headers("Content-Type: application/json")
    @GET("/v3/transactions/{id}/verify")
    Call<RaveTransaction> verifyTransaction(@Header("Authorization") String authorization, @Path("id") String id);
}
