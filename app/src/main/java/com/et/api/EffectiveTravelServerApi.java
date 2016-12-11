package com.et.api;

import com.et.request.body.LoginBody;
import com.et.request.body.NoteBody;
import com.et.response.BaseResponse;
import com.et.response.RoutesResponse;
import com.et.response.SignupResponse;
import com.et.response.StationsResponse;
import com.et.response.StatisticsResponse;
import com.et.response.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EffectiveTravelServerApi {
    @POST("token")
    Call<TokenResponse> token(@Body LoginBody login_password);

    @POST("registration")
    Call<SignupResponse> registration(@Body LoginBody login_password);

    @GET("api/routes")
    Call<RoutesResponse> routes(@Header("Authorization") String token);

    @GET("api/stations")
    Call<StationsResponse> stations(@Header("Authorization") String token);

    @GET("api/statisitics/{s_id}/{r_id}")
    Call<StatisticsResponse> statistics(@Header("Authorization") String token, @Path("s_id") int s_id, @Path("r_id") int r_id);

    @POST("/api/notes/{s_id}/{r_id}")
    Call<BaseResponse> submitNote(@Header("Authorization") String token, @Path("s_id") int s_id, @Path("r_id") int r_id, @Body NoteBody noteBody);
}
