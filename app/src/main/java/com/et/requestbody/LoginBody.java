package com.et.requestbody;



public class LoginBody {
    private String login;
    private String password;

    public LoginBody(String login, String password) {
        this.login = login;
        this.password = password;
    }
}