package com.et.api;


import android.util.Log;

import com.et.exception.LoginFailed;
import com.et.exception.SignupFailed;

public class Auth {

    private ApiClient client;

    private static String token = "";

    public Auth() {
        client = ApiClient.instance();
    }

    public static String getToken() {
        return token;
    }
    public static void setToken(String token) { Auth.token = token; }
    public static void logout() { token = ""; }

    public boolean login(String login, String password) {
        try {
            token = client.login(login, password);
//            Log.i("Auth", "Login successful, token: " + token);
            return true;
        }
        catch (LoginFailed e) {
//            Log.i("Auth", "Login failed.");
            return false;
        }
    }

    public boolean signup(String login, String password) {
        try {
            token = client.signup(login, password);
//            Log.i("Auth", "Sign up successful, token: " + token);
            return true;
        }
        catch (SignupFailed e) {
//            Log.i("Auth", "Sign up failed");
            return false;
        }
    }


}
