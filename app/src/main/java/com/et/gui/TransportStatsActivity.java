package com.et.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.et.R;
import com.et.api.ApiClient;
import com.et.response.object.FreqObject;
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
        String timeStamp = new SimpleDateFormat("H:mm").format(Calendar.getInstance().getTime());

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
        }
        return 0;
    }

    public TransportStatsActivity() {
        super(true);
    }

    private int s_id;
    private int r_id;
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
        s_id = getIntent().getIntExtra("S_ID", -1);
        r_id = getIntent().getIntExtra("R_ID", -1);

        stationTextView = (TextView) findViewById(R.id.station);
        routeTextView = (TextView) findViewById(R.id.route);
        stationTextView.setText("S_ID - "+s_id);
        routeTextView.setText("ROUTE - " + r_id);

        //get current day of week
        String dayOfTheWeek = new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime()).toLowerCase();


        if (dayOfTheWeek == "friday"){
            statistics = manager.getStats(s_id, r_id).getFridayFreq();
        } else if (dayOfTheWeek == "saturday" || dayOfTheWeek == "sunday"){
            statistics = manager.getStats(s_id, r_id).getWeekendFreq();
        } else {
            statistics = manager.getStats(s_id, r_id).getWeekdaysFreq();
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
            //labels[i] = "7:"+i*5;
            //labels[i] = (i%2 == 0 ? "pidor" : "ebuchij");
            //if (j == 3) j = 0;
        }

        //thirdly (LAST STEP)
        //you need to add *DataSet to *Data object(s)
        BarDataSet set = new BarDataSet(entries, "Statistics");
        set.setColor(Color.parseColor("#5482ca")); // ColorPrimary

        set.setStackLabels(labels);

        set.setLabel("ЕБУЧИЙ АНДРОИД");

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
        chart.moveViewTo(0,getTimeToX(), YAxis.AxisDependency.LEFT);

        //chart.moveViewTo(28f, 0f, chart.getAxisLeft().AxisDependency());
        /////!!!!!!///
        //here we will use getTimeToX() instead of argument!!!111!!!
        /////!!!!!!///

        chart.setVisibleXRangeMaximum(10f); //is set AFTER setting data
        chart.setMaxVisibleValueCount(5);

        chart.invalidate(); //refresh

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_from_TS);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(TransportStatsActivity.this, AddStatisticsActivity.class));

            }
        });
    }
}
