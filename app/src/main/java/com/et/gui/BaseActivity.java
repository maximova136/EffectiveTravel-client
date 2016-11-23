package com.et.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.et.R;


/**
 * Created by glaux on 23.11.16.
 */

public class BaseActivity extends Activity {
    private boolean requiresToken = false;

    public BaseActivity(boolean requiresToken) {
        this.requiresToken = requiresToken;
    }

    public boolean checkToken() {
        return getToken().length() > 3;
    }


    public void setToken(String token) {

    }

    public String getToken() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return token;
    }


    @Override
    protected void onResume() {
        if(this.requiresToken && !checkToken()) {
            Intent transitionToLogin = new Intent(this, LoginActivity.class);
            startActivity(transitionToLogin);
            return;
        }

        super.onResume();
    }
}
