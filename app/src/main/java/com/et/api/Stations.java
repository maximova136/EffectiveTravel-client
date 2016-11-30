package com.et.api;

import com.et.exception.BadResponseException;
import com.et.exception.FetchException;
import com.et.responses.StationObject;

import java.util.List;



public class Stations {

    private static String TAG = "Stations";

    private ApiClient client = ApiClient.instance();

    private List<StationObject> stations;

    public Stations() {
        if(!loadFromLocalDb()) {
        }
    }

    public boolean load() throws FetchException, BadResponseException {
//        if(!loadFromLocalDb()) {
        try {
            fetchFromServer();
            return true;
        }
        catch (FetchException e) {
            e.printStackTrace();
//                Log.e(TAG, "Failed to fetch routes from server. Error: " + e.getMessage());
            throw e;
//                return false;
        }
//        }
//        return true;
    }

    private void fetchFromServer() throws FetchException, BadResponseException {
        this.stations = client.stations();
//        Log.i(TAG, "RN: " + routes.size());
    }

    private boolean loadFromLocalDb() {
        return false;
    }

    private boolean saveToLocalDb(List<StationObject> routes) {
        return true;
    }

    public List<StationObject> getStations() {
        return stations;
    }

}
