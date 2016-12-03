package com.et.gui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.et.R;
import com.et.auth.Auth;


public class BaseActivity extends Activity {
    private static String TAG = "BaseActivity";

    protected boolean requiresToken = false;

    public BaseActivity(boolean requiresToken) {
        this.requiresToken = requiresToken;
    }

    public boolean checkToken() {
        return getToken().length() > 3;
    }


    public void setToken(String token) {
        Log.i(TAG, "Saving token(" + token + ") ...");
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        Auth.setToken(token);
        return token;
    }
}
