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
import android.widget.TextView;

import com.et.R;
import com.et.adapters.RoutesListAdapter;
import com.et.adapters.RoutesMockLocalStorage;
import com.et.api.ApiClient;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;
import com.et.routes.TransportRoutes;
import com.et.adapters.StationsMockLocalStorage;

import java.util.ArrayList;
import java.util.List;


public class RoutesListActivity extends BaseActivity {

    private static String TAG = "RoutesListActivity";

    private RoutesListActivity.RoutesLoadTask mLoadStationTask = null;
    Context ctx;
    LayoutInflater lInflater;
    StationsMockLocalStorage stationsMockLocalStorage;

    ListView routesListView;
    EditText editText;

    String searchWord = "";
    int s_id;
    String s_title;
    TransportRoutes routes;
    RoutesListActivity self = this;


    public RoutesListActivity() {
        super(true);
        // TODO: get rid of Mock
        stationsMockLocalStorage = new StationsMockLocalStorage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);

        s_id = getIntent().getIntExtra("S_ID", 0);
        s_title = getIntent().getStringExtra("S_TITLE");

        TextView sTitleTextView = (TextView) findViewById(R.id.sTitle);
        sTitleTextView.setText(s_title);
        // find the list
        routesListView = (ListView) findViewById(R.id.routes_list);

        routesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RouteObject route = (RouteObject) routesListView.getAdapter().getItem(position);
                Log.i("ITEM ROUTE", route.getTitle());

                String initActivity =  getIntent().getStringExtra("INIT_ACTIVITY");

                //CHOOSE NEXT ACTIVITY
                Intent intent;
                if (initActivity.equals("MENU")){
                    intent = new Intent(RoutesListActivity.this, TransportStatsActivity.class);
                } else {
                    intent = new Intent(RoutesListActivity.this, AddStatisticsActivity.class);
                }
                intent.putExtra("S_ID", s_id);
                intent.putExtra("R_ID", route.getR_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        editText = (EditText)findViewById(R.id.search_field);
        TextWatcher watcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                searchWord = s.toString();

                // form new Station List with suited stations
                List<RouteObject> suitedRoutes = new ArrayList<>();
                for(RouteObject route : routes.getAll()){
                    if (route.getTitle().toLowerCase().contains(searchWord.toLowerCase())){
                        suitedRoutes.add(route);
                    }
                }
                // create an adapter
                RoutesListAdapter adapter = new RoutesListAdapter(self, suitedRoutes);
                //assign the adapter to the list
                routesListView.setAdapter(adapter);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        editText.addTextChangedListener(watcher);

        mLoadStationTask = new RoutesListActivity.RoutesLoadTask();
        mLoadStationTask.execute((Void) null);
    }


    private void onStationSelected(StationObject station) {

    }


    public class RoutesLoadTask extends AsyncTask<Void, Void, Boolean> {

        private RoutesMockLocalStorage routesMockLocalStorage;

        RoutesLoadTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("RoutesListActivity", "Trying to load routes.");

            routesMockLocalStorage = new RoutesMockLocalStorage();
            routes = new TransportRoutes(ApiClient.instance(), routesMockLocalStorage);

            return routes.load();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            // create an adapter
            RoutesListAdapter adapter = new RoutesListAdapter(self, routes.getAll());
            //assign the adapter to the list
            routesListView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {

        }
    }


}
