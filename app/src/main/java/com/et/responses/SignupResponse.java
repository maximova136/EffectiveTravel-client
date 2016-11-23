package com.et.responses;

/**
 * Created by glaux on 23.11.16.
 */

public class SignupResponse extends BaseResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
