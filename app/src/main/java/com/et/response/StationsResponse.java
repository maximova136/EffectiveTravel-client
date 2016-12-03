package com.et.response;

import com.et.response.object.StationObject;

import java.util.List;




public class StationsResponse extends BaseResponse {
    protected List<StationObject> stations;

    public List<StationObject> getStations() {
        return stations;
    }

    public void setStations(List<StationObject> stations) {
        this.stations = stations;
    }
}
