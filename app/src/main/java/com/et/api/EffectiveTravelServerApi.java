package com.et.api;

import com.et.request.body.LoginBody;
import com.et.response.RoutesResponse;
import com.et.response.SignupResponse;
import com.et.response.StationsResponse;
import com.et.response.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface EffectiveTravelServerApi {
    @POST("token")
    Call<TokenResponse> token(@Body LoginBody login_password);

    @POST("registration")
    Call<SignupResponse> registration(@Body LoginBody login_password);

    @GET("api/routes")
    Call<RoutesResponse> routes(@Header("Authorization") String token);

    @GET("/api/stations")
    Call<StationsResponse> stations(@Header("Authorization") String token);

//    @POST("/api/notes/{s_id}/{r_id}")
//    Call<StationsResponse> addNote(@Header("Authorization") String token);
}
