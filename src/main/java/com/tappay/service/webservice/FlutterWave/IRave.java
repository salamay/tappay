package com.tappay.service.webservice.FlutterWave;

import com.tappay.service.webservice.FlutterWave.Model.transaction.RaveTransaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IRave {
    String AUTHORIZATION="Bearer FLWSECK_TEST-875126bc78dbb35e90f53437169376da-X";
    @Headers({
            "Content-Type: application/json",
            "Authorization: "+AUTHORIZATION
    })
    @GET("/v3/transactions/{id}/verify")
    Call<RaveTransaction> verifyTransaction(@Path("id") String id);
}
