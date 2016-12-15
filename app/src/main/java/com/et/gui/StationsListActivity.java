package com.et.gui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.et.R;
import com.et.adapters.StationsListAdapter;
import com.et.api.ApiClient;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;
import com.et.storage.AppSQliteDb;

import java.util.ArrayList;
import java.util.List;


public class StationsListActivity extends BaseActivity {

    private static String TAG = "StationsListActivity";

    private StationsListActivity.StationsLoadTask mLoadStationTask = null;
    Context ctx;
    LayoutInflater lInflater;

    ListView stationsListView;
    EditText editText;

    String searchWord = "";
    StationsList stations;
    StationsListActivity self = this;


    public StationsListActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_list);

        // find the list
        stationsListView = (ListView) findViewById(R.id.stations_list);

        stationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StationObject station = (StationObject) stationsListView.getAdapter().getItem(position);
                Intent intent = new Intent(StationsListActivity.this, RoutesListActivity.class);

                intent.putExtra("STATION", station);
                intent.putExtra("INIT_ACTIVITY", getIntent().getStringExtra("INIT_ACTIVITY"));

                startActivity(intent);
            }
        });

        editText = (EditText)findViewById(R.id.search_field);
        TextWatcher watcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                searchWord = s.toString();

                // form new Station List with suited stations
                List<StationObject> suitedStations = new ArrayList<>();
                for(StationObject st : stations.getAll()){
                    if (st.getTitle().toLowerCase().contains(searchWord.toLowerCase())){
                        suitedStations.add(st);
                    }
                }
                // create an adapter
                StationsListAdapter adapter = new StationsListAdapter(self, suitedStations);
                //assign the adapter to the list
                stationsListView.setAdapter(adapter);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        editText.addTextChangedListener(watcher);

        mLoadStationTask = new StationsListActivity.StationsLoadTask();
        mLoadStationTask.execute((Void) null);
    }


    public class StationsLoadTask extends AsyncTask<Void, Void, Boolean> {

        StationsLoadTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("StationsListActivity", "Trying to load stations.");

            stations = new StationsList(ApiClient.instance(), AppSQliteDb.getInstance());

            return stations.load();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            // create an adapter
            StationsListAdapter adapter = new StationsListAdapter(self, stations.getAll());
            //assign the adapter to the list
            stationsListView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {

        }
    }


}
