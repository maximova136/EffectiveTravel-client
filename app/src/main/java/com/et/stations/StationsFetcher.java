package com.et.stations;

import com.et.api.IApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.StationObject;

import java.util.List;


public class StationsFetcher implements IStationsFetcher {
    private IApiClient client;

    public StationsFetcher(IApiClient client) {
        this.client = client;
    }

    @Override
    public List<StationObject> fetchAll() throws RequestFailedException, InsuccessfulResponseException {
        return client.stations();
    }
}
