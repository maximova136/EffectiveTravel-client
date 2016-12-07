package com.et.stations;


import com.et.api.ApiClient;
import com.et.api.IApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.StationObject;
import com.et.storage.ILocalStorage;

import java.util.List;

public class StationsList implements IStationsProvider {
    private IStationsFetcher fetcher;
    private IStationsStorage storage;

    private List<StationObject> stations;

    public StationsList(IApiClient client, ILocalStorage localStorage) {
        fetcher = new StationsFetcher(client);
        storage = new StationsStorage(localStorage);
    }

    public boolean load() {
        if(!loadFromDb()) {
            try {
                stations = fetcher.fetchAll();
                saveToDb();
                return true;
            }
            catch(InsuccessfulResponseException e) {
                e.printStackTrace();
                return false;
            }
            catch (RequestFailedException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return true;
        }
    }

    private boolean loadFromDb() {
        stations = storage.load();
        return stations != null;
    }

    private boolean saveToDb() {
        return storage.save(stations);
    }

    @Override
    public List<StationObject> getAll() {
        return stations;
    }

    /*
    public int getSize() {
        return 3;
    }

    public StationObject getStation(int pos) { return stations.get(pos); }
*/
}
