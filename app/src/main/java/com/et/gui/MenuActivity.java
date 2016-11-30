package com.et.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.et.R;
import com.et.api.Auth;

import junit.framework.Test;

public class MenuActivity extends BaseActivity {
    private static String TAG = "MenuActivity";

    public MenuActivity() {
        super(true);
    }

    protected void checkLoggedIn() {
        if(this.requiresToken && !checkToken()) {
            Log.i(TAG, "Activity requires login, opening login activity...");
            Intent transitionToLogin = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(transitionToLogin);
            return;
        }
    }

    @Override
    protected void onResume() {
        checkLoggedIn();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.logout();
                setToken(Auth.getToken());
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            }
        });

        Button testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, TestActivity.class));
            }
        });
    }
}
