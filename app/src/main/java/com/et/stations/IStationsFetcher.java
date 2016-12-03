package com.et.stations;

import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.StationObject;

import java.util.List;


public interface IStationsFetcher {
    public List<StationObject> fetchAll() throws RequestFailedException, InsuccessfulResponseException;
}
