package com.et.auth;


import com.et.api.ApiClient;
import com.et.exception.api.LoginFailedException;
import com.et.exception.api.SignupFailedException;

public class Auth {
    private static String token = "";



    public static String getToken() {
        return token;
    }



    public static void setToken(String token) {
        Auth.token = token;
    }



    public static void logout() {
        token = "";
    }



    public static String getHeaderField() {
        return "JWT " + getToken();
    }



    public static boolean login(String login, String password) {
        try {
            token = ApiClient.instance().login(login, password);
            return true;
        }
        catch (LoginFailedException e) {
            return false;
        }
    }



    public static boolean signup(String login, String password) {
        try {
            token = ApiClient.instance().signup(login, password);
            return true;
        }
        catch (SignupFailedException e) {
            return false;
        }
    }
}
