package com.et.stations;


import com.et.api.ApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.StationObject;

import java.util.List;

public class StationsList implements IStationsProvider {
    private IStationsFetcher fetcher;
    private IStationsStorage storage;

    private List<StationObject> stations;

    public StationsList() {
        fetcher = new StationsFetcher(ApiClient.instance());
    }

    public boolean load() {
        if(!loadFromDb()) {
            try {
                stations = fetcher.fetchAll();
                saveToDb();
                return true;
            }
            catch(InsuccessfulResponseException e) {
                return false;
            }
            catch (RequestFailedException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }

    private boolean loadFromDb() {
        return false;
    }

    private void saveToDb() {  }

    @Override
    public List<StationObject> getAll() {
        return stations;
    }
}
