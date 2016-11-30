package com.et.api;


import android.util.Log;

import com.et.exception.FetchRoutesException;
import com.et.responses.RouteObject;

import java.util.List;

public class Routes {
    private static String TAG = "Routes";

    private ApiClient client = ApiClient.instance();

    private List<RouteObject> routes;

    public Routes() {
        if(!loadFromLocalDb()) {
        }
    }

    public boolean load() throws FetchRoutesException {
//        if(!loadFromLocalDb()) {
            try {
                fetchFromServer();
                return true;
            }
            catch (FetchRoutesException e) {
                e.printStackTrace();
//                Log.e(TAG, "Failed to fetch routes from server. Error: " + e.getMessage());
                throw e;
//                return false;
            }
//        }
//        return true;
    }

    private void fetchFromServer() throws FetchRoutesException {
        this.routes = client.routes();
//        Log.i(TAG, "RN: " + routes.size());
    }

    private boolean loadFromLocalDb() {
        return false;
    }

    private boolean saveToLocalDb(List<RouteObject> routes) {
        return true;
    }

    public List<RouteObject> getRoutes() {
        return routes;
    }

}
