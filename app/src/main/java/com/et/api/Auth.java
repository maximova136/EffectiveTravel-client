package com.et.api;


import android.util.Log;

import com.et.exception.LoginFailed;

public class Auth {

    private ApiClient client;

    private static String token = "";

    public Auth() {
        client = ApiClient.instance();
    }

    public boolean login(String login, String password) {
        try {
            token = client.login(login, password);
            Log.i("Auth", "Token: " + token);
            return true;
        }
        catch (LoginFailed e) {
            Log.i("Auth", "Login failed.");
            return false;
        }
    }

}
