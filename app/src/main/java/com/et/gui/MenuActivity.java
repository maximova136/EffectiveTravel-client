package com.et.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.et.R;
import com.et.auth.Auth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        this.getIntent().addCategory(Intent.CATEGORY_HOME);

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.logout();
                // TODO зачем это в каждом обработчике?
                setToken(Auth.getToken());
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            }
        });


        Button transportStat = (Button) findViewById(R.id.transport_stat_button);
        transportStat.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(MenuActivity.this, StationsListActivity.class);
                  intent.putExtra("INIT_ACTIVITY", "MENU");
                  startActivity(intent);
                  //startActivity(new Intent(MenuActivity.this, StationsListActivity.class));
              }
        });

        Button personalStat = (Button) findViewById(R.id.personal_stats_button);
        personalStat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, PersonalStatsActivity.class));
            }
        });

        Button settingsButton = (Button) findViewById(R.id.setting_button);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Settings are coming soon", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_from_menu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //startActivity(new Intent(MenuActivity.this, AddStatisticsActivity.class));
                Intent intent = new Intent(MenuActivity.this, StationsListActivity.class);
                intent.putExtra("INIT_ACTIVITY", "SUBMIT");
                startActivity(intent);
            }
        });
    }
}
