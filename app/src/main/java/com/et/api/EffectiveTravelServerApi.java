package com.et.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface EffectiveTravelServerApi {
    @POST("token")
    Call<TokenResponse> token(@Body LoginBody login);
}
