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
import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;
import com.et.adapters.StationsMockLocalStorage;
import com.et.stations.StationsStorage;
import com.et.storage.ILocalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StationsListActivity extends BaseActivity {
    private class MockLocalStorage implements ILocalStorage {
        public ArrayList<StationObject> list;
        public ArrayList<StationObject> savedList;

        public boolean clearDbCalled = false;

        public MockLocalStorage() {
            savedList = new ArrayList<>();
            list = new ArrayList<>();
        }

        public void clear() {
            savedList.clear();
            list.clear();
            clearDbCalled = false;
        }

        public void put(StationObject station) {
            list.add(station);
        }

        @Override
        public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new PutObjectFailed("Incorrect collection name");

            StationObject r = new StationObject();
            r.setS_id(Integer.parseInt(values.get(StationsStorage.S_ID_KEY)));
            r.setTitle(values.get(StationsStorage.TITLE_KEY));

            savedList.add(r);
        }

        @Override
        public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new UpdateObjectFailed("Incorrect collection name");
        }

        @Override
        public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new DeleteObjectFailed("Incorrect collection name");

        }

        @Override
        public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new LoadCollectionFailed("Incorrect collection name");

            ArrayList< HashMap<String, String> > collectionList = new ArrayList<>();

            for (StationObject s : list ) {
                HashMap<String, String> item = new HashMap<>();
                item.put(StationsStorage.S_ID_KEY,  "" + s.getS_id());
                item.put(StationsStorage.TITLE_KEY, "" + new String(s.getTitle()));
                collectionList.add(item);
            }

            return collectionList;
        }

        @Override
        public void clearCollection(String collection) throws DeleteObjectFailed {
            if(collection != StationsStorage.COLLECTION_NAME)
                throw new DeleteObjectFailed("Incorrect collection name");

            list.clear();
            savedList.clear();
            clearDbCalled = true;
        }
    }

    private static String TAG = "StationsListActivity";

    private StationsListActivity.StationsLoadTask mLoadStationTask = null;
    Context ctx;
    LayoutInflater lInflater;
    StationsMockLocalStorage stationsMockLocalStorage;

    ListView stationsListView;
    EditText editText;

    String searchWord = "";
    StationsList stations;
    StationsListActivity self = this;


    public StationsListActivity() {
        super(true);
        // TODO: get rid of Mock
        stationsMockLocalStorage = new StationsMockLocalStorage();
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
                intent.putExtra("S_ID", station.getS_id());
                intent.putExtra("S_TITLE", station.getTitle());
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

        private StationsMockLocalStorage stationsMockLocalStorage;

        StationsLoadTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("StationsListActivity", "Trying to load stations.");

            stationsMockLocalStorage = new StationsMockLocalStorage();
            stations = new StationsList(ApiClient.instance(), stationsMockLocalStorage);

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
