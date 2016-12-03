package com.et.routes;

import com.et.exception.storage.LoadCollectionFailed;
import com.et.response.object.RouteObject;
import com.et.storage.ILocalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class RouteStorage implements IRouteStorage {
    public static String COLLECTION_NAME = "routes";

    public static String R_ID_KEY = "r_id";
    public static String TITLE_KEY = "title";
    public static String TYPE_KEY = "transport_type";
    public static String COST_KEY = "cost";

    private ILocalStorage storage;

    public RouteStorage(ILocalStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean save(List<RouteObject> routes) {
        return false;
    }

    @Override
    public List<RouteObject> load() {
        try {
            ArrayList<RouteObject> routes = new ArrayList<>();
            List< HashMap<String, String> > collection = storage.loadCollection(COLLECTION_NAME);

            if(collection == null || collection.size() <= 0)
                return null;

            for (HashMap<String, String> item : collection ) {
                RouteObject r = new RouteObject();
                r.setR_id(Integer.parseInt(item.get(R_ID_KEY)));
                r.setTransport_type(item.get(TYPE_KEY));
                r.setTitle(item.get(TITLE_KEY));
                r.setCost(Integer.parseInt(item.get(COST_KEY)));
                routes.add(r);
            }

            return routes;
        }
        catch (NumberFormatException e) {
            return null;
        }
        catch (LoadCollectionFailed e) {
            return null;
        }
    }
}
