package com.et.stations;

import com.et.api.ApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.StationObject;

import java.util.List;


public class StationsFetcher implements IStationsFetcher {
    private ApiClient client;

    public StationsFetcher(ApiClient client) {
        this.client = client;
    }

    @Override
    public List<StationObject> fetchAll() throws RequestFailedException, InsuccessfulResponseException {
        return client.stations();
    }
}
