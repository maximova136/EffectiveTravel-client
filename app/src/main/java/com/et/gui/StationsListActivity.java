package com.et.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.et.R;
import com.et.api.ApiClient;
import com.et.auth.Auth;
import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;
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
        public void clearStorage() throws DeleteObjectFailed {
            list.clear();
            savedList.clear();
            clearDbCalled = true;
        }
    }

    private static String TAG = "StationsListActivity";

    private StationsListActivity.StationsLoadTask mLoadStationTask = null;
    Context ctx;
    LayoutInflater lInflater;
    ListView stationsListView;
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
                Log.i("ITEM STATION", station.getTitle());
                /*Intent intent = new Intent(StationsListActivity.this, RouteListActivity.class);
                String message = "abc";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
*/
            }
        });

        mLoadStationTask = new StationsListActivity.StationsLoadTask(stations);
        mLoadStationTask.execute((Void) null);
    }

    private void onStationSelected(StationObject station) {

    }


    public class StationsLoadTask extends AsyncTask<Void, Void, Boolean> {

        StationsList stations;
        private MockLocalStorage mockLocalStorage;

        StationsLoadTask(StationsList stations) {
            this.stations = stations;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("StationsListActivity", "Trying to load stations.");

            mockLocalStorage = new MockLocalStorage();
            stations = new StationsList(ApiClient.instance(), mockLocalStorage);
            mockLocalStorage.clear();

            return stations.load();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            // create an adapter
            StationsListAdapter adapter = new StationsListAdapter(self, stations);
            //assign the adapter to the list
            stationsListView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {

        }
    }


}
