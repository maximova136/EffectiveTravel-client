package com.et.api;


import android.util.Log;

import com.et.exception.LoginFailed;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://busstat-server.herokuapp.com/";

    private Retrofit retrofit;
    private EffectiveTravelServerApi service;

    private static ApiClient inst = null;


    public static ApiClient instance() {
        if(inst == null) {
            inst = new ApiClient();
        }
        return inst;
    }

    private ApiClient() {
        Log.d("ApiClient", "Create client");

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client((new OkHttpClient.Builder()).build())
                .build();

        service = retrofit.create(EffectiveTravelServerApi.class);
        Log.d("ApiClient", "Create client .. done");
    }

    public String login(String login, String password) throws LoginFailed {
        Call<TokenResponse> req = service.token(new LoginBody(login, password));
        Log.d("ApiClient", "About to send request.");

        try {
            Log.d("ApiClient", "Sending request to server...");
            Response<TokenResponse> response = req.execute();
            Log.d("ApiClient", "Sending request to server... done");
            if(response.body().getSuccess())
                return response.body().getToken();
            else
                throw new LoginFailed();
        }
        catch (IOException e) {
            Log.d("ApiClient", "Exception caught while sending request.");
            throw new LoginFailed();
        }
    }
}
