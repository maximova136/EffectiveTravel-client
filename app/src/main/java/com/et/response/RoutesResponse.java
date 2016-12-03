package com.et.response;

import com.et.response.object.RouteObject;

import java.util.List;



public class RoutesResponse extends BaseResponse {

    List<RouteObject> routes;

    public List<RouteObject> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteObject> routes) {
        this.routes = routes;
    }
}
