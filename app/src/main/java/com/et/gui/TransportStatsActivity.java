package com.et.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.et.R;

public class TransportStatsActivity extends BaseActivity {

    public TransportStatsActivity(boolean requiresToken) {
        super(requiresToken);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transport_stats);
    }
}
