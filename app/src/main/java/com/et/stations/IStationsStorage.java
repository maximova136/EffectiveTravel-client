package com.et.stations;



import com.et.response.object.StationObject;

import java.util.List;

public interface IStationsStorage {
    public boolean save(List<StationObject> routes);
    public List<StationObject> load();
}
