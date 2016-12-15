package com.et.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.et.R;
import com.et.api.ApiClient;
import com.et.response.object.FreqObject;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;
import com.et.stats.transport.ITransportStatsManager;
import com.et.stats.transport.TransportStatsManager;
import com.et.storage.AppSQliteDb;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ColorFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransportStatsActivity extends BaseActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    private TransportStatsManager manager;
    private List <FreqObject> statistics;

    public int getTimeToX(){
        /*String timeStamp = new SimpleDateFormat("H:mm").format(Calendar.getInstance().getTime());

        int hours   = Integer.parseInt(timeStamp.substring(0,2));
        int minutes = Integer.parseInt(timeStamp.substring(3,5));
        if (minutes % 5 != 0) {
            minutes -= minutes%5;
        }
        String timeStampNew = timeStamp.substring(0,3);
        if (minutes < 10)
            timeStampNew += "0";
        timeStampNew += minutes;

        String beginTime = statistics.get(0).getTime();
        int minH   = Integer.parseInt(beginTime.substring(0,2));
        int minM   = Integer.parseInt(beginTime.substring(3,5));

        if (hours < minH && minutes < minM)
            return 0 ;

        for (FreqObject i : statistics){
            if(i.equals(timeStampNew)){
                return statistics.indexOf(i);
            }
        }*/
        Calendar timeStamp = Calendar.getInstance();
        int hours   = timeStamp.get(timeStamp.HOUR_OF_DAY);
        System.out.println(hours);
        int minutes   = timeStamp.get(timeStamp.MINUTE);
        System.out.println(minutes);
        int sumMin = hours*60+minutes;
        int interval = sumMin/5;
        System.out.println(interval);
        return interval+5;
    }

    public TransportStatsActivity() {
        super(true);
    }

    private StationObject station;
    private RouteObject route;
    private TextView stationTextView;
    private TextView routeTextView;

    public class LabelFuckingFormatter implements IAxisValueFormatter{
        private final String[] mLabels;

        public LabelFuckingFormatter(String[] labels){
            mLabels = labels;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new TransportStatsManager(ApiClient.instance(), AppSQliteDb.getInstance());

        setContentView(R.layout.activity_transport_stats);
        station = getIntent().getParcelableExtra("STATION");
        route = getIntent().getParcelableExtra("ROUTE");

        stationTextView = (TextView) findViewById(R.id.station);
        routeTextView = (TextView) findViewById(R.id.route);
        stationTextView.setText("STATION - "+ station.getTitle());
        routeTextView.setText("ROUTE - " + route.getTitle() + " (" + route.getTransport_type() + ")");

        //get current day of week
        String dayOfTheWeek = new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime()).toLowerCase();

        StatisticsLoadTask mStatisticsLoadTask = new StatisticsLoadTask(dayOfTheWeek);
        mStatisticsLoadTask.execute((Void) null);

/*
*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_from_TS);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(TransportStatsActivity.this, AddStatisticsActivity.class);
                intent.putExtra("STATION", station);
                intent.putExtra("ROUTE", route);
                startActivity(intent);

            }
        });
    }

    public class StatisticsLoadTask extends AsyncTask<Void, Void, Boolean> {

        String day;

        StatisticsLoadTask(String day) {
            this.day = day;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("StatisticsLoad", "Trying to load statistics.");

            if (day == "friday"){
                statistics = manager.getStats(station.getS_id(), route.getR_id()).getFridayFreq();
            } else if (day == "saturday" || day == "sunday"){
                statistics = manager.getStats(station.getS_id(), route.getR_id()).getWeekendFreq();
            } else {
                statistics = manager.getStats(station.getS_id(), route.getR_id()).getWeekdaysFreq();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.i("StatisticsLoad", "Statistics was loaded");

            if(statistics == null) {
               // TODO: null handler
            }

            //graphics steps
            //first - create view
            HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.chart);

            //second - Adding data
            //you need to create list of Entry type
            List <BarEntry> entries = new ArrayList<BarEntry>();

            //some data;
            float a[] = {0.3f, 0.67f, 0.95f};
            int j = 0;
            String labels[] = new String [statistics.size()];
            //add coordinates Entry(getValueX(), getValueY());

            //string - time, float - probability
            for (int i = 0; i < statistics.size() ; i++){
                entries.add(new BarEntry(i, statistics.get(i).getCount()));
                labels[i] = statistics.get(i).getTime();
            }

            //thirdly (LAST STEP)
            //you need to add *DataSet to *Data object(s)
            BarDataSet set = new BarDataSet(entries, "Statistics");
            set.setColor(Color.parseColor("#5482ca")); // ColorPrimary

            set.setStackLabels(labels);

            BarData data = new BarData(set);

            //data.setHighlightEnabled(false); //чтобы не нажимались графики. но без этого выглядит грустно
            //data.setBarWidth(0.1f);
            //data.setDrawValues(true);

            chart.setData(data);
            chart.getXAxis().setValueFormatter(new LabelFuckingFormatter(labels));

            //it seems that it doesn't work :(
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getXAxis().setDrawGridLines(false);
            chart.setDrawGridBackground(false);

//        chart.setDrawGridBackground(false); //don't know for what  but pls DO NOT DELETE IT
            chart.setFitBars(true);
            chart.setVisibleXRangeMaximum(10f); //is set AFTER setting data
            chart.setMaxVisibleValueCount(5);
            //-30!!!!!!!!!!!!!
            //chart.moveViewTo(0,getTimeToX(), YAxis.AxisDependency.LEFT);
            chart.moveViewTo(0,getTimeToX(), YAxis.AxisDependency.LEFT);
            //chart.moveViewTo(28f, 0f, chart.getAxisLeft().AxisDependency());
            /////!!!!!!///
            //here we will use getTimeToX() instead of argument!!!111!!!
            /////!!!!!!///

            chart.setVisibleXRangeMaximum(10f); //is set AFTER setting data
            chart.setMaxVisibleValueCount(5);

            chart.invalidate(); //refresh
        }

        @Override
        protected void onCancelled() {

        }
    }
}
