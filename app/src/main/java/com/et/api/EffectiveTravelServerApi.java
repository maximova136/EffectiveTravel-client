package com.et.api;

import com.et.requestbody.LoginBody;
import com.et.responses.RoutesResponse;
import com.et.responses.SignupResponse;
import com.et.responses.StationsResponse;
import com.et.responses.TokenResponse;

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
}
