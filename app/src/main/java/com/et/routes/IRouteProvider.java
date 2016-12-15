package com.et.routes;

import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;

import java.util.List;




public interface IRouteProvider {
    public RouteObject getRouteById(int rId);
    public List<RouteObject> getAll();
    public List<RouteObject> getRoutesForStation(StationObject station);
}
