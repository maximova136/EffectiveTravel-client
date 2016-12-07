package com.et.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.et.R;
import com.et.auth.Auth;

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

        Button transportStat = (Button) findViewById(R.id.transport_stat_button);
        transportStat.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  //setToken(Auth.getToken());
                  //startActivity(new Intent(MenuActivity.this, TransportStatsActivity.class));
                  //I CAN'T UNDERSTAND WHY APP IS BEING STOPPED HERE
              }
        });

        Button personalStat = (Button) findViewById(R.id.personal_stats_button);
        personalStat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, PersonalStatsActivity.class));
            }
        });
    }
}
