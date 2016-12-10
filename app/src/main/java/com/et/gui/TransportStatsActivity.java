package com.et.gui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.et.R;

public class TransportStatsActivity extends BaseActivity {

    public TransportStatsActivity() {
        super(true);
    }

    private int s_id;
    private int r_id;
    private TextView titleTextView;
    private TextView idsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transport_stats);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_from_TS);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(TransportStatsActivity.this, AddStatisticsActivity.class));

            }
        });
// end TODO: WTF is that? 2 activities for transport stats?
//        setContentView(R.layout.activity_statistics);
//
//        s_id = getIntent().getIntExtra("S_ID", -1);
//        r_id = getIntent().getIntExtra("R_ID", -1);
//
//        titleTextView = (TextView) findViewById(R.id.title);
//        idsTextView = (TextView) findViewById(R.id.ids);
//        idsTextView.setText("S_ID="+s_id+"    "+"R_ID="+r_id);

    }
}
