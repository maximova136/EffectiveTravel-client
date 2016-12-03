package com.et.routes;

import com.et.api.IApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.object.RouteObject;
import com.et.storage.ILocalStorage;

import java.util.List;


public class TransportRoutes implements IRouteProvider {
    private IRouteFetcher fetcher;
    private IRouteStorage storage;

    private List<RouteObject> routes;

    public TransportRoutes(IApiClient client, ILocalStorage localStorage) {
        this.fetcher = new RouteFetcher(client);
        this.storage = new RouteStorage(localStorage);
    }

    public boolean load() {
        if(!loadFromDb()) {
            try {
                routes = fetcher.fetchAll();
                saveToDb();
                return true;
            }
            catch (InsuccessfulResponseException e) {
                e.printStackTrace();
                return false;
            }
            catch (RequestFailedException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return true;
        }
    }

    private boolean loadFromDb() {
        routes = storage.load();
        return routes != null;
    }

    private boolean saveToDb() {
        return storage.save(routes);
    }

    @Override
    public List<RouteObject> getAll() {
        return routes;
    }
}
