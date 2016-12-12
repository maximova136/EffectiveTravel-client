package com.et.api;


import com.et.auth.Auth;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.exception.api.LoginFailedException;
import com.et.exception.api.SignupFailedException;
import com.et.request.body.LoginBody;
import com.et.request.body.NoteBody;
import com.et.response.BaseResponse;
import com.et.response.StatisticsObject;
import com.et.response.StatisticsResponse;
import com.et.response.object.RouteObject;
import com.et.response.RoutesResponse;
import com.et.response.SignupResponse;
import com.et.response.object.StationObject;
import com.et.response.StationsResponse;
import com.et.response.TokenResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements IApiClient {
    private static ApiClient inst = null;

    public static final String BASE_URL = "https://busstat-server.herokuapp.com/";
//    public static final String BASE_URL = "http://localhost:8000/";

    private Retrofit retrofit;
    private EffectiveTravelServerApi service;



    public static ApiClient instance() {
        if(inst == null) {
            inst = new ApiClient();
        }
        return inst;
    }



    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client((new OkHttpClient.Builder()).build())
                .build();

        service = retrofit.create(EffectiveTravelServerApi.class);
    }



    @Override
    public String login(String login, String password) throws LoginFailedException {
        Call<TokenResponse> req = service.token(new LoginBody(login, password));
        try {
            Response<TokenResponse> response = req.execute();
            if(response.body().isSuccess())
                return response.body().getToken();
            else
                throw new LoginFailedException("Bad login or password");
        }
        catch (IOException e) {
            throw new LoginFailedException(e);
        }
    }



    @Override
    public String signup(String login, String password) throws SignupFailedException {
        Call<SignupResponse> req = service.registration(new LoginBody(login, password));
        try {
            Response<SignupResponse> resp = req.execute();
            if(resp.body().isSuccess()) {
                return resp.body().getToken();
            }
            else {
                throw new SignupFailedException("Impossible to create account with specified login/password");
            }
        }
        catch (IOException e) {
            throw new SignupFailedException(e);
        }
    }



    @Override
    public List<RouteObject> routes() throws RequestFailedException, InsuccessfulResponseException {
        try {
            Call<RoutesResponse> request = service.routes(Auth.getHeaderField());
            Response<RoutesResponse> response = request.execute();
            if(response.body().isSuccess()) {
                return response.body().getRoutes();
            }
            else {
                throw new InsuccessfulResponseException("Failed to fetch routes from server.");
            }
        }
        catch (IOException e) {
            throw new RequestFailedException(e);
        }
    }



    @Override
    public List<StationObject> stations() throws RequestFailedException, InsuccessfulResponseException {
        try {
            Call<StationsResponse> request = service.stations(Auth.getHeaderField());
            Response<StationsResponse> response = request.execute();
            if(response.body().isSuccess()) {
                return response.body().getStations();
            }
            else {
                throw new InsuccessfulResponseException("Failed to fetch stations from server");
            }
        }
        catch (IOException e) {
            throw new RequestFailedException(e);
        }
    }



    @Override
    public StatisticsObject statistics(int s_id, int r_id) throws RequestFailedException, InsuccessfulResponseException {
        try {
            Call<StatisticsResponse> request = service.statistics(Auth.getHeaderField(), s_id, r_id);
            Response<StatisticsResponse> response = request.execute();

            if(response.body() == null || response.body().isSuccess()) {
                return response.body().getStatistics();
            }
            else {
                throw new InsuccessfulResponseException("Failed to fetch statistics for s_id:" + s_id + " r_id:" + r_id);
            }
        }
        catch (IOException e) {
            throw new RequestFailedException(e);
        }
    }



    @Override
    public void submitNote(int s_id, int r_id, String time) throws RequestFailedException, InsuccessfulResponseException {
        try {
            NoteBody noteBody = new NoteBody(time);
            Call<BaseResponse> request = service.submitNote(Auth.getHeaderField(), s_id, r_id, noteBody);
            Response<BaseResponse> response = request.execute();
            if(!response.body().isSuccess()) {
                throw new InsuccessfulResponseException("Failed to send new note to server s_id:" + s_id + " r_id:" + r_id);
            }
        }
        catch (IOException e) {
            throw new RequestFailedException(e);
        }
    }
}
