package com.et.stations;


import com.et.response.object.StationObject;

import java.util.List;

public interface IStationsProvider {
    public List<StationObject> getAll();
    public StationObject getBySId(int sId);
}
