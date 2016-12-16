package com.et.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.et.R;
import com.et.api.ApiClient;
import com.et.exception.manager.WrongTransportType;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;
import com.et.stats.personal.PersonalStatsManager;
import com.et.stats.transport.TransportStatsManager;
import com.et.storage.AppSQliteDb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddStatisticsActivity extends BaseActivity {

    private StationObject station;
    private RouteObject route;
    private TextView stationTextView;
    private TextView routeTextView;

    @Override
    protected void onResume() {
        super.onResume();
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    private TransportStatsManager manager;
    private TimePicker timePicker;
    private PersonalStatsManager personalStatsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new TransportStatsManager(ApiClient.instance(), AppSQliteDb.getInstance());
        personalStatsManager = new PersonalStatsManager(AppSQliteDb.getInstance());

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

                Toast.makeText(getApplicationContext(), "Data has been sent", Toast.LENGTH_SHORT).show();
                //Snackbar.make(view, "Data has been sent", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

            // TODO: WTF in Calendar
            Calendar calendar = (Calendar) Calendar.getInstance().clone();
            //calendar.setTime(new Date());
            calendar.set(GregorianCalendar.HOUR, hours);
            calendar.set(GregorianCalendar.MINUTE, minutes);

            try {
                personalStatsManager.incrementCounterForType(route.getTransport_type());
            } catch (WrongTransportType e) {
                e.printStackTrace();
            }

            return manager.submitNote(station.getS_id(), route.getR_id(), new Date());
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
