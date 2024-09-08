package com.tappay.service.webservice.FlutterWave.service;

import com.tappay.service.webservice.FlutterWave.IRave;
import com.tappay.service.webservice.FlutterWave.Model.transaction.RaveTransaction;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.function.Consumer;

import static com.tappay.service.webservice.FlutterWave.IRave.AUTHORIZATION;

public class RaveService {
    Logger logger= LoggerFactory.getLogger(RaveService.class);

    public Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl("https://api.flutterwave.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }
    public void verifyTransaction(String id, Consumer<String> consumer){
        try{
            // Verify the transaction
            logger.info("Verifying transaction: "+"id:"+id);
            Retrofit retrofit=getClient();
            IRave iRave=retrofit.create(IRave.class);
            Call<RaveTransaction> call=iRave.verifyTransaction(AUTHORIZATION,id);
            call.enqueue(new Callback<RaveTransaction>() {
                @Override
                public void onResponse(@NotNull Call<RaveTransaction> call, @NotNull Response<RaveTransaction> response) {
                    logger.info(response.message() );
                    logger.info(String.valueOf(response.code()));
                    if(response.isSuccessful()){
                        RaveTransaction body=response.body();
                        logger.info("Transaction verified: id:"+id);
                        consumer.accept("Balance updated successfully");
                    }else{
                        logger.info("Transaction verification failed: "+response.message()+"id:"+id);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<RaveTransaction> call, @NotNull Throwable throwable) {
                    logger.error(throwable.getMessage());
                    logger.error("Error in verifying transaction: ");
                }
            });
        }catch (Exception e){
            logger.error("Error in verifying transaction: "+e.getMessage()+"id:"+id);

        }
    }
}
