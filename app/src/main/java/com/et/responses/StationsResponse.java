package com.et.responses;

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
