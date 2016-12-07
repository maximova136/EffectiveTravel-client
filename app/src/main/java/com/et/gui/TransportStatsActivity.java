package com.et.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.et.R;

public class TransportStatsActivity extends AppCompatActivity {

    private int s_id;
    private int r_id;
    private TextView titleTextView;
    private TextView idsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        s_id = getIntent().getIntExtra("S_ID", -1);
        r_id = getIntent().getIntExtra("R_ID", -1);

        titleTextView = (TextView) findViewById(R.id.title);
        idsTextView = (TextView) findViewById(R.id.ids);
        idsTextView.setText("S_ID="+s_id+"    "+"R_ID="+r_id);
    }
}
