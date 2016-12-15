package com.et.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.et.R;
import com.et.adapters.StationsListAdapter;
import com.et.adapters.StationsMockLocalStorage;
import com.et.api.ApiClient;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;
import com.et.stats.transport.TransportStatsManager;
import com.et.storage.AppSQliteDb;

import junit.framework.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddStatisticsActivity extends BaseActivity {

    private StationObject station;
    private RouteObject route;
    private TextView stationTextView;
    private TextView routeTextView;
    private TransportStatsManager manager;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new TransportStatsManager(ApiClient.instance(), AppSQliteDb.getInstance());
        setContentView(R.layout.activity_add_statistics);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        //timePicker.clearFocus();

        setContentView(R.layout.activity_add_statistics);
        station = getIntent().getParcelableExtra("STATION");
        route = getIntent().getParcelableExtra("ROUTE");

        stationTextView = (TextView) findViewById(R.id.stationView);
        routeTextView = (TextView) findViewById(R.id.routeView);
        stationTextView.setText("STATION - " + station.getTitle());
        routeTextView.setText("ROUTE - " + route.getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send_stat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NoteSubmitTask mNoteSubmitTask = new NoteSubmitTask(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                mNoteSubmitTask.execute((Void) null);

                Snackbar.make(view, "Data has been sent (or not)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public class NoteSubmitTask extends AsyncTask<Void, Void, Boolean> {

        int hours;
        int minutes;

        NoteSubmitTask(int hours, int minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("NoteSubmit", "Trying to submit note.");

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(GregorianCalendar.HOUR, hours);
            calendar.set(GregorianCalendar.MINUTE, minutes);

            return manager.submitNote(station.getS_id(), route.getR_id(), calendar.getTime());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.i("NoteSubmit", "Note was submitted");
            } else {
                Log.i("NoteSubmit", "Note wasn't submitted");
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

}
