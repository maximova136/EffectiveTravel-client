package com.et.routes;


import com.et.response.object.RouteObject;

import java.util.List;

public interface IRouteStorage {
    public boolean save(List<RouteObject> routes);
    public List<RouteObject> load();
}
