package com.et.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.et.R;
import com.et.exception.manager.InitPersonalStatsFailed;
import com.et.exception.manager.WrongTransportType;
import com.et.routes.RouteType;
import com.et.stats.personal.PersonalStatsManager;
import com.et.storage.AppSQliteDb;

public class PersonalStatsActivity extends BaseActivity implements View.OnClickListener {

    private PersonalStatsManager personalStatsManager;

    private TextView busCount;
    private TextView shuttleCount;
    private TextView tramCount;
    private TextView trolleyCount;
    private Button   resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_stats);

        personalStatsManager = new PersonalStatsManager(AppSQliteDb.getInstance());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_from_PS);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(PersonalStatsActivity.this, AddStatisticsActivity.class));
            }
        });


        busCount        = (TextView) findViewById(R.id.textBusCount);
        shuttleCount    = (TextView) findViewById(R.id.textShuttleCount);
        tramCount       = (TextView) findViewById(R.id.textTramCount);
        trolleyCount    = (TextView) findViewById(R.id.textTrolleyCount);

        resetButton = (Button) findViewById(R.id.buttonPersonalReset);
        resetButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Integer busTrips = new Integer(personalStatsManager.getNumberOfTripsForType(RouteType.BUS));
            Integer shuttleTrips = new Integer(personalStatsManager.getNumberOfTripsForType(RouteType.SHUTTLE));
            Integer tramTrips = new Integer(personalStatsManager.getNumberOfTripsForType(RouteType.TRAM));
            Integer trolleyTrips = new Integer(personalStatsManager.getNumberOfTripsForType(RouteType.TROLLEY));

            busCount.setText(busTrips.toString());
            shuttleCount.setText(shuttleTrips.toString());
            tramCount.setText(tramTrips.toString());
            trolleyCount.setText(trolleyTrips.toString());
        }
        catch (WrongTransportType e) {}
    }


    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.personal_reset_title))
                .setMessage(getString(R.string.personal_reset_description))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("PERSONAL", "Reset pressed");
                        try {
                            personalStatsManager.init();
                        }
                        catch (InitPersonalStatsFailed e) {
                            Log.e("PERSONAL", "Failed to reset");
                        }
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}
