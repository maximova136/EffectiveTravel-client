package com.et.api;


import android.util.Log;

import com.et.exception.BadResponseException;
import com.et.exception.FetchException;
import com.et.exception.LoginFailed;
import com.et.exception.SignupFailed;
import com.et.requestbody.LoginBody;
import com.et.responses.BaseResponse;
import com.et.responses.RouteObject;
import com.et.responses.RoutesResponse;
import com.et.responses.SignupResponse;
import com.et.responses.StationObject;
import com.et.responses.StationsResponse;
import com.et.responses.TokenResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String TAG = "ApiClient";

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
//        Log.d("ApiClient", "Create client");

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client((new OkHttpClient.Builder()).build())
                .build();

        service = retrofit.create(EffectiveTravelServerApi.class);
//        Log.d("ApiClient", "Create client .. done");
    }

    public String login(String login, String password) throws LoginFailed {
        Call<TokenResponse> req = service.token(new LoginBody(login, password));
//        Log.d("ApiClient", "About to send request.");

        try {
//            Log.d("ApiClient", "Sending request to server...");
            Response<TokenResponse> response = req.execute();
//            Log.d("ApiClient", "Sending request to server... done");
            if(response.body().isSuccess())
                return response.body().getToken();
            else
                throw new LoginFailed();
        }
        catch (IOException e) {
//            Log.d("ApiClient", "Exception caught while sending request.");
            throw new LoginFailed();
        }
    }


    public String signup(String login, String password) throws SignupFailed {
        Call<SignupResponse> req = service.registration(new LoginBody(login, password));
        Log.d("ApiClient", "About to send sign up request.");

        try {
            Log.d("ApiClient", "Sending sing up request to server...");
            Response<SignupResponse> resp = req.execute();
            Log.d("ApiClient", "Sending sing up request to server... done");
            if(resp.body().isSuccess()) {
                return resp.body().getToken();
            }
            else {
                throw new SignupFailed();
            }
        }
        catch (IOException e) {
            Log.d("ApiClient", "Exception caught while sending sign up request.");
            throw new SignupFailed();
        }
    }

    public List<RouteObject> routes() throws FetchException {
        Call<RoutesResponse> req = service.routes("JWT " + Auth.getToken());
//        Log.d(TAG, "About to fetch routes list from server. Authorization: " + "JWT " + Auth.getToken());
        try {
//            Log.d(TAG, "Fetching routes from server...");
            Response<RoutesResponse> response = req.execute();
//            Log.d(TAG, "Fetching routes from server... done");
            if(response.body().isSuccess()) {
//                int i = response.body().getRoutes().size();
//                Log.i(TAG, "Number of routes: " + response.body().getAdditionalProperties().toString());
                return response.body().getRoutes();
            }
            else {
                throw new FetchException("EXAMPLE_ERROR_CODE");
            }
        }
        catch (RuntimeException e) {
//            Log.e(TAG, "Error " + e.getMessage());
            e.printStackTrace();
            throw new FetchException(e);

        }
        catch (IOException e) {
            e.printStackTrace();
            throw new FetchException(e);
        }
        catch (FetchException e) {
            e.printStackTrace();
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new FetchException(e);
        }
    }

    public List<StationObject> stations() throws FetchException, BadResponseException {
        try {
            Call<StationsResponse> request = service.stations("JWT " + Auth.getToken());
            Response<StationsResponse> response = request.execute();
            if(response.body().isSuccess()) {
                return response.body().getStations();
            }
            else {
                throw new BadResponseException();
            }
        }
        catch (IOException e) {
            throw new FetchException(e);
        }
    }
}
