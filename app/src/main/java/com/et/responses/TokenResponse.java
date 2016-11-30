package com.et.responses;

import java.util.HashMap;
import java.util.Map;

public class TokenResponse extends BaseResponse {

    private String token;
    private String role;


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }
}