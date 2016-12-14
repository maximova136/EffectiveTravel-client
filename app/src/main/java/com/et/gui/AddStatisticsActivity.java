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
import com.et.stations.StationsList;
import com.et.stats.transport.TransportStatsManager;
import com.et.storage.AppSQliteDb;

import junit.framework.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddStatisticsActivity extends BaseActivity {

    private int s_id;
    private int r_id;
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
        s_id = getIntent().getIntExtra("S_ID", -1);
        r_id = getIntent().getIntExtra("R_ID", -1);

        stationTextView = (TextView) findViewById(R.id.stationView);
        routeTextView = (TextView) findViewById(R.id.routeView);
        stationTextView.setText("S_ID - "+s_id);
        routeTextView.setText("ROUTE - " + r_id);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send_stat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NoteSubmitTask mNoteSubmitTask = new NoteSubmitTask(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                mNoteSubmitTask.execute((Void) null);

                Snackbar.make(view, "Data has been sent (or not)", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                /*

                manager = new TransportStatsManager(ApiClient.instance(), AppSQliteDb.getInstance());

                Date time = Calendar.getInstance().getTime();
                timePicker.clearFocus();

                int h = timePicker.getHour();
                int m = (timePicker.getMinute());
                */
                /*time.setHours(h);
                time.setMinutes(m);
                DatePicker simpleDatePicker = (DatePicker)findViewById(R.id.simpleDatePicker); // initiate a date picker
                int month = simpleDatePicker.getMonth();
                manager.submitNote(s_id, r_id, time);*/
                //startActivity(new Intent(TransportStatsActivity.this, AddStatisticsActivity.class));*/



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

            return manager.submitNote(495, 53, calendar.getTime());
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
