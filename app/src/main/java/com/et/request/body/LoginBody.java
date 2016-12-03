package com.et.request.body;



public class LoginBody {
    private String login;
    private String password;

    public LoginBody(String login, String password) {
        this.login = login;
        this.password = password;
    }
}